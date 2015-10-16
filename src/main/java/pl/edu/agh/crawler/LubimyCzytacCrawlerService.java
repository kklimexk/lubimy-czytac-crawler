package pl.edu.agh.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pl.edu.agh.model.User;
import pl.edu.agh.util.CrawlerUtil;

import java.io.IOException;

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

            Element basicInformationDiv = profileHeaderInfoDiv.after(userDescriptionSpan).child(0);
            String basicInformation = basicInformationDiv.text();

            return new User(userName, userDescription, basicInformation);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
