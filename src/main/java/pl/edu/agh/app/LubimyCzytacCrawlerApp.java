package pl.edu.agh.app;

import pl.edu.agh.crawler.LubimyCzytacCrawlerService;
import pl.edu.agh.dao.UserDAO;
import pl.edu.agh.model.User;
import pl.edu.agh.service.UserService;

public class LubimyCzytacCrawlerApp {

    public static void main(String[] args) {
        LubimyCzytacCrawlerService lubimyCzytacCrawlerService = new LubimyCzytacCrawlerService();

        UserService userService = new UserService(new UserDAO());

        User user = lubimyCzytacCrawlerService.crawlUserFromUrl("http://lubimyczytac.pl/profil/2771/lukasz-kuc");

        System.out.print(user);

    }
}
