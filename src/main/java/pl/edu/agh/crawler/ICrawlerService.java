package pl.edu.agh.crawler;

import org.jsoup.nodes.Document;
import pl.edu.agh.model.Book;
import pl.edu.agh.model.User;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

public interface ICrawlerService {
    User crawlUserFromUrl(Document doc);
    Book crawlBookFromUrl(Document doc);
    Set<Book> crawlUserBooksFromUrl(Document doc, OptionalInt lastPageOpt);
    Set<User> crawlUserFriendsFromUrl(Document doc, OptionalInt lastPageOpt);
}
