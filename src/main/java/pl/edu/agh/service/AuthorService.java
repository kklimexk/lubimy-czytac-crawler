package pl.edu.agh.service;

import pl.edu.agh.dao.AuthorDAO;
import pl.edu.agh.model.Author;

import java.util.List;

public class AuthorService {
    private AuthorDAO authorDAO = new AuthorDAO();

    public List<Author> getAllAuthors() {
        return authorDAO.getAllAuthors();
    }

    public void saveAuthor(Author author) {
        authorDAO.saveAuthor(author);
    }

    public Author findByName(String name) {
        return authorDAO.findByName(name);
    }
}
