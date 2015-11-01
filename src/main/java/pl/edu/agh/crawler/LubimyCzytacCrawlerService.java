package pl.edu.agh.crawler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.http.PageDownloader;
import pl.edu.agh.model.*;
import pl.edu.agh.util.CrawlerUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class LubimyCzytacCrawlerService implements ICrawlerService {

    final Logger logger = LoggerFactory.getLogger(LubimyCzytacCrawlerService.class);

    @Override
    public User crawlUserFromUrl(Document doc) {

        if (doc.location().contains("brak-uprawnien-konto")) return null;

        Element profileHeader = doc.getElementsByClass("profile-header").first();

        Element userNameH = profileHeader.getElementsByClass("title").first();
        String userName = CrawlerUtil.removeLastChar(userNameH.ownText());

        Element profileHeaderInfoDiv = doc.getElementsByClass("profile-header-info").first();

        Element userDescriptionSpan = profileHeaderInfoDiv.getElementsByTag("span").first();
        String userDescription = userDescriptionSpan.ownText();

        Element basicInformationDiv = profileHeaderInfoDiv.select("div.font-szary-a3.spacer-10-t").first();
        String basicInformation = basicInformationDiv.text();

        String readBooksUrl = "";
        Integer numOfReadBooks = 0;
        String currentlyReadingBooksUrl = "";
        Integer numOfCurrentlyReadingBooks = 0;
        String wantToReadBooksUrl = "";
        Integer numOfWantToReadBooks = 0;

        try {
            Document shelf = PageDownloader.getPage(doc.location() + "/biblioteczka/miniatury");

            Element readBooksUrlEl = shelf.select("a[href*=/przeczytane/]").first();
            readBooksUrl = readBooksUrlEl.attr("abs:href");
            numOfReadBooks = Integer.valueOf(readBooksUrlEl.parent().getElementsByAttribute("data-shelf-id-counter").text());

            Element currentlyReadingBooksUrlEl = shelf.select("a[href*=/teraz-czytam/]").first();
            currentlyReadingBooksUrl = currentlyReadingBooksUrlEl.attr("abs:href");
            numOfCurrentlyReadingBooks = Integer.valueOf(currentlyReadingBooksUrlEl.parent().getElementsByAttribute("data-shelf-id-counter").text());

            Element wantToReadBooksUrlEl = shelf.select("a[href*=/chce-przeczytac/]").first();
            wantToReadBooksUrl = wantToReadBooksUrlEl.attr("abs:href");
            numOfWantToReadBooks = Integer.valueOf(wantToReadBooksUrlEl.parent().getElementsByAttribute("data-shelf-id-counter").text());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new User(userName, userDescription, basicInformation, doc.location(), readBooksUrl, currentlyReadingBooksUrl, wantToReadBooksUrl, numOfReadBooks, numOfCurrentlyReadingBooks, numOfWantToReadBooks);
    }

    @Override
    public Set<Book> crawlUserBooksFromUrl(Document doc, OptionalInt lastPageOpt) {

        Set<Book> books = new HashSet<>();
        try {
            Element pagerDefault = doc.select("table.pager-default").first();
            Element tdClassCentered = null;

            if (pagerDefault != null) tdClassCentered = pagerDefault.select("td.centered").first();

            Integer lastPage = null;

            if (pagerDefault == null) lastPage = 1;
            else if (lastPageOpt.isPresent()) lastPage = lastPageOpt.getAsInt();
            else lastPage = Integer.valueOf(tdClassCentered.getElementsByTag("li").last().text());

            for (int i = 1; i <= lastPage; ++i) {
                logger.info("Crawling (books): " + i + " page");

                Document bookPage = PageDownloader.getPage(doc.location() + "/" + i);

                Elements booksList = bookPage.select("a[href~=/ksiazka/[0-9]+/]");
                books.addAll(booksList.stream().map(bookEl -> {
                    try {
                        Book book = crawlBookFromUrl(PageDownloader.getPage(bookEl.attr("href")));
                        logger.info(book.toString());
                        return book;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return books;
    }

    @Override
    public Set<User> crawlUserFriendsFromUrl(Document doc, OptionalInt lastPageOpt) {
        Set<User> users = new HashSet<>();
        try {
            Element pagerDefault = doc.select("table.pager-default").first();
            Element tdClassCentered = null;

            if (pagerDefault != null) tdClassCentered = pagerDefault.select("td.centered").first();

            Integer lastPage = null;

            if (pagerDefault == null) lastPage = 1;
            else if (lastPageOpt.isPresent()) lastPage = lastPageOpt.getAsInt();
            else lastPage = Integer.valueOf(tdClassCentered.getElementsByTag("li").last().text());

            for (int i = 1; i <= lastPage; ++i) {
                logger.info("Crawling (users): " + i + " page");
                Document friendsDoc = PageDownloader.getPage(doc.location() + "/" + i);
                Element accountList = friendsDoc.select("ul.account-list").first();
                Elements accountsElements = accountList.select("a.name");
                users.addAll(accountsElements.stream().map(account -> {
                    try {
                        User user = crawlUserFromUrl(PageDownloader.getPage(account.attr("href")));
                        if (user != null) {
                            logger.info(user.toString());
                            return user;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toSet()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        users.remove(null);
        return users;
    }

    @Override
    public Book crawlBookFromUrl(Document doc) {
        try {
            String bookName = doc.select("h1[itemprop=name]").text();

            Elements authorsSpan = doc.select("span[itemprop=author]");
            Elements authorsLinks = authorsSpan.select("a[itemprop=name]");
            Set<Author> authors = authorsLinks.stream().map(author -> new Author(author.text(), author.attr("href"))).collect(Collectors.toSet());

            Element publisherElement = doc.select("a[href*=/wydawnictwo/]").first();
            String publisherName = "";
            String publisherLink = "";
            Publisher publisher = null;
            if (publisherElement != null) {
                publisherName = publisherElement.text();
                publisherLink = publisherElement.attr("href");
                publisher = new Publisher(publisherName, publisherLink);
            }

            Double ratingValue = null;
            Element ratingValueEl = doc.getElementById("rating-value");
            if (ratingValueEl != null && !ratingValueEl.select("span[itemprop=ratingValue]").isEmpty() && !ratingValueEl.select("span[itemprop=ratingValue]").text().isEmpty())
                ratingValue = Double.parseDouble(ratingValueEl.select("span[itemprop=ratingValue]").text().replace(',', '.'));

            Integer ratingVotes = null;
            Element ratingVotesEl = doc.getElementById("rating-votes");
            if (ratingVotesEl != null && !ratingVotesEl.select("span[itemprop=ratingCount").isEmpty() && !ratingVotesEl.select("span[itemprop=ratingCount").text().isEmpty())
                ratingVotes = Integer.parseInt(ratingVotesEl.select("span[itemprop=ratingCount").text());

            Integer ratingAmount = null;
            Element ratingAmountEl = doc.getElementById("rating-amount");
            if (ratingAmountEl != null && !ratingAmountEl.text().isEmpty())
                ratingAmount = Integer.parseInt(ratingAmountEl.text());

            Element dBookDetailsDiv = doc.getElementById("dBookDetails");

            Date datePublished = null;
            Elements datePublishedElement = dBookDetailsDiv.select("dd[itemprop=datePublished]");
            if (!datePublishedElement.isEmpty()) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                datePublished = dateFormat.parse(datePublishedElement.attr("content"));
            }

            String isbn = dBookDetailsDiv.select("span[itemprop=isbn]").text();

            Integer numOfPages = null;
            Elements numOfPagesElement = dBookDetailsDiv.select(":contains(liczba stron)");
            if (!numOfPagesElement.isEmpty())
                numOfPages = Integer.parseInt(numOfPagesElement.parents().last().select("dd").text());

            Elements categoryElements = dBookDetailsDiv.select("a[itemprop=genre]");
            Set<Category> categories = categoryElements.stream().map(categoryElem -> new Category(categoryElem.getElementsByTag("span").text(), "http://lubimyczytac.pl/" + categoryElem.attr("href"))).collect(Collectors.toSet());

            String language = dBookDetailsDiv.select("dd[itemprop=inLanguage").text();
            String description = doc.select("p.description.regularText").text();

            Integer numOfComments = Integer.valueOf(doc.getElementById("headerReviewsCounter").text());

            return new Book(bookName, authors, publisher, ratingValue, ratingVotes, ratingAmount, datePublished, isbn, numOfPages, categories, language, description, doc.location(), numOfComments);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
