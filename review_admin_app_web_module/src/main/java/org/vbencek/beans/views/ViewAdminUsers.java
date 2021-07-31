/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.views;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author Tino
 */
@Named(value = "viewAdminUsers")
@ViewScoped
public class ViewAdminUsers implements Serializable {

    /**
     * Creates a new instance of ViewAdminUsers
     */
    public ViewAdminUsers() {
    }
    
}
