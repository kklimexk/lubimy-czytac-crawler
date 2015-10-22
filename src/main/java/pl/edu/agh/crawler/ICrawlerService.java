package pl.edu.agh.crawler;

import org.jsoup.nodes.Document;
import pl.edu.agh.model.Book;
import pl.edu.agh.model.User;

public interface ICrawlerService {
    User crawlUserFromUrl(Document doc);
    Book crawlBookFromUrl(Document doc);
}
