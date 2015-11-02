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
    private LinkedList<User> usersToCrawl = new LinkedList<User>();
    private LinkedList<User> fullyReadUsers = new LinkedList<User>();

    public LubimyCzytacCrawlerWorker(ICrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @Override
    public void run() {
        try {
            String userUrl = "http://lubimyczytac.pl/profil/802/joanna-kalio-golaszewska";
            Document userPage = PageDownloader.getPage(userUrl);

            User user = crawlerService.crawlUserFromUrl(userPage);
            saveUserWithBooks(user);
            saveUserFriends(user, userUrl);
	            
            while (!usersToCrawl.isEmpty()) {
            	user = getNextUser();
            	if (fullyReadUsers.contains(user)) {
            		break;
            	}
            	saveUserWithBooks(user);
                saveUserFriends(user, user.getUrl());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private User getNextUser() {
		User user;
		user = usersToCrawl.pollFirst();
		while (fullyReadUsers.contains(user) && !usersToCrawl.isEmpty()) {
			user = usersToCrawl.pollFirst();
		}
		return user;
	}
	
	private void saveUserWithBooks(User user) throws IOException {
		Set<Book> readBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(user.getReadBooksUrl()), OptionalInt.of(1));
        Set<Book> currentlyReadingBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(user.getCurrentlyReadingBooksUrl()), OptionalInt.of(1));
        Set<Book> wantToReadBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(user.getWantToReadBooksUrl()), OptionalInt.of(1));
        user.setReadBooks(readBooks);
        user.setCurrentlyReadingBooks(currentlyReadingBooks);
        user.setWantToReadBooks(wantToReadBooks);
        userService.saveUser(user);
	}
	
	private void saveUserFriends(User user, String userUrl) throws IOException {
		Set<User> friends = crawlerService.crawlUserFriendsFromUrl(PageDownloader.getPage(userUrl + "/znajomi"), OptionalInt.of(1));
        friends.forEach(friend -> userService.saveUser(friend));
        usersToCrawl.addAll(friends.stream().filter(friend -> !fullyReadUsers.contains(friend)).collect(Collectors.toList()));
        user.setFriends(friends);
        userService.saveUserFriends(user);
        fullyReadUsers.add(user);
	}
}
