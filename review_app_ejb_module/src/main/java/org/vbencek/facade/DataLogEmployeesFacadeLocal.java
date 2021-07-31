/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.facade;

import java.util.List;
import javax.ejb.Local;
import org.vbencek.model.DataLogEmployees;

/**
 *
 * @author Tino
 */
@Local
public interface DataLogEmployeesFacadeLocal {

    void create(DataLogEmployees dataLogEmployees);

    void edit(DataLogEmployees dataLogEmployees);

    void remove(DataLogEmployees dataLogEmployees);

    DataLogEmployees find(Object id);

    List<DataLogEmployees> findAll();

    List<DataLogEmployees> findRange(int[] range);

    int count();
    
}
