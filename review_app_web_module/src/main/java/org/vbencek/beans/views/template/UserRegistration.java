package org.vbencek.beans.views.template;

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
@Named(value = "userRegistration")
@ViewScoped
public class UserRegistration implements Serializable {

    @EJB(beanName = "UserTFacade")
    UserTFacadeLocal userTFacade;

    @Inject
    Localization localization;
    ResourceBundle res;

    @Inject
    ActiveUserSession activeUserSession;

    @Getter
    @Setter
    String username = "";

    @Getter
    @Setter
    String password = "";

    @Getter
    @Setter
    String email = "";

    @Getter
    @Setter
    String firstName = "";

    @Getter
    @Setter
    String lastName = "";

    @Getter
    @Setter
    String confirmationCode;

    @Getter
    @Setter
    String codeMessage = "";

    @Getter
    @Setter
    boolean showResendLink = false;

    @Getter
    @Setter
    boolean showHomepageLink = false;
    @Getter
    @Setter
    boolean renderScripter = false;

    private int generatedCode;

    @Getter
    public static Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();

    @PostConstruct
    public void init() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
    }

    public void sendRegistrationCode() {
        if (activeUserSession.getActiveUser() == null) {
            try {
                setConfirmationCode("");
                EmailSender emailSender = new EmailSender();
                String emailTo = email;
                String subject = res.getString("userRegistration.messageTitle");
                generatedCode = emailSender.generateConfirmationCode();
                String text = res.getString("userRegistration.messageBody") + generatedCode;
                emailSender.sendMessage(emailTo, subject, text);
                System.out.println("UserRegistration: Sending: " + text);
                codeMessage = "";
                renderScripter = true;
            } catch (Exception e) {
                System.out.println("UserRegistration: mail not sent");
            }
        }
    }

    public void confirmCode() {
        if (activeUserSession.getActiveUser() == null) {
            System.out.println(username + " " + generatedCode + " " + confirmationCode);
            int integerCode = convertToInt(confirmationCode);
            if (generatedCode == integerCode && integerCode != 0) {
                RegisterUser();
                System.out.println("Registration: User registered!");
                codeMessage = res.getString("userRegistration.successMessage");
                showResendLink = false;
                showHomepageLink = true;
            } else {
                System.out.println("Registration: Registration failed; Wrong confirmation code");
                codeMessage = res.getString("userRegistration.failureMessage");
                showResendLink = true;
                showHomepageLink = false;
            }
        }
        renderScripter = true;
    }

    private void RegisterUser() {
        UserT user = new UserT();
        String hashPassword = pbkdf2PasswordEncoder.encode(password);
        user.setPassword(hashPassword);
        user.setUsername(username);
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        userTFacade.create(user);
        loginUser(user);
    }

    private void loginUser(UserT user) {
        activeUserSession.setActiveAppUser(user);
    }


    private int convertToInt(String broj) {
        try {
            return Integer.parseInt(broj);
        } catch (Exception e) {
            return 0;
        }
    }

}
