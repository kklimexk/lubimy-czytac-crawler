package pl.edu.agh.crawler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.edu.agh.http.PageDownloader;
import pl.edu.agh.model.*;
import pl.edu.agh.util.CrawlerUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class LubimyCzytacCrawlerService implements ICrawlerService {

    @Override
    public User crawlUserFromUrl(Document doc) {
        Element profileHeader = doc.getElementsByClass("profile-header").first();

        Element userNameH = profileHeader.getElementsByClass("title").first();
        String userName = CrawlerUtil.removeLastChar(userNameH.ownText());

        Element profileHeaderInfoDiv = doc.getElementsByClass("profile-header-info").first();

        Element userDescriptionSpan = profileHeaderInfoDiv.getElementsByTag("span").first();
        String userDescription = userDescriptionSpan.ownText();

        Element basicInformationDiv = profileHeaderInfoDiv.select("div.font-szary-a3.spacer-10-t").first();
        String basicInformation = basicInformationDiv.text();

        return new User(userName, userDescription, basicInformation, doc.location());
    }

    @Override
    public Set<Book> crawlUserBooksFromUrl(Document doc) {
    	doc.appendText("/biblioteczka/miniatury");
        Elements booksList = doc.select("a[href~=/ksiazka/[0-9]+/]");
        Set<Book> books = booksList.stream().map(bookEl -> {
            try {
                return crawlBookFromUrl(PageDownloader.getPage(bookEl.attr("href")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toSet());

        return books;
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

            Double ratingValue = Double.parseDouble(doc.getElementById("rating-value").select("span[itemprop=ratingValue]").text().replace(',', '.'));
            Integer ratingVotes = Integer.parseInt(doc.getElementById("rating-votes").select("span[itemprop=ratingCount").text());
            Integer ratingAmount = Integer.parseInt(doc.getElementById("rating-amount").text());

            Element dBookDetailsDiv = doc.getElementById("dBookDetails");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            String stringDate = dBookDetailsDiv.select("dd[itemprop=datePublished]").attr("content");
            
            Date datePublished = null;
            if(!stringDate.equals("")) {
            	datePublished = dateFormat.parse(dBookDetailsDiv.select("dd[itemprop=datePublished]").attr("content"));
            }
            
            String isbn = dBookDetailsDiv.select("span[itemprop=isbn]").text();
            Integer numOfPages = Integer.parseInt(dBookDetailsDiv.select(":contains(liczba stron)").parents().last().select("dd").text());

            Elements categoryElements = dBookDetailsDiv.select("a[itemprop=genre]");
            Set<Category> categories = categoryElements.stream().map(categoryElem -> new Category(categoryElem.getElementsByTag("span").text(), "http://lubimyczytac.pl/" + categoryElem.attr("href"))).collect(Collectors.toSet());

            String language = dBookDetailsDiv.select("dd[itemprop=inLanguage").text();
            String description = doc.select("p.description.regularText").text();

            return new Book(bookName, authors, publisher, ratingValue, ratingVotes, ratingAmount, datePublished, isbn, numOfPages, categories, language, description, doc.location());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
