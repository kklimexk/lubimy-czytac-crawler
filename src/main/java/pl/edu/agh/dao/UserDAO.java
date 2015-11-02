package pl.edu.agh.dao;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.edu.agh.model.User;
import pl.edu.agh.service.BookService;
import pl.edu.agh.util.HibernateUtil;

import java.math.BigInteger;
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
        if (user != null && findByName(user.getName()) == null) {
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();

                user.getReadBooks().forEach(bookService::saveBook);
                user.getCurrentlyReadingBooks().forEach(bookService::saveBook);
                user.getWantToReadBooks().forEach(bookService::saveBook);

                tx.commit();

                tx = session.beginTransaction();

                Query query = session.createSQLQuery("INSERT INTO users (name, description, basicInformation, url, readBooksUrl, currentlyReadingBooksUrl, wantToReadBooksUrl, numOfReadBooks, numOfCurrentlyReadingBooks, numOfWantToReadBooks)" +
                        " VALUES (:name, :description, :basicInformation, :url, :readBooksUrl, :currentlyReadingBooksUrl, :wantToReadBooksUrl, :numOfReadBooks, :numOfCurrentlyReadingBooks, :numOfWantToReadBooks)");

                query.setParameter("name", user.getName());
                query.setParameter("description", user.getDescription());
                query.setParameter("basicInformation", user.getBasicInformation());
                query.setParameter("url", user.getUrl());
                query.setParameter("readBooksUrl", user.getReadBooksUrl());
                query.setParameter("currentlyReadingBooksUrl", user.getCurrentlyReadingBooksUrl());
                query.setParameter("wantToReadBooksUrl", user.getWantToReadBooksUrl());
                query.setParameter("numOfReadBooks", user.getNumOfReadBooks());
                query.setParameter("numOfCurrentlyReadingBooks", user.getNumOfCurrentlyReadingBooks());
                query.setParameter("numOfWantToReadBooks", user.getNumOfWantToReadBooks());

                query.executeUpdate();

                tx.commit();

                saveReadBooks(user);
                saveReadingBooks(user);
                saveWantToReadBooks(user);

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

    public void saveReadBooks(User user) {
        Session session = null;
        final Transaction[] tx = {null};
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            final Session finalSession = session;

            user.getReadBooks().forEach(b -> {
                tx[0] = finalSession.beginTransaction();
                Query query = finalSession.createSQLQuery("INSERT INTO read (userid, bookid)" +
                        " VALUES (:userid, :bookid)");

                query.setParameter("userid", BigInteger.valueOf(findByName(user.getName()).getId()));
                query.setParameter("bookid", BigInteger.valueOf(bookService.findByIsbn(b.getIsbn()).getId()));

                query.executeUpdate();
                tx[0].commit();
            });

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

    public void saveReadingBooks(User user) {
        Session session = null;
        final Transaction[] tx = {null};
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            final Session finalSession = session;

            user.getCurrentlyReadingBooks().forEach(b -> {
                tx[0] = finalSession.beginTransaction();
                Query query = finalSession.createSQLQuery("INSERT INTO reading (userid, bookid)" +
                        " VALUES (:userid, :bookid)");

                query.setParameter("userid", BigInteger.valueOf(findByName(user.getName()).getId()));
                query.setParameter("bookid", BigInteger.valueOf(bookService.findByIsbn(b.getIsbn()).getId()));

                query.executeUpdate();
                tx[0].commit();
            });

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

    public void saveWantToReadBooks(User user) {
        Session session = null;
        final Transaction[] tx = {null};
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            final Session finalSession = session;

            user.getWantToReadBooks().forEach(b -> {
                tx[0] = finalSession.beginTransaction();
                Query query = finalSession.createSQLQuery("INSERT INTO wanttoread (userid, bookid)" +
                        " VALUES (:userid, :bookid)");

                query.setParameter("userid", BigInteger.valueOf(findByName(user.getName()).getId()));
                query.setParameter("bookid", BigInteger.valueOf(bookService.findByIsbn(b.getIsbn()).getId()));

                query.executeUpdate();
                tx[0].commit();
            });

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

    public void saveFriends(User user) {
        Session session = null;
        final Transaction[] tx = {null};
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            final Session finalSession = session;

            user.getFriends().forEach(f -> {
                if (f != null) {
                    tx[0] = finalSession.beginTransaction();
                    Query query = finalSession.createSQLQuery("INSERT INTO friends (userid, friendid)" +
                            " VALUES (:userid, :friendid)");

                    query.setParameter("userid", BigInteger.valueOf(findByName(user.getName()).getId()));
                    query.setParameter("friendid", BigInteger.valueOf(findByName(f.getName()).getId()));

                    query.executeUpdate();
                    tx[0].commit();
                }
            });

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

    public User findById(Long id) {
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            user =  (User) session.get(User.class, id);
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

}
