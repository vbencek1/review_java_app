/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.vbencek.facade.UserTFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.UserT;

/**
 *
 * @author Tino
 */
@Named(value = "activeUserSession")
@SessionScoped
public class ActiveUserSession implements Serializable {

    @EJB(beanName = "UserTFacade")
    UserTFacadeLocal userTFacade;

    @Inject
    Localization localization;
    ResourceBundle res;

    @Getter
    @Setter
    String randomUserTest = "";

    @Getter
    @Setter
    String username = "";
    @Getter
    @Setter
    String password = "";
    @Getter
    @Setter
    String btnAutenhName = "";
    @Getter
    @Setter
    boolean showInputUsername = true;
    @Getter
    @Setter
    boolean showInputPass = true;
    @Getter
    @Setter
    boolean showLblInfo = false;
    @Getter
    @Setter
    boolean showRegistrationLink = true;
    @Getter
    @Setter
    boolean showForgottenPassLink = true;
    @Getter
    @Setter
    boolean showBtnLogin = true;
    @Getter
    @Setter
    boolean showBtnLogout = false;
    @Getter
    @Setter
    String script = "";
    @Getter
    @Setter
    boolean renderScripter = false;
    @Getter
    @Setter
    UserT activeUser = null;

    @PostConstruct
    public void init() {
        //prijevodi
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
    }

    public void setActiveAppUser(UserT user) {
        setActiveUser(user);
        renderLogin();
    }

    private void renderLogin() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        showBtnLogout = true;
        showBtnLogin = false;
        showInputUsername = false;
        showInputPass = false;
        showLblInfo = true;
        showForgottenPassLink = false;
        showRegistrationLink = false;
        renderScripter = true;
        script = switchButtonDisplay("none");
    }

    private void renderLogout() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        showBtnLogout = false;
        showBtnLogin = true;
        showInputUsername = true;
        showInputPass = true;
        showLblInfo = false;
        showForgottenPassLink = true;
        showRegistrationLink = true;
        script = switchButtonDisplay("block");
    }

    public void authenticate() {
        if (activeUser == null) {
            //login
            activeUser = userTFacade.findUserByUsername(username);
            Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
            if (activeUser != null && encoder.matches(password, activeUser.getPassword())) { 
                renderLogin();
            }
        } else {
            //logout
            activeUser = null;
            renderLogout();
        }
    }

    private String switchButtonDisplay(String mode) {
        String fja = "document.getElementById('loginPopup').style.display = '" + mode + "'";
        return fja;
    }

}
