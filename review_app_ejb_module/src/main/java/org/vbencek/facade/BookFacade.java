/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.facade;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.vbencek.model.Book;
import org.vbencek.model.Review;

/**
 *
 * @author Tino
 */
@Stateless
public class BookFacade extends AbstractFacade<Book> implements BookFacadeLocal {

    @PersistenceContext(unitName = "new_review_app_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BookFacade() {
        super(Book.class);
    }

    @Override
    public List<Book> findBooksByCriteria(String isbn, String keyword, int publishYear, String publisher, double minimumAvgRating, String sortOption, int offset, int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (isbn != null && !"".equals(isbn)) {
            predicates.add(cb.equal(book.get("isbn"), isbn));
        }
        if (keyword != null && !"".equals(keyword)) {
            Predicate title = cb.like(cb.upper(book.get("title")), "%" + keyword.toUpperCase() + "%");
            Predicate authors = cb.like(cb.upper(book.get("authors")), "%" + keyword.toUpperCase() + "%");
            predicates.add(cb.or(title, authors));
        }

        if (publishYear != 0) {
            predicates.add(cb.equal(cb.function("year", Integer.class, book.get("publicationDate")), publishYear));
        }

        if (publisher != null && !"".equals(publisher)) {
            predicates.add(cb.equal(book.get("publisher"), publisher));
        }

        if (minimumAvgRating != 0) {
            predicates.add(cb.greaterThanOrEqualTo(book.get("averageRating"), minimumAvgRating));
        }
        //add sort options
        if (sortOption != null && !"".equals(sortOption)) {
            if ("title".equals(sortOption)) {
                cq.orderBy(cb.asc(book.get("title")));
            }
            if ("rating".equals(sortOption)) {
                cq.orderBy(cb.desc(book.get("averageRating")));
            }

            if ("reviews".equals(sortOption)) {
                cq.orderBy(cb.desc(book.get("ratingsCount")));
            }
            if ("date".equals(sortOption)) {
                 Root<Review> review = cq.from(Review.class);
                 predicates.add(cb.equal(book.get("bookId"), review.get("book").get("bookId")));
                 cq.orderBy(cb.desc(review.get("ratingDate")));
            }
        }

        //Query
        cq.select(book).where(predicates.toArray(new Predicate[]{}));
        //execute query and do something with result
        List<Book> results = em.createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return results;
    }

    @Override
    public long countBooksByCriteria(String isbn, String keyword, int publishYear, String publisher, double minimumAvgRating) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Book> book = cq.from(Book.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (isbn != null && !"".equals(isbn)) {
            predicates.add(cb.equal(book.get("isbn"), isbn));
        }
        if (keyword != null && !"".equals(keyword)) {
            Predicate title = cb.like(cb.upper(book.get("title")), "%" + keyword.toUpperCase() + "%");
            Predicate authors = cb.like(cb.upper(book.get("authors")), "%" + keyword.toUpperCase() + "%");
            predicates.add(cb.or(title, authors));
        }

        if (publishYear != 0) {
            predicates.add(cb.equal(cb.function("year", Integer.class, book.get("publicationDate")), publishYear));
        }

        if (publisher != null && !"".equals(publisher)) {
            predicates.add(cb.equal(book.get("publisher"), publisher));
        }

        if (minimumAvgRating != 0) {
            predicates.add(cb.greaterThanOrEqualTo(book.get("averageRating"), minimumAvgRating));
        }

        //Query
        cq.select(cb.count(book)).where(predicates.toArray(new Predicate[]{}));
        //execute query and do something with result
        long count = em.createQuery(cq).getSingleResult();

        return count;
    }

    @Override
    public List<String> findAllYears() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Book> book = cq.from(Book.class);

        cq.orderBy(cb.asc(cb.function("year", Integer.class, book.get("publicationDate"))));
        cq.select(cb.function("year", String.class, book.get("publicationDate"))).distinct(true);

        List<String> yearList = em.createQuery(cq).getResultList();

        return yearList;
    }

    @Override
    public List<String> findAllPublishers() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Book> book = cq.from(Book.class);

        cq.orderBy(cb.asc(book.get("publisher")));
        cq.select(book.get("publisher")).distinct(true);

        List<String> publisherList = em.createQuery(cq).getResultList();

        return publisherList;
    }

    @Override
    public boolean isISBNExists(String isbn) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Book> book = cq.from(Book.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (isbn != null && !"".equals(isbn)) {
            predicates.add(cb.equal(book.get("isbn"), isbn));
        }
        cq.select(cb.count(book)).where(predicates.toArray(new Predicate[]{}));
        //execute query and do something with result
        long count = em.createQuery(cq).getSingleResult();

        return count!=0;
    }

    @Override
    public List<Book> findBooksWithReviews() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);
        Root<Review> review = cq.from(Review.class);
        List<Predicate> predicates = new ArrayList<Predicate>();  
        
        //JOIN TABLES
        predicates.add(cb.equal(book.get("bookId"), review.get("book").get("bookId")));
        //Query
        cq.select(book).where(predicates.toArray(new Predicate[]{})).distinct(true);
        //execute query and do something with result
        List<Book> results = em.createQuery(cq)
                .getResultList();

        return results;
    }

    @Override
    public Book findBookById(int id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);
        List<Predicate> predicates = new ArrayList<Predicate>();  
        predicates.add(cb.equal(book.get("bookId"), id));
        //Query
        cq.select(book).where(predicates.toArray(new Predicate[]{}));
        //execute query and do something with result
        Book result = em.createQuery(cq)
                .getSingleResult();
        return result;
    }

    @Override
    public List<Book> findRecommendedBooks(String keyword, double minimumAvgRating, int minimumRatingsCount, String sortOption,int offset,int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (keyword != null && !"".equals(keyword)) {
            Predicate title = cb.like(cb.upper(book.get("title")), "%" + keyword.toUpperCase() + "%");
            Predicate authors = cb.like(cb.upper(book.get("authors")), "%" + keyword.toUpperCase() + "%");
            predicates.add(cb.or(title, authors));
        }

        if (minimumAvgRating != 0) {
            predicates.add(cb.greaterThanOrEqualTo(book.get("averageRating"), minimumAvgRating));
        }
        
        if (minimumRatingsCount != 0) {
            predicates.add(cb.greaterThanOrEqualTo(book.get("ratingsCount"), minimumRatingsCount));
        }
        //add sort options
        if (sortOption != null && !"".equals(sortOption)) {
            if ("title".equals(sortOption)) {
                cq.orderBy(cb.asc(book.get("title")));
            }
            if ("rating".equals(sortOption)) {
                cq.orderBy(cb.desc(book.get("averageRating")));
            }

            if ("reviews".equals(sortOption)) {
                cq.orderBy(cb.desc(book.get("ratingsCount")));
            }
            if ("date".equals(sortOption)) {
                 Root<Review> review = cq.from(Review.class);
                 predicates.add(cb.equal(book.get("bookId"), review.get("book").get("bookId")));
                 cq.orderBy(cb.desc(review.get("ratingDate")));
            }
        }

        //Query
        cq.select(book).where(predicates.toArray(new Predicate[]{}));
        //execute query and do something with result
        List<Book> results = em.createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return results;
    }

}
