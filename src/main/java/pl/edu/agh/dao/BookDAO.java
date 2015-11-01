package pl.edu.agh.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.agh.model.Book;
import pl.edu.agh.service.AuthorService;
import pl.edu.agh.service.CategoryService;
import pl.edu.agh.service.PublisherService;
import pl.edu.agh.util.HibernateUtil;

import java.math.BigInteger;
import java.util.List;

public class BookDAO {

    private AuthorService authorService = new AuthorService();
    private CategoryService categoryService = new CategoryService();
    private PublisherService publisherService = new PublisherService();

    public List<Book> getAllBooks() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("from Book");
            List<Book> books = q.list();
            return books;
        } catch (Exception e) {
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

                book.getAuthors().forEach(authorService::saveAuthor);
                book.getCategories().forEach(categoryService::saveCategory);
                publisherService.savePublisher(book.getPublisher());

                tx.commit();

                tx = session.beginTransaction();
                Query query = null;
                if (book.getPublisher() != null) {
                    query = session.createSQLQuery("INSERT INTO books (datepublished, description, isbn, language, name, numofpages, ratingreviews, ratingvalue, ratingvotes, url, publisherid, numOfComments)" +
                            " VALUES (:datepublished, :description, :isbn, :language, :name, :numofpages, :ratingreviews, :ratingvalue, :ratingvotes, :url, :publisherid, :numOfComments)");

                    query.setParameter("publisherid", BigInteger.valueOf(publisherService.findByName(book.getPublisher().getName()).getId()));

                } else {
                    query = session.createSQLQuery("INSERT INTO books (datepublished, description, isbn, language, name, numofpages, ratingreviews, ratingvalue, ratingvotes, url, numOfComments)" +
                            " VALUES (:datepublished, :description, :isbn, :language, :name, :numofpages, :ratingreviews, :ratingvalue, :ratingvotes, :url, :numOfComments)");
                }

                query.setParameter("datepublished", book.getDatePublished());
                query.setParameter("description", book.getDescription());
                query.setParameter("isbn", book.getIsbn());
                query.setParameter("language", book.getLanguage());
                query.setParameter("name", book.getName());
                if (book.getNumOfPages() == null) query.setParameter("numofpages", -1);
                else query.setParameter("numofpages", book.getNumOfPages());
                query.setParameter("ratingreviews", book.getRatingReviews());
                query.setParameter("ratingvalue", book.getRatingValue());
                query.setParameter("ratingvotes", book.getRatingVotes());
                query.setParameter("url", book.getUrl());
                query.setParameter("numOfComments", book.getNumOfComments());

                query.executeUpdate();

                tx.commit();

                saveWrittenBy(book);
                saveBelongsToCategory(book);

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

    public void saveWrittenBy(Book book) {
        Session session = null;
        final Transaction[] tx = {null};
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            final Session finalSession = session;

            book.getAuthors().forEach(a -> {
                tx[0] = finalSession.beginTransaction();
                Query query = finalSession.createSQLQuery("INSERT INTO writtenby (bookid, authorid)" +
                        " VALUES (:bookid, :authorid)");

                query.setParameter("bookid", BigInteger.valueOf(findByIsbn(book.getIsbn()).getId()));
                query.setParameter("authorid", BigInteger.valueOf(authorService.findByName(a.getName()).getId()));

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

    public void saveBelongsToCategory(Book book) {
        Session session = null;
        final Transaction[] tx = {null};
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            final Session finalSession = session;

            book.getCategories().forEach(c -> {
                tx[0] = finalSession.beginTransaction();
                Query query = finalSession.createSQLQuery("INSERT INTO belongstocategory (bookid, categoryid)" +
                        " VALUES (:bookid, :categoryid)");

                query.setParameter("bookid", BigInteger.valueOf(findByIsbn(book.getIsbn()).getId()));
                query.setParameter("categoryid", BigInteger.valueOf(categoryService.findByName(c.getName()).getId()));

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

    public Book findByIsbn(String isbn) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("FROM Book WHERE isbn = :isbn");
            q.setParameter("isbn", isbn);
            Book book = (Book) q.uniqueResult();
            return book;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }
}
