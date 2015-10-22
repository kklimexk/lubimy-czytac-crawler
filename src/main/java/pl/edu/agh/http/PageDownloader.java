package pl.edu.agh.http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class PageDownloader {
    public static Document getPage(String url) throws IOException {
        return Jsoup.connect(url).timeout(0).get();
    }
}
