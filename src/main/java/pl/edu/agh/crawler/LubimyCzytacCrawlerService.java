package pl.edu.agh.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pl.edu.agh.model.Book;
import pl.edu.agh.model.User;
import pl.edu.agh.util.CrawlerUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LubimyCzytacCrawlerService implements ICrawlerService {

    @Override
    public User crawlUserFromUrl(String userProfileUrl) {
        try {
            Document doc = Jsoup.connect(userProfileUrl).timeout(TIMEOUT).get();
            Element profileHeader = doc.getElementsByClass("profile-header").first();

            Element userNameH = profileHeader.getElementsByClass("title").first();
            String userName = CrawlerUtil.removeLastChar(userNameH.ownText());

            Element profileHeaderInfoDiv = doc.getElementsByClass("profile-header-info").first();

            Element userDescriptionSpan = profileHeaderInfoDiv.getElementsByTag("span").first();
            String userDescription = userDescriptionSpan.ownText();

            Element basicInformationDiv = profileHeaderInfoDiv.select("div.font-szary-a3.spacer-10-t").first();
            String basicInformation = basicInformationDiv.text();

            return new User(userName, userDescription, basicInformation, userProfileUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Book crawlBookFromUrl(String bookUrl) {
        try {
            Document doc = Jsoup.connect(bookUrl).timeout(TIMEOUT).get();

            String bookName = doc.select("h1[itemprop=name]").text();
            Double ratingValue = Double.parseDouble(doc.getElementById("rating-value").select("span[itemprop=ratingValue]").text().replace(',','.'));
            Integer ratingVotes = Integer.parseInt(doc.getElementById("rating-votes").select("span[itemprop=ratingCount").text());
            Integer ratingAmount = Integer.parseInt(doc.getElementById("rating-amount").text());

            Element dBookDetailsDiv = doc.getElementById("dBookDetails");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date datePublished = dateFormat.parse(dBookDetailsDiv.select("dd[itemprop=datePublished]").attr("content"));
            String isbn = dBookDetailsDiv.select("span[itemprop=isbn]").text();
            Integer numOfPages = Integer.parseInt(dBookDetailsDiv.select(":contains(liczba stron)").parents().last().select("dd").text());
            String language = dBookDetailsDiv.select("dd[itemprop=inLanguage").text();
            String description = doc.select("p.description.regularText").text();

            return new Book(bookName, ratingValue, ratingVotes, ratingAmount, datePublished, isbn, numOfPages, language, description, bookUrl);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
