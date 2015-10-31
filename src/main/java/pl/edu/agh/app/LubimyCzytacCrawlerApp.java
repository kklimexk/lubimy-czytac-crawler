package pl.edu.agh.app;

import pl.edu.agh.crawler.ICrawlerService;
import pl.edu.agh.crawler.LubimyCzytacCrawler;
import pl.edu.agh.crawler.LubimyCzytacCrawlerService;
import pl.edu.agh.http.PageDownloader;
import pl.edu.agh.model.*;
import pl.edu.agh.service.*;

import java.io.IOException;
import java.util.Set;

public class LubimyCzytacCrawlerApp {

    /*public static void main(String[] args) {
        ICrawlerService lubimyCzytacCrawlerService = new LubimyCzytacCrawlerService();

        UserService userService = new UserService();
        BookService bookService = new BookService();
        AuthorService authorService = new AuthorService();
        CategoryService categoryService = new CategoryService();
        PublisherService publisherService = new PublisherService();

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

            Set<Book> books = lubimyCzytacCrawlerService.crawlUserBooksFromUrl(PageDownloader.getPage("http://lubimyczytac.pl/profil/802/joanna-kalio-golaszewska/polka/2759/przeczytane/miniatury/1"));
            System.out.println(books);

            Publisher publisher1 = new Publisher("p1", "p1");
            Publisher publisher2 = new Publisher("p1", "p11");
            publisherService.savePublisher(publisher1);
            publisherService.savePublisher(publisher2);

            Author author1 = new Author("a1", "a1");
            Author author2 = new Author("a1", "a11");
            authorService.saveAuthor(author1);
            authorService.saveAuthor(author2);

            Category category1 = new Category("c1", "c1");
            Category category2 = new Category("c1", "c11");
            categoryService.saveCategory(category1);
            categoryService.saveCategory(category2);

            user4.setReadBooks(books);
            userService.saveUser(user4);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void main(String[] args) {
        LubimyCzytacCrawler lubimyCzytacCrawler = new LubimyCzytacCrawler();
        lubimyCzytacCrawler.crawl();
    }
}
