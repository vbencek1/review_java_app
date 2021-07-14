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
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.vbencek.model.Book;

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
    public List<Book> findBooksByCriteria(String isbn, String keyword, int publishYear, String publisher, double minimumAvgRating, String sortOption, int offset,int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if(isbn!=null && !"".equals(isbn)){
            predicates.add(cb.equal(book.get("isbn"), isbn));
        }
        if(keyword !=null && !"".equals(keyword)){
            Predicate title=cb.like(book.get("title"), keyword);
            Predicate authors=cb.like(book.get("authors"), keyword);
            predicates.add(cb.or(title,authors));
        } 
        //TO DO -> extract godine
        //if(publishYear!=0){
        //    predicates.add(cb.equal(book.get("publicationDate"), publishYear));
        //}
        if(publisher!=null && !"".equals(publisher)){
            predicates.add(cb.equal(book.get("publisher"), publisher));
        }
        if(minimumAvgRating!=0){
            predicates.add(cb.greaterThanOrEqualTo(book.get("averageRating"), minimumAvgRating));
        }
        //add sort options
        if(sortOption!=null && !"".equals(sortOption)){
            if("rating".equals(sortOption)){
               cq.orderBy(cb.desc(book.get("averageRating"))); 
            }
        }
        
        //Query
        cq.select(book).where(predicates.toArray(new Predicate[]{}));
        //execute query and do something with result
        List<Book>results=em.createQuery(cq)
                            .setFirstResult(offset)
                            .setMaxResults(limit)
                            .getResultList();
        
        return results;
    }
    
}
