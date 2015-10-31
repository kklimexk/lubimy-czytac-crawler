package pl.edu.agh.crawler;

import org.jsoup.nodes.Document;
import pl.edu.agh.http.PageDownloader;
import pl.edu.agh.model.Book;
import pl.edu.agh.model.User;
import pl.edu.agh.service.*;

import java.io.IOException;
import java.util.OptionalInt;
import java.util.Set;

public class LubimyCzytacCrawlerWorker implements Runnable {

    private final ICrawlerService crawlerService;

    private final UserService userService = new UserService();
    private final BookService bookService = new BookService();
    private final AuthorService authorService = new AuthorService();
    private final CategoryService categoryService = new CategoryService();
    private final PublisherService publisherService = new PublisherService();

    public LubimyCzytacCrawlerWorker(ICrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @Override
    public void run() {
        try {
            Document userPage = PageDownloader.getPage("http://lubimyczytac.pl/profil/802/joanna-kalio-golaszewska");
            User user = crawlerService.crawlUserFromUrl(userPage);
            Set<Book> readBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(user.getReadBooksUrl()), OptionalInt.of(2));
            user.setReadBooks(readBooks);

            userService.saveUser(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
