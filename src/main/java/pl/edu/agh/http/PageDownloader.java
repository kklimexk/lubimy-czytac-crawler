package pl.edu.agh.http;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pl.edu.agh.util.PropertiesUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class PageDownloader {

    public static Document getPage(String url) throws IOException {
        return Jsoup.connect(url).timeout(0).get();
    }

    public static Document getAuthenticatedPage(String url) throws IOException {
        Map loginCookies = getLoginCookies();
        Document doc = Jsoup.connect(url).cookies(loginCookies).timeout(0).get();
        return doc;
    }

    private static Map<String, String> getLoginCookies() throws IOException {
        Properties lubimyCzytacProp = PropertiesUtil.getProperties("lubimyczytac.properties");
        String login = lubimyCzytacProp.getProperty("login");
        String password = lubimyCzytacProp.getProperty("password");

        Connection.Response res = Jsoup.connect("http://lubimyczytac.pl/zaloguj/c")
                .data("Email", login, "Password", password)
                .method(Connection.Method.POST)
                .execute();

        Map<String, String> loginCookies = res.cookies();

        return loginCookies;
    }
}
