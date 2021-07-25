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
import org.vbencek.model.UserT;

/**
 *
 * @author Tino
 */
@Stateless
public class ReviewFacade extends AbstractFacade<Review> implements ReviewFacadeLocal {

    @PersistenceContext(unitName = "new_review_app_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReviewFacade() {
        super(Review.class);
    }

    @Override
    public List<Review> findReviewsByCriteria(Book book, UserT userT, double minimumAvgRating, String sortOption, boolean hasDescription, boolean isPublic, int offset, int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);
        Root<Review> review = cq.from(Review.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if(book!=null){
            predicates.add(cb.equal(review.get("book"), book));
        }
        if(userT !=null){
            predicates.add(cb.equal(review.get("userT"), userT));
        }
        
        if(minimumAvgRating!=0){
            predicates.add(cb.greaterThanOrEqualTo(review.get("rating"), minimumAvgRating));
        }
        if(hasDescription){
            predicates.add(cb.isNotNull(review.get("description")));
        }
        if(isPublic){
             predicates.add(cb.isTrue(review.get("ispublic")));
        }
        //add sort options
        if(sortOption!=null && !"".equals(sortOption)){    
            //TODO
        }
        
        //Query
        cq.select(review).where(predicates.toArray(new Predicate[]{}));
        //execute query and do something with result
        List<Review>results=em.createQuery(cq)
                            .setFirstResult(offset)
                            .setMaxResults(limit)
                            .getResultList();
        
        return results;
    }

    @Override
    public long countReviewsByCriteria(Book book, UserT userT, double minimumAvgRating, boolean hasDescription, boolean isPublic) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Review> review = cq.from(Review.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if(book!=null){
            predicates.add(cb.equal(review.get("book"), book));
        }
        if(userT !=null){
            predicates.add(cb.equal(review.get("userT"), userT));
        }
        
        if(minimumAvgRating!=0){
            predicates.add(cb.greaterThanOrEqualTo(review.get("rating"), minimumAvgRating));
        }
        if(hasDescription){
            predicates.add(cb.isNotNull(review.get("description")));
        }
        if(isPublic){
             predicates.add(cb.isTrue(review.get("ispublic")));
        }
        //Query
        cq.select(cb.count(review)).where(predicates.toArray(new Predicate[]{}));
        long count=em.createQuery(cq).getSingleResult();
        
        return count;
    }

}
