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
        if (publisher != null && findByName(publisher.getName()) == null) {
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();
                session.saveOrUpdate(publisher);
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

    public Publisher findByName(String name) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("FROM Publisher WHERE name = :name");
            q.setParameter("name", name);
            Publisher publisher = (Publisher) q.uniqueResult();
            return publisher;
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
