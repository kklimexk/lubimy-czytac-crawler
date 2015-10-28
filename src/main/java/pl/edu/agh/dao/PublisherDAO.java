package pl.edu.agh.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.agh.model.Publisher;
import pl.edu.agh.util.HibernateUtil;

import java.util.List;

public class PublisherDAO {
    public List<Publisher> getAllPublishers() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("from Publisher");
            List<Publisher> publishers = q.list();
            return publishers;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public void savePublisher(Publisher publisher) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(publisher);
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
