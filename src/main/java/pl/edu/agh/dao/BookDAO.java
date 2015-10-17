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
        try {
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
