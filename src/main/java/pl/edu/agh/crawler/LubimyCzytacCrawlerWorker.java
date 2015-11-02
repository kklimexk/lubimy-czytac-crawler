package pl.edu.agh.crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
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
    private final OptionalInt numberOfPagesToRead = OptionalInt.empty();

    public LubimyCzytacCrawlerWorker(ICrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @Override
    public void run() {
        try {
            String userUrl = "http://lubimyczytac.pl/profil/802/joanna-kalio-golaszewska";

            User user = userService.findByUrl(userUrl);
            if (user == null) {
                Document userPage = PageDownloader.getPage(userUrl);
            	user = crawlerService.crawlUserFromUrl(userPage);            	
            }
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
		// if at least half of the books are loaded, don't load more
		// as it would require to read the whole pages once again
		// and this is so very time consuming
		if (user.getReadBooks().size() < user.getNumOfReadBooks() / 2) {
			Set<Book> readBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(user.getReadBooksUrl()), numberOfPagesToRead);
			user.setReadBooks(readBooks);
		}
		if (user.getCurrentlyReadingBooks().size() < user.getNumOfCurrentlyReadingBooks() / 2) {
			Set<Book> currentlyReadingBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(user.getCurrentlyReadingBooksUrl()), numberOfPagesToRead);
			user.setCurrentlyReadingBooks(currentlyReadingBooks);
		}
		if (user.getWantToReadBooks().size() < user.getNumOfWantToReadBooks() / 2) {
			Set<Book> wantToReadBooks = crawlerService.crawlUserBooksFromUrl(PageDownloader.getPage(user.getWantToReadBooksUrl()), numberOfPagesToRead);
			user.setWantToReadBooks(wantToReadBooks);			
		}
        userService.saveUser(user);
	}
	
	private void saveUserFriends(User user, String userUrl) throws IOException {
		Set<User> friends = crawlerService.crawlUserFriendsFromUrl(PageDownloader.getPage(userUrl + "/znajomi"), numberOfPagesToRead);
        friends.forEach(friend -> userService.saveUser(friend));
        usersToCrawl.addAll(friends.stream().filter(friend -> !fullyReadUsers.contains(friend)).collect(Collectors.toList()));
        user.setFriends(friends);
        userService.saveUserFriends(user);
        fullyReadUsers.add(user);
	}
}
