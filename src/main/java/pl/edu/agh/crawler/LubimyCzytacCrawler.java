package pl.edu.agh.crawler;

public class LubimyCzytacCrawler {

    public void crawl() {
        ICrawlerService crawlerService = new LubimyCzytacCrawlerService();
        LubimyCzytacCrawlerWorker lubimyCzytacCrawlerWorker = new LubimyCzytacCrawlerWorker(crawlerService);
        lubimyCzytacCrawlerWorker.run();
    }
}
