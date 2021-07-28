/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.vbencek.model.EmployeeRole;

/**
 *
 * @author Tino
 */
@Stateless
public class EmployeeRoleFacade extends AbstractFacade<EmployeeRole> implements EmployeeRoleFacadeLocal {

    @PersistenceContext(unitName = "new_review_app_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmployeeRoleFacade() {
        super(EmployeeRole.class);
    }
    
}
