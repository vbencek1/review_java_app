/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.validators;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.vbencek.facade.UserTFacadeLocal;
import org.vbencek.localization.Localization;

/**
 *
 * @author Tino
 */
@Named(value = "validations")
@ViewScoped
public class Validations implements Serializable {
    
    @EJB(beanName = "UserTFacade")
    UserTFacadeLocal userTFacade;
    
    @Inject
    Localization localization;
    ResourceBundle res;

    @Getter
    @Setter
    String validationMessage = "";
    
    String msgFirstName="";
    String msgLastName="";
    String msgUsername="";
    String msgPassword="";
    String msgEmail="";

    @PostConstruct
    public void init() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
    }

    public void validateFirstName(FacesContext context, UIComponent comp, Object value) {
        msgFirstName="";
        String validString = (String) value;
        if (validString.length() < 1) {
            System.out.println("validations: invalid first name");
            ((UIInput) comp).setValid(false);
            msgFirstName = "*" + res.getString("validations.validateFirstName") + "\n";
        }
    }

    public void validateLastName(FacesContext context, UIComponent comp, Object value) {
        msgLastName="";
        String validString = (String) value;
        if (validString.length() < 1) {
            System.out.println("validations: invalid last name");
            ((UIInput) comp).setValid(false);
            msgLastName = "*" + res.getString("validations.validateLastName") + "\n";
        }
    }

    public void validateUsername(FacesContext context, UIComponent comp, Object value) {
        msgUsername="";
        String validString = (String) value;
        if (validString.length() < 4 || validString.length() > 16) {
            System.out.println("validations: invalid username");
            ((UIInput) comp).setValid(false);
            msgUsername += "*" + res.getString("validations.validateUsername") + "\n";
        }else if(userTFacade.findUserByUsername(validString)!=null){
            System.out.println("validations: username already exists");
            ((UIInput) comp).setValid(false);
            msgUsername = "*" + res.getString("validations.validateUsernameExists") + "\n";
        }
    }

    public void validatePassword(FacesContext context, UIComponent comp, Object value) {
        msgPassword="";
        String validString = (String) value;
        String confPassword = context.getExternalContext().getRequestParameterMap().get("registrationForm:confPassword");
        if (validString.length() < 4 || validString.length() > 20) {
            System.out.println("validations: invalid password");
            ((UIInput) comp).setValid(false);
            msgPassword = "*" + res.getString("validations.validatePassword") + "\n";
        } else if (!validString.equals(confPassword)) {
            System.out.println("validations: password aren't matching");
            ((UIInput) comp).setValid(false);
            msgPassword = "*" + res.getString("validations.validateConfPassword") + "\n";
        }
    }
    
    public void validatePasswordOnUserInfo(FacesContext context, UIComponent comp, Object value) {
        msgPassword="";
        String validString = (String) value;
        String confPassword = context.getExternalContext().getRequestParameterMap().get("changePassForm:confPassword");
        if (validString.length() < 4 || validString.length() > 20) {
            System.out.println("validations: invalid password");
            ((UIInput) comp).setValid(false);
            msgPassword = "*" + res.getString("validations.validatePassword") + "\n";
        } else if (!validString.equals(confPassword)) {
            System.out.println("validations: password aren't matching");
            ((UIInput) comp).setValid(false);
            msgPassword = "*" + res.getString("validations.validateConfPassword") + "\n";
        }
    }

    public void validateEmail(FacesContext context, UIComponent comp, Object value) {
        msgEmail="";
        String validString = (String) value;
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher match = pattern.matcher(validString);
        boolean checkValid = match.matches();
        if (!checkValid) {
            System.out.println("validations: invalid Email");
            ((UIInput) comp).setValid(false);
            msgEmail = "*" + res.getString("validations.validateEmail") + "\n";
        }else if(userTFacade.checkIfEmailExist(validString)){
            System.out.println("validations: email already exists");
            ((UIInput) comp).setValid(false);
            msgEmail = "*" + res.getString("validations.validateEmailExists") + "\n";
        }
    }
    
    public String printValidationMessage(){
        setValidationMessage("");
        validationMessage=msgFirstName+msgLastName+msgUsername+msgPassword+msgEmail;
        return validationMessage;
    }
    public String printValidationMessageInfo(){
        setValidationMessage("");
        validationMessage=msgFirstName+msgLastName;
        return validationMessage;
    }
    public String printValidationMessageEmail(){
        setValidationMessage("");
        validationMessage=msgEmail;
        return validationMessage;
    }
    public String printValidationMessagePassword(){
        setValidationMessage("");
        validationMessage=msgPassword;
        return validationMessage;
    }

}
