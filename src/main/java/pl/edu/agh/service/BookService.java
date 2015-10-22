package pl.edu.agh.service;

import pl.edu.agh.dao.BookDAO;
import pl.edu.agh.model.Book;

import java.util.List;

public class BookService {

    private BookDAO bookDAO = new BookDAO();

    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public void saveBook(Book book) {
        bookDAO.saveBook(book);
    }
}
