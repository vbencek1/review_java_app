/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import org.vbencek.model.UserT;

/**
 *
 * @author Tino
 */
@Stateless
public class UserTFacade extends AbstractFacade<UserT> implements UserTFacadeLocal {

    @PersistenceContext(unitName = "new_review_app_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserTFacade() {
        super(UserT.class);
    }

    @Override
    public UserT findUserByUsername(String username) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserT> cq = cb.createQuery(UserT.class);
        Root<UserT> rt = cq.from(UserT.class);
        ParameterExpression<String> parametar = cb.parameter(String.class);
        cq.select(rt).where(cb.equal(rt.get("username"), parametar));
        Query q = getEntityManager().createQuery(cq);
        q.setParameter(parametar, username);

        List<UserT> temp = q.getResultList();
        if (temp.isEmpty()) {
            return null;
        }
        UserT user = temp.get(0);
        return user;
    }

    @Override
    public boolean checkIfEmailExist(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserT> cq = cb.createQuery(UserT.class);
        Root<UserT> rt = cq.from(UserT.class);
        ParameterExpression<String> parametar = cb.parameter(String.class);
        cq.select(rt).where(cb.equal(rt.get("email"), parametar));
        Query q = getEntityManager().createQuery(cq);
        q.setParameter(parametar, email);

        List<UserT> temp = q.getResultList();
        return !temp.isEmpty();
    }

    @Override
    public List<UserT> findUsersWithLimit(int offset, int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserT> cq = cb.createQuery(UserT.class);
        Root<UserT> user = cq.from(UserT.class);
        cq.select(user);
        List<UserT> results = em.createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return results;
        
    }

}
