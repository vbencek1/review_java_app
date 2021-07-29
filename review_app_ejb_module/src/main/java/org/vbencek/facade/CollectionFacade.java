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
import org.vbencek.model.Collection;
import org.vbencek.model.UserT;

/**
 *
 * @author Tino
 */
@Stateless
public class CollectionFacade extends AbstractFacade<Collection> implements CollectionFacadeLocal {

    @PersistenceContext(unitName = "new_review_app_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CollectionFacade() {
        super(Collection.class);
    }

    @Override
    public boolean isBookInCollection(Book book, UserT userT) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Collection> review = cq.from(Collection.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if(userT !=null){
            predicates.add(cb.equal(review.get("userT"), userT));
        }
        if(book !=null){
            predicates.add(cb.equal(review.get("book"), book));
        }
        
        //Query
        cq.select(cb.count(review)).where(predicates.toArray(new Predicate[]{}));
        long count=em.createQuery(cq).getSingleResult();
        
        return count!=0;
    }

    @Override
    public List<Book> findUserBooksByCriteria(UserT userT, String isbn, String keyword, int publishYear, String publisher, double minimumAvgRating, String sortOption, int offset, int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);
        Root<Collection> collection = cq.from(Collection.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if(userT!=null){
            predicates.add(cb.equal(collection.get("userT"), userT));
        }
        
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
        }
        
        //JOIN TABLES
        predicates.add(cb.equal(book.get("bookId"), collection.get("book").get("bookId")));
        
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
    public long countUserBooksByCriteria(UserT userT, String isbn, String keyword, int publishYear, String publisher, double minimumAvgRating) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Book> book = cq.from(Book.class);
        Root<Collection> collection = cq.from(Collection.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if(userT!=null){
            predicates.add(cb.equal(collection.get("userT"), userT));
        }
        
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
        
        //JOIN TABLES
        predicates.add(cb.equal(book.get("bookId"), collection.get("book").get("bookId")));
        
        //Query
        cq.select(cb.count(book)).where(predicates.toArray(new Predicate[]{}));
        //execute query and do something with result
        long count = em.createQuery(cq).getSingleResult();

        return count;
    }
    
}
