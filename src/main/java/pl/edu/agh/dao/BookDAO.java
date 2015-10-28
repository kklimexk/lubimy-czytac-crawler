package pl.edu.agh.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.edu.agh.model.Book;
import pl.edu.agh.model.Publisher;
import pl.edu.agh.service.BookService;
import pl.edu.agh.service.PublisherService;
import pl.edu.agh.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class BookDAO {
    public List<Book> getAllBooks() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("from Book");
            List<Book> books = q.list();
            return books;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public void saveBook(Book book) {
        Session session = null;
        Transaction tx = null;
        try {
        	
        	Optional<Book> inBase = null;
            List<Book> books = new BookService().getAllBooks();
            if(books != null) {
            	inBase = books.stream().filter(b -> b.getIsbn().equals(book.getIsbn())).findAny();
            	if(inBase.isPresent()) {
                	return;
                }
            }
        	
        	
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(book);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
