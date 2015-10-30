package pl.edu.agh.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "categories")
    private Set<Book> books = new HashSet<>();
    private String url;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, Set<Book> books) {
        this.name = name;
        this.books = books;
    }

    public Category(String name, Set<Book> books, String url) {
        this.name = name;
        this.books = books;
        this.url = url;
    }

    public Category(String name, String url) {
        this.name = name;
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

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
