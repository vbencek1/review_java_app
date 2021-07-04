/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.views;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.vbencek.beans.ActiveUserSession;

/**
 *
 * @author Tino
 */
@Named(value = "viewAddBookRequest")
@ViewScoped
public class ViewAddBookRequest implements Serializable {

    @Inject
    ActiveUserSession activeUserSession;
    
    @Getter
    @Setter
    boolean renderRedirect = false;

    @Getter
    @Setter
    String redirectFunction = "";
    
    @PostConstruct
    void init(){
        if (activeUserSession.getActiveUser() == null) {
            redirectFunction = "location.href = 'index.xhtml';";
            renderRedirect = true;
        }
    }
    
}
