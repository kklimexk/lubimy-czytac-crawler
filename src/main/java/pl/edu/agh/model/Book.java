package pl.edu.agh.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "books", uniqueConstraints = @UniqueConstraint(columnNames = {"isbn"}))
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    //TODO: author
    private Double ratingValue;
    private Integer ratingVotes;
    private Integer ratingReviews;
    private Date datePublished;
    private String isbn;
    private Integer numOfPages;
    //TODO: category
    private String language;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String url;

    public Book() {
    }

    public Book(String name, Double ratingValue, Integer ratingVotes, Integer ratingReviews, Date datePublished, String isbn, Integer numOfPages, String language, String description, String url) {
        this.name = name;
        this.ratingValue = ratingValue;
        this.ratingVotes = ratingVotes;
        this.ratingReviews = ratingReviews;
        this.datePublished = datePublished;
        this.isbn = isbn;
        this.numOfPages = numOfPages;
        this.language = language;
        this.description = description;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Integer getRatingVotes() {
        return ratingVotes;
    }

    public void setRatingVotes(Integer ratingVotes) {
        this.ratingVotes = ratingVotes;
    }

    public Integer getRatingReviews() {
        return ratingReviews;
    }

    public void setRatingReviews(Integer ratingReviews) {
        this.ratingReviews = ratingReviews;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(Integer numOfPages) {
        this.numOfPages = numOfPages;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ratingValue=" + ratingValue +
                ", ratingVotes=" + ratingVotes +
                ", ratingReviews=" + ratingReviews +
                ", datePublished=" + datePublished +
                ", isbn='" + isbn + '\'' +
                ", numOfPages=" + numOfPages +
                ", language='" + language + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
