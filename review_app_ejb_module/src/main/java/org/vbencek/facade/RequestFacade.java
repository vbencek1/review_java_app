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
import org.vbencek.model.Request;

/**
 *
 * @author Tino
 */
@Stateless
public class RequestFacade extends AbstractFacade<Request> implements RequestFacadeLocal {

    @PersistenceContext(unitName = "new_review_app_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RequestFacade() {
        super(Request.class);
    }

    @Override
    public List<Request> findRequestsByISBNExists(boolean hasISBN) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Request> cq = cb.createQuery(Request.class);
        Root<Request> request = cq.from(Request.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (hasISBN) {
            predicates.add(cb.isNotNull(request.get("isbn")));
        }else{
            predicates.add(cb.isNull(request.get("isbn")));
        }
        //Query
        cq.select(request).where(predicates.toArray(new Predicate[]{}));
        //execute query and do something with result
        List<Request> results = em.createQuery(cq)
                .getResultList();

        return results;
    }

    @Override
    public List<Request> findRequestsByISBN(String isbn) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Request> cq = cb.createQuery(Request.class);
        Root<Request> request = cq.from(Request.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (isbn !=null && !"".equals(isbn)) {
            predicates.add(cb.equal(request.get("isbn"),isbn));
        }
        //Query
        cq.select(request).where(predicates.toArray(new Predicate[]{}));
        //execute query and do something with result
        List<Request> results = em.createQuery(cq)
                .getResultList();

        return results;
    }
    
}
