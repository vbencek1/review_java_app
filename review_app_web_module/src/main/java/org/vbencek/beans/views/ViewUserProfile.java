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
import javax.ejb.EJB;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.facade.UserTFacadeLocal;
import org.vbencek.model.UserT;

/**
 *
 * @author Tino
 */
@Named(value = "viewUserProfile")
@ViewScoped
public class ViewUserProfile implements Serializable {

    @Inject
    ActiveUserSession activeUserSession;

    @EJB(beanName = "UserTFacade")
    UserTFacadeLocal userTFacade;

    @Getter
    @Setter
    String username;

    @Getter
    @Setter
    String firstname;

    @Getter
    @Setter
    String lastname;

    @Getter
    @Setter
    String email;

    @Getter
    @Setter
    String password;

    @Getter
    @Setter
    String confPassword;

    @Getter
    @Setter
    String confCode;

    @Getter
    @Setter
    boolean renderRedirect = false;

    @Getter
    @Setter
    String redirectFunction = "";

    @Getter
    @Setter
    String messageInfo = "";

    @Getter
    @Setter
    String messageEmail = "";

    @Getter
    @Setter
    boolean rendermessageInfoOK = false;

    @Getter
    @Setter
    boolean rendermessageInfoNotOK = false;

    @Getter
    @Setter
    boolean rendermessageEmailOK = false;

    @Getter
    @Setter
    boolean rendermessageEmailNotOK = false;

    UserT thisUser;

    @PostConstruct
    public void init() {
        thisUser = activeUserSession.getActiveUser();
        if (thisUser == null) {
            redirectFunction = "location.href = 'index.xhtml?action=openLogin';";
            renderRedirect = true;
        } else {
            setUserparams();
        }
    }

    private void setUserparams() {
        //Netreba mi ovo ali za sad ostavljam
        //Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        //String userID = params.get("id");
        username = thisUser.getUsername();
        firstname = thisUser.getFirstname();
        lastname = thisUser.getLastname();
        email = thisUser.getEmail();
    }

    private void resetMessages() {
        messageInfo = "";
        messageEmail = "";
        rendermessageInfoNotOK = false;
        rendermessageInfoOK = false;
        rendermessageEmailOK = false;
        rendermessageEmailNotOK = false;
    }

    public void updateUserInfo() {
        resetMessages();
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        if (encoder.matches(password, thisUser.getPassword())) {
            UserT updatedUser = thisUser;
            updatedUser.setFirstname(firstname);
            updatedUser.setLastname(lastname);
            userTFacade.edit(updatedUser);
            rendermessageInfoOK = true;
            messageInfo = "Podaci su ažurirani.";
        } else {
            rendermessageInfoNotOK = true;
            messageInfo = "Unesena lozinka je neispravna.";
        }

    }

    public void sendConfCode() {
        resetMessages();
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        if (encoder.matches(password, thisUser.getPassword())) {
            rendermessageEmailOK = true;
            messageEmail = "Konfirmacijski kod poslan je na vašu email adresu. Unesite ga kako biste potvrdili promjenu.";
        } else {
            rendermessageEmailNotOK = true;
            messageEmail = "Unesena lozinka je neispravna.";
        }
    }

    public void updateUserEmail() {

    }

}
