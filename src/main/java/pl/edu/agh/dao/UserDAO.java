package pl.edu.agh.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.edu.agh.model.Book;
import pl.edu.agh.model.User;
import pl.edu.agh.service.BookService;
import pl.edu.agh.service.UserService;
import pl.edu.agh.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class UserDAO {

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
        try {
        	
        	Optional<User> inBase = null;
            List<User> users = new UserService().getAllUsers();
            if(users != null) {
            	inBase = users.stream().filter(u -> u.getUrl().equals(user.getUrl())).findAny();
            	if(inBase.isPresent()) {
                	return;
                }
            }
        	
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
        } catch (Exception e) {
//            if (tx != null) {
//                tx.rollback();
//            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
