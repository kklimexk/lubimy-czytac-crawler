package pl.edu.agh.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.agh.model.Category;
import pl.edu.agh.util.HibernateUtil;

import java.util.List;

public class CategoryDAO {

    public List<Category> getAllCategories() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("from Category");
            List<Category> categories = q.list();
            return categories;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public void saveCategory(Category category) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(category);
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
