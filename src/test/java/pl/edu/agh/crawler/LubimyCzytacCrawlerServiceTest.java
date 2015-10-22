package pl.edu.agh.crawler;

import org.hamcrest.CoreMatchers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.model.Book;
import pl.edu.agh.model.User;

import java.io.IOException;

public class LubimyCzytacCrawlerServiceTest {

    Document bookDoc;
    Document userDoc;
    ICrawlerService crawlerService = new LubimyCzytacCrawlerService();

    @Before
    public void setup() throws IOException {
        bookDoc = Jsoup.parse(this.getClass().getResourceAsStream("/bookDoc.html"), null, "http://example.com/");
        userDoc = Jsoup.parse(this.getClass().getResourceAsStream("/userDoc.html"), null, "http://example.com/");
    }

    @Test
    public void testParse() {
        Assert.assertNotNull(bookDoc);
        Assert.assertNotNull(userDoc);
    }

    @Test
    public void testCrawlUser() {
        User user = crawlerService.crawlUserFromUrl(userDoc);
        Assert.assertThat(user.getName(), CoreMatchers.containsString("shczooreczek"));
        Assert.assertThat(user.getDescription(), CoreMatchers.containsString("shczooreczek.blogspot.com"));
        Assert.assertThat(user.getBasicInformation(), CoreMatchers.containsString("status: Czytelnik, ostatnio widziany 2 dni temu"));
    }

    @Test
    public void testCrawlBook() {
        Book book = crawlerService.crawlBookFromUrl(bookDoc);
        Assert.assertThat(book.getName(), CoreMatchers.containsString("Kwiat pustyni. Z namiotu Nomadów do Nowego Jorku"));
        Assert.assertEquals(book.getAuthors().size(), 2);
        Assert.assertThat(book.getRatingValue().doubleValue(), CoreMatchers.equalTo(7.34));
        Assert.assertThat(book.getRatingVotes(), CoreMatchers.equalTo(9703));
        Assert.assertThat(book.getRatingReviews(), CoreMatchers.equalTo(731));
        Assert.assertThat(book.getIsbn(), CoreMatchers.containsString("8372272808"));
        Assert.assertThat(book.getNumOfPages(), CoreMatchers.equalTo(254));
        Assert.assertEquals(book.getCategories().size(), 1);
        Assert.assertThat(book.getLanguage(), CoreMatchers.containsString("polski"));
    }

}
