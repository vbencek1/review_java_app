/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.facade;

import java.util.List;
import javax.ejb.Local;
import org.vbencek.model.EmployeeRole;

/**
 *
 * @author Tino
 */
@Local
public interface EmployeeRoleFacadeLocal {

    void create(EmployeeRole employeeRole);

    void edit(EmployeeRole employeeRole);

    void remove(EmployeeRole employeeRole);

    EmployeeRole find(Object id);

    List<EmployeeRole> findAll();

    List<EmployeeRole> findRange(int[] range);

    int count();
    
}
