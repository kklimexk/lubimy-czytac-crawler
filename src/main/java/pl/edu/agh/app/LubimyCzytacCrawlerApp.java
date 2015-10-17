package pl.edu.agh.app;

import pl.edu.agh.crawler.LubimyCzytacCrawlerService;
import pl.edu.agh.dao.BookDAO;
import pl.edu.agh.dao.UserDAO;
import pl.edu.agh.model.Book;
import pl.edu.agh.model.User;
import pl.edu.agh.service.BookService;
import pl.edu.agh.service.UserService;

public class LubimyCzytacCrawlerApp {

    public static void main(String[] args) {
        LubimyCzytacCrawlerService lubimyCzytacCrawlerService = new LubimyCzytacCrawlerService();

        //UserService userService = new UserService(new UserDAO());
        //BookService bookService = new BookService(new BookDAO());

        User user1 = lubimyCzytacCrawlerService.crawlUserFromUrl("http://lubimyczytac.pl/profil/2771/lukasz-kuc");
        User user2 = lubimyCzytacCrawlerService.crawlUserFromUrl("http://lubimyczytac.pl/profil/12447/mamalgosia");
        User user3 = lubimyCzytacCrawlerService.crawlUserFromUrl("http://lubimyczytac.pl/profil/1803/shczooreczek");
        User user4 = lubimyCzytacCrawlerService.crawlUserFromUrl("http://lubimyczytac.pl/profil/802/joanna-kalio-golaszewska");

        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);
        System.out.println(user4);

        Book book1 = lubimyCzytacCrawlerService.crawlBookFromUrl("http://lubimyczytac.pl/ksiazka/21938/tezy-o-glupocie-piciu-i-umieraniu");
        Book book2 = lubimyCzytacCrawlerService.crawlBookFromUrl("http://lubimyczytac.pl/ksiazka/210112/traktat-o-luskaniu-fasoli");

        System.out.println(book1);
        System.out.println(book2);

    }
}
