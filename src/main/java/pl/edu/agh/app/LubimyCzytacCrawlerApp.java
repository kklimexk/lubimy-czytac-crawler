package pl.edu.agh.app;

import pl.edu.agh.crawler.ICrawlerService;
import pl.edu.agh.crawler.LubimyCzytacCrawlerService;
import pl.edu.agh.http.PageDownloader;
import pl.edu.agh.model.Book;
import pl.edu.agh.model.User;
import pl.edu.agh.service.AuthorService;
import pl.edu.agh.service.BookService;
import pl.edu.agh.service.CategoryService;
import pl.edu.agh.service.UserService;

import java.io.IOException;

public class LubimyCzytacCrawlerApp {

    public static void main(String[] args) {
        ICrawlerService lubimyCzytacCrawlerService = new LubimyCzytacCrawlerService();

        UserService userService = new UserService();
        BookService bookService = new BookService();
        AuthorService authorService = new AuthorService();
        CategoryService categoryService = new CategoryService();

        try {
            User user1 = lubimyCzytacCrawlerService.crawlUserFromUrl(PageDownloader.getPage("http://lubimyczytac.pl/profil/2771/lukasz-kuc"));
            User user2 = lubimyCzytacCrawlerService.crawlUserFromUrl(PageDownloader.getPage("http://lubimyczytac.pl/profil/12447/mamalgosia"));
            User user3 = lubimyCzytacCrawlerService.crawlUserFromUrl(PageDownloader.getPage("http://lubimyczytac.pl/profil/1803/shczooreczek"));
            User user4 = lubimyCzytacCrawlerService.crawlUserFromUrl(PageDownloader.getPage("http://lubimyczytac.pl/profil/802/joanna-kalio-golaszewska"));

            System.out.println(user1);
            System.out.println(user2);
            System.out.println(user3);
            System.out.println(user4);

            Book book1 = lubimyCzytacCrawlerService.crawlBookFromUrl(PageDownloader.getPage("http://lubimyczytac.pl/ksiazka/21938/tezy-o-glupocie-piciu-i-umieraniu"));
            Book book2 = lubimyCzytacCrawlerService.crawlBookFromUrl(PageDownloader.getPage("http://lubimyczytac.pl/ksiazka/210112/traktat-o-luskaniu-fasoli"));
            Book book3 = lubimyCzytacCrawlerService.crawlBookFromUrl(PageDownloader.getPage("http://lubimyczytac.pl/ksiazka/47775/kwiat-pustyni-z-namiotu-nomadow-do-nowego-jorku"));

            System.out.println(book1);
            System.out.println(book2);
            System.out.println(book3);

            //bookService.saveBook(book3);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
