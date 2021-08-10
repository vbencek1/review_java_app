/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.views.template;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.vbencek.email.EmailSender;
import org.vbencek.facade.UserTFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.UserT;

/**
 * View that's used to recover forgoten password
 * @author vbencek
 */
@Named(value = "forgotenPasswordBean")
@ViewScoped
public class ForgotenPasswordBean implements Serializable {

    @EJB(beanName = "UserTFacade")
    UserTFacadeLocal userTFacade;

    @Inject
    Localization localization;
    ResourceBundle res;

    @Getter
    @Setter
    String username = "";

    @Getter
    @Setter
    String email = "";
    @Getter
    @Setter
    String message = "";

    private UserT findUserWithEmail(String username) {
        UserT user = userTFacade.findUserByUsername(username);
        if (user == null) {
            return null;
        }
        if (!user.getEmail().equals(email)) {
            return null;
        }
        return user;
    }

    private void updatePassword(UserT user, String newPass) {
        UserT updatedUser = user;
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
        String hashPassword = pbkdf2PasswordEncoder.encode(newPass);
        updatedUser.setPassword(hashPassword);
        userTFacade.edit(updatedUser);
    }
    
    /**
     * Sends password via email
     */
    public void sendPassword() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        UserT user = findUserWithEmail(username);
        if (user != null) {
            EmailSender emailSender = new EmailSender();
            String emailTo = email;
            String subject = res.getString("template.emailHeaderFrg");
            String newPass = String.valueOf(emailSender.generateConfirmationCode());
            String text = res.getString("template.emailBodyFrg") + newPass;
            emailSender.sendMessage(emailTo, subject, text);
            System.out.println("ForgotenPass: Sending: " + text);
            updatePassword(user, newPass);
            message = res.getString("template.frgMessageOk");
        } else {
            message = res.getString("template.frgMessageNotOk");
        }
        username = "";
        email = "";
    }

}
