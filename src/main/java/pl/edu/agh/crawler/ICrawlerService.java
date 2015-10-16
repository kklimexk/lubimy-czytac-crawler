package pl.edu.agh.crawler;

import pl.edu.agh.model.User;

public interface ICrawlerService {
    int TIMEOUT = 0;
    User crawlUserFromUrl(String userProfileUrl);
}
