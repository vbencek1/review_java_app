/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.views;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.email.EmailSender;
import org.vbencek.facade.UserTFacadeLocal;
import org.vbencek.localization.Localization;
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
    
    @Inject 
    Localization localization;
    ResourceBundle res;
    
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
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
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
            messageInfo = res.getString("viewUserProfile.msg.updateData");
            activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "Information update");
        } else {
            rendermessageInfoNotOK = true;
            messageInfo = res.getString("viewUserProfile.msg.invalidPass");
        }

    }

    public void sendConfCode() {
        resetMessages();
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        if (encoder.matches(password, thisUser.getPassword())) {
            EmailSender emailSender = new EmailSender();
            String emailTo = email;
            String subject = res.getString("viewUserProfile.msg.emailSubject");
            generatedCode = emailSender.generateConfirmationCode();
            String text = res.getString("viewUserProfile.msg.emailBody") + generatedCode;
            emailSender.sendMessage(emailTo, subject, text);
            System.out.println("ViewUserProfile: Sending: " + text);
            rendermessageEmailOK = true;
            messageEmail = res.getString("viewUserProfile.msg.codeSend");
        } else {
            rendermessageEmailNotOK = true;
            messageEmail = res.getString("viewUserProfile.msg.invalidPass");
        }
    }

    private void updateEmail() {
        UserT updatedUser = thisUser;
        updatedUser.setEmail(email);
        userTFacade.edit(updatedUser);
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "New email: "+email);
    }

    public void updateUserEmail() {
        resetMessages();
        int integerCode = convertToInt(confCode);
        if (generatedCode == integerCode && integerCode != 0) {
            updateEmail();
            rendermessageCodeOK = true;
            messageCode = res.getString("viewUserProfile.msg.updateEmail");
        } else {
            rendermessageCodeNotOK = true;
            messageCode = res.getString("viewUserProfile.msg.invalidCode");
        }
    }

    private void updatepassword() {
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        UserT updatedUser = thisUser;
        updatedUser.setPassword(encoder.encode(newPassword));
        userTFacade.edit(updatedUser);
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "Password update");
    }

    public void updateUserPassword() {
        resetMessages();
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        if (encoder.matches(password, thisUser.getPassword()) && newPassword.equals(confNewPassword)) {
            updatepassword();
            rendermessagePassOK = true;
            messagePass = res.getString("viewUserProfile.msg.updatePass");
        } else if(!encoder.matches(password, thisUser.getPassword())) {
            rendermessagePassNotOK = true;
            messagePass = res.getString("viewUserProfile.msg.invalidPass");
        } else if(!newPassword.equals(confNewPassword)){
            rendermessagePassNotOK = true;
            messagePass = res.getString("viewUserProfile.msg.invalidConfPass");
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
