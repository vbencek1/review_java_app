/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.facade;

import java.util.List;
import javax.ejb.Local;
import org.vbencek.model.UserT;

/**
 *  
 * @author Tino
 */
@Local
public interface UserTFacadeLocal {

    void create(UserT userT);

    void edit(UserT userT);

    void remove(UserT userT);

    UserT find(Object id);

    List<UserT> findAll();

    List<UserT> findRange(int[] range);

    int count();
    
    UserT findUserByUsername(String username);
    
    boolean checkIfEmailExist(String email);
    
    List<UserT> findUsersWithLimit(int offset, int limit);
}
