/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.localization;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vbencek.beans.ActiveUserSession;

/**
 *
 * @author Tino
 */
@Named(value = "localizationForcer")
@ViewScoped
public class LocalizationForcer implements Serializable {

    @Inject
    ActiveUserSession activeUserSession;
 
    @PostConstruct
    public void init() {
        activeUserSession.init();
    }
    
}
