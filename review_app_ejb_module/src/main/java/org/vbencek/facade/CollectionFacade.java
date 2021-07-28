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
    
}
