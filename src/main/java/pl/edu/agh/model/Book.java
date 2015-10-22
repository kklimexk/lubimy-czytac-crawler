package pl.edu.agh.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books", uniqueConstraints = @UniqueConstraint(columnNames = {"isbn"}))
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "books_authors",
            joinColumns = {@JoinColumn(name = "bookId")},
            inverseJoinColumns = {@JoinColumn(name = "authorId")})
    private Set<Author> authors = new HashSet<>();
    private Double ratingValue;
    private Integer ratingVotes;
    private Integer ratingReviews;
    private Date datePublished;
    private String isbn;
    private Integer numOfPages;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "books_categories",
            joinColumns = {@JoinColumn(name = "bookId")},
            inverseJoinColumns = {@JoinColumn(name = "categoryId")})
    private Set<Category> categories = new HashSet<>();
    private String language;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String url;

    public Book() {
    }

    public Book(String name, Set<Author> authors, Double ratingValue, Integer ratingVotes, Integer ratingReviews, Date datePublished, String isbn, Integer numOfPages, Set<Category> categories, String language, String description, String url) {
        this.name = name;
        this.authors = authors;
        this.ratingValue = ratingValue;
        this.ratingVotes = ratingVotes;
        this.ratingReviews = ratingReviews;
        this.datePublished = datePublished;
        this.isbn = isbn;
        this.numOfPages = numOfPages;
        this.categories = categories;
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

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
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
                ", authors=" + authors +
                ", ratingValue=" + ratingValue +
                ", ratingVotes=" + ratingVotes +
                ", ratingReviews=" + ratingReviews +
                ", datePublished=" + datePublished +
                ", isbn='" + isbn + '\'' +
                ", numOfPages=" + numOfPages +
                ", categories=" + categories +
                ", language='" + language + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
