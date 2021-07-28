/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.facade;

import java.util.List;
import javax.ejb.Local;
import org.vbencek.model.DataLog;

/**
 *
 * @author Tino
 */
@Local
public interface DataLogFacadeLocal {

    void create(DataLog dataLog);

    void edit(DataLog dataLog);

    void remove(DataLog dataLog);

    DataLog find(Object id);

    List<DataLog> findAll();

    List<DataLog> findRange(int[] range);

    int count();
    
}
