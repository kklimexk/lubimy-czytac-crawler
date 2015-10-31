package pl.edu.agh.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.agh.model.Author;
import pl.edu.agh.model.Book;
import pl.edu.agh.util.HibernateUtil;

import java.util.List;

public class AuthorDAO {
    public List<Author> getAllAuthors() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("from Author");
            List<Author> authors = q.list();
            return authors;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public void saveAuthor(Author author) {
        Session session = null;
        Transaction tx = null;
        if (author != null && findByName(author.getName()) == null) {
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();
                session.saveOrUpdate(author);
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

    public Author findByName(String name) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("FROM Author WHERE name = :name");
            q.setParameter("name", name);
            Author author = (Author) q.uniqueResult();
            return author;
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
