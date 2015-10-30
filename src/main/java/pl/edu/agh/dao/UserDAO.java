package pl.edu.agh.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.agh.model.User;
import pl.edu.agh.service.BookService;
import pl.edu.agh.util.HibernateUtil;

import java.util.List;

public class UserDAO {

    private BookService bookService = new BookService();

    public List<User> getAllUsers() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("from User");
            List<User> users = q.list();
            return users;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public void saveUser(User user) {
        Session session = null;
        Transaction tx = null;
        if (findByName(user.getName()) == null) {
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();

                user.getReadBooks().forEach(bookService::saveBook);
                /*user.getCurrentlyReadingBooks().forEach(bookService::saveBook);
                user.getWantToReadBooks().forEach(bookService::saveBook);*/

                //session.saveOrUpdate(user);
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

    public User findByName(String name) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("FROM User WHERE name = :name");
            q.setParameter("name", name);
            User user = (User) q.uniqueResult();
            return user;
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
