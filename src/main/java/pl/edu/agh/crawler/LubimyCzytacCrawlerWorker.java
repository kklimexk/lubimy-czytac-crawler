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
            //Crawlowanie dla jednego usera jego ksiazki
            String userUrl = "http://lubimyczytac.pl/profil/802/joanna-kalio-golaszewska";
            Document userPage = PageDownloader.getPage(userUrl);

            User user = crawlerService.crawlUserFromUrl(userPage);
            Set<Book> readBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(user.getReadBooksUrl()), OptionalInt.of(2));
            Set<Book> currentlyReadingBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(user.getCurrentlyReadingBooksUrl()), OptionalInt.of(2));
            Set<Book> wantToReadBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(user.getWantToReadBooksUrl()), OptionalInt.of(2));

            user.setReadBooks(readBooks);
            user.setCurrentlyReadingBooks(currentlyReadingBooks);
            user.setWantToReadBooks(wantToReadBooks);
            userService.saveUser(user);

            //Crawlowanie znajomych powyzszego uzytkownika wraz z ich ksiazkami
            Set<User> friends = crawlerService.crawlUserFriendsFromUrl(PageDownloader.getPage(userUrl + "/znajomi"), OptionalInt.of(2));
            friends.forEach(friend -> {
                try {
                    Set<Book> friendReadBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(friend.getReadBooksUrl()), OptionalInt.of(2));
                    Set<Book> friendCurrentlyReadingBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(friend.getCurrentlyReadingBooksUrl()), OptionalInt.of(2));
                    Set<Book> friendWantToReadBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(friend.getWantToReadBooksUrl()), OptionalInt.of(2));
                    friend.setReadBooks(friendReadBooks);
                    friend.setCurrentlyReadingBooks(friendCurrentlyReadingBooks);
                    friend.setWantToReadBooks(friendWantToReadBooks);
                    userService.saveUser(friend);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            user.setFriends(friends);
            userService.saveUserFriends(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
