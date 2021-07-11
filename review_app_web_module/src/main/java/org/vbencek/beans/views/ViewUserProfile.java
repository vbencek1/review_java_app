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
import org.vbencek.email.EmailSender;
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
    String newPassword = "";

    @Getter
    @Setter
    String confNewPassword = "";

    @Getter
    @Setter
    String confCode;

    private int generatedCode;

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
    String messageCode = "";

    @Getter
    @Setter
    String messagePass = "";

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

    @Getter
    @Setter
    boolean rendermessageCodeOK = false;

    @Getter
    @Setter
    boolean rendermessageCodeNotOK = false;

    @Getter
    @Setter
    boolean rendermessagePassOK = false;

    @Getter
    @Setter
    boolean rendermessagePassNotOK = false;

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
        messageCode = "";
        messagePass = "";
        rendermessageInfoNotOK = false;
        rendermessageInfoOK = false;
        rendermessageEmailOK = false;
        rendermessageEmailNotOK = false;
        rendermessageCodeNotOK = false;
        rendermessageCodeOK = false;
        rendermessagePassNotOK = false;
        rendermessagePassOK = false;
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
            EmailSender emailSender = new EmailSender();
            String emailTo = email;
            String subject = "New email conf code";
            generatedCode = emailSender.generateConfirmationCode();
            String text = "Code:" + generatedCode;
            emailSender.sendMessage(emailTo, subject, text);
            System.out.println("ViewUserProfile: Sending: " + text);
            rendermessageEmailOK = true;
            messageEmail = "Konfirmacijski kod poslan je na vašu email adresu. Unesite ga kako biste potvrdili promjenu.";
        } else {
            rendermessageEmailNotOK = true;
            messageEmail = "Unesena lozinka je neispravna.";
        }
    }

    private void updateEmail() {
        UserT updatedUser = thisUser;
        updatedUser.setEmail(email);
        userTFacade.edit(updatedUser);
    }

    public void updateUserEmail() {
        resetMessages();
        int integerCode = convertToInt(confCode);
        if (generatedCode == integerCode && integerCode != 0) {
            updateEmail();
            rendermessageCodeOK = true;
            messageCode = "EMAIL AŽURIRAN";
        } else {
            rendermessageCodeNotOK = true;
            messageCode = "Neispravan kod. Pokusajte ponovo.";
        }
    }

    private void updatepassword() {
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        UserT updatedUser = thisUser;
        updatedUser.setPassword(encoder.encode(newPassword));
        userTFacade.edit(updatedUser);
    }

    public void updateUserPassword() {
        resetMessages();
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        if (encoder.matches(password, thisUser.getPassword()) && newPassword.equals(confNewPassword)) {
            updatepassword();
            rendermessagePassOK = true;
            messagePass = "Podaci su ažurirani.";
        } else if(!encoder.matches(password, thisUser.getPassword())) {
            rendermessagePassNotOK = true;
            messagePass = "Unesena lozinka je neispravna";
        } else if(!newPassword.equals(confNewPassword)){
            rendermessagePassNotOK = true;
            messagePass = "Nove lozinke nisu identične";
        }
    }

    private int convertToInt(String broj) {
        try {
            return Integer.parseInt(broj);
        } catch (Exception e) {
            return 0;
        }
    }

}
