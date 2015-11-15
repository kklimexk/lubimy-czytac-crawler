package pl.edu.agh.schema;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.agh.util.HibernateUtil;

public class Schema {
    public void addWhenReadColumnToReadTable() {
        Session session = null;
        final Transaction[] tx = {null};
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            final Session finalSession = session;

            try {
                tx[0] = finalSession.beginTransaction();
                Query query = finalSession.createSQLQuery("ALTER TABLE read ADD COLUMN whenRead timestamp");

                query.executeUpdate();
                tx[0].commit();
            } catch (Exception e) {
                if (tx[0] != null) {
                    tx[0].rollback();
                }
                e.printStackTrace();
            }

        } catch (Exception e) {
            if (tx[0] != null) {
                tx[0].rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
