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
import org.vbencek.model.Employee;

/**
 *
 * @author Tino
 */
@Stateless
public class EmployeeFacade extends AbstractFacade<Employee> implements EmployeeFacadeLocal {

    @PersistenceContext(unitName = "new_review_app_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmployeeFacade() {
        super(Employee.class);
    }
    
    @Override
    public Employee findEmployeeByUsername(String username) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> rt = cq.from(Employee.class);
        ParameterExpression<String> parametar = cb.parameter(String.class);
        cq.select(rt).where(cb.equal(rt.get("username"), parametar));
        Query q = getEntityManager().createQuery(cq);
        q.setParameter(parametar, username);

        List<Employee> temp = q.getResultList();
        if (temp.isEmpty()) {
            return null;
        }
        Employee user = temp.get(0);
        return user;
    }
    
}
