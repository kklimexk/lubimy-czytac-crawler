package pl.edu.agh.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String basicInformation;
    private String url;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "read",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "bookId")})
    private Set<Book> readBooks = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "reading",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "bookId")})
    private Set<Book> currentlyReadingBooks = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "wantToRead",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "bookId")})
    private Set<Book> wantToReadBooks = new HashSet<>();

    public User() {
    }

    public User(String name, String description, String basicInformation, String url) {
        this.name = name;
        this.description = description;
        this.basicInformation = basicInformation;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBasicInformation() {
        return basicInformation;
    }

    public void setBasicInformation(String basicInformation) {
        this.basicInformation = basicInformation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Book> getReadBooks() {
        return readBooks;
    }

    public void setReadBooks(Set<Book> readBooks) {
        this.readBooks = readBooks;
    }

    public Set<Book> getCurrentlyReadingBooks() {
        return currentlyReadingBooks;
    }

    public void setCurrentlyReadingBooks(Set<Book> currentlyReadingBooks) {
        this.currentlyReadingBooks = currentlyReadingBooks;
    }

    public Set<Book> getWantToReadBooks() {
        return wantToReadBooks;
    }

    public void setWantToReadBooks(Set<Book> wantToReadBooks) {
        this.wantToReadBooks = wantToReadBooks;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", basicInformation='" + basicInformation + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

}
