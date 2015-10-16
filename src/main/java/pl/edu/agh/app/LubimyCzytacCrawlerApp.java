package pl.edu.agh.app;

import pl.edu.agh.crawler.LubimyCzytacCrawlerService;
import pl.edu.agh.dao.UserDAO;
import pl.edu.agh.model.User;
import pl.edu.agh.service.UserService;

public class LubimyCzytacCrawlerApp {

    public static void main(String[] args) {
        LubimyCzytacCrawlerService lubimyCzytacCrawlerService = new LubimyCzytacCrawlerService();

        UserService userService = new UserService(new UserDAO());

        User user1 = lubimyCzytacCrawlerService.crawlUserFromUrl("http://lubimyczytac.pl/profil/2771/lukasz-kuc");
        User user2 = lubimyCzytacCrawlerService.crawlUserFromUrl("http://lubimyczytac.pl/profil/12447/mamalgosia");
        User user3 = lubimyCzytacCrawlerService.crawlUserFromUrl("http://lubimyczytac.pl/profil/1803/shczooreczek");
        User user4 = lubimyCzytacCrawlerService.crawlUserFromUrl("http://lubimyczytac.pl/profil/802/joanna-kalio-golaszewska");

        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);
        System.out.println(user4);

        /*userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);
        userService.saveUser(user4);*/

    }
}
