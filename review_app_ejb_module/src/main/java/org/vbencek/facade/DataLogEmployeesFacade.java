/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.vbencek.model.DataLogEmployees;

/**
 *
 * @author Tino
 */
@Stateless
public class DataLogEmployeesFacade extends AbstractFacade<DataLogEmployees> implements DataLogEmployeesFacadeLocal {

    @PersistenceContext(unitName = "new_review_app_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DataLogEmployeesFacade() {
        super(DataLogEmployees.class);
    }
    
}
