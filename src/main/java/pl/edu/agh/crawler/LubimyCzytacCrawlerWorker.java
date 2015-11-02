package pl.edu.agh.crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;

import pl.edu.agh.http.PageDownloader;
import pl.edu.agh.model.Book;
import pl.edu.agh.model.User;
import pl.edu.agh.service.AuthorService;
import pl.edu.agh.service.BookService;
import pl.edu.agh.service.CategoryService;
import pl.edu.agh.service.PublisherService;
import pl.edu.agh.service.UserService;

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
        	List<User> fullyReadUsers = new LinkedList<User>();
        	
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
            friends.forEach(friend -> userService.saveUser(friend));
            LinkedList<User> usersToCrawl = new LinkedList<User>(friends);
            
            user.setFriends(friends);
            userService.saveUserFriends(user);
            fullyReadUsers.add(user);
	            
            while(!usersToCrawl.isEmpty()) {
            	user = usersToCrawl.pollFirst();
            	
            	readBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(user.getReadBooksUrl()), OptionalInt.of(2));
                currentlyReadingBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(user.getCurrentlyReadingBooksUrl()), OptionalInt.of(2));
                wantToReadBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(user.getWantToReadBooksUrl()), OptionalInt.of(2));

                user.setReadBooks(readBooks);
                user.setCurrentlyReadingBooks(currentlyReadingBooks);
                user.setWantToReadBooks(wantToReadBooks);
                userService.saveUser(user);
            	
            	friends = crawlerService.crawlUserFriendsFromUrl(PageDownloader.getPage(userUrl + "/znajomi"), OptionalInt.of(2));
            	friends.forEach(friend -> userService.saveUser(friend));
            	usersToCrawl.addAll(friends.stream().filter(friend -> !fullyReadUsers.contains(friend)).collect(Collectors.toList()));
            	user.setFriends(friends);
	            userService.saveUserFriends(user);
	            fullyReadUsers.add(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
