package pl.edu.agh.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.agh.model.Book;
import pl.edu.agh.util.HibernateUtil;

import java.util.List;

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
        if (findByIsbn(book.getIsbn()) == null) {
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();
                session.saveOrUpdate(book);
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

    public Book findByIsbn(String isbn) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("FROM Book WHERE isbn = :isbn");
            q.setParameter("isbn", isbn);
            Book book = (Book) q.uniqueResult();
            return book;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }
}
