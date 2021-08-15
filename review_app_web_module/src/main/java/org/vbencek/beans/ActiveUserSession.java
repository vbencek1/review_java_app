/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.vbencek.facade.DataLogFacadeLocal;
import org.vbencek.facade.UserTFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.DataLog;
import org.vbencek.model.UserT;

/**
 * Class that holds active user information. It also Login, logout and application navigation
 * @author vbencek
 */
@Named(value = "activeUserSession")
@SessionScoped
public class ActiveUserSession implements Serializable {

    @EJB(beanName = "UserTFacade")
    UserTFacadeLocal userTFacade;

    @EJB(beanName = "DataLogFacade")
    DataLogFacadeLocal dataLogFacade;

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
    String pleaseLoginMsg = "";
    @Getter
    @Setter
    String failedLoginMsg = "";
    @Getter
    @Setter
    UserT activeUser = null;
    
    /**
     * Checks if user was redirected here. If he was, show him aprropriate message.
     */
    @PostConstruct
    public void init() {
        //prijevodi
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String action = params.get("action");
        pleaseLoginMsg = "";
        if ("openLogin".equals(action)) {
            pleaseLoginMsg = res.getString("template.login.redirectMsg");
        }

    }

    public void setActiveAppUser(UserT user) {
        setActiveUser(user);
        renderLogin();
    }
    
    /**
     * Hides unnessesery fields when user is successfuly loged in
     */
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
        failedLoginMsg = "";
        script = switchButtonDisplay("none");
        addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "login");
    }
    
    public void redirectToUrl(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }
    
    /**
     * Hides unnessesery fields when user is successfuly logged out
     */
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
        addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "logout");
        redirectToUrl("index.xhtml");
    }
    
    public void authenticate() {
        if (activeUser == null) {
            // Active user doesn't exist. Login
            UserT tempUser = userTFacade.findUserByUsername(username);
            if (tempUser != null) {
                Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
                boolean passMatch = encoder.matches(password, tempUser.getPassword());
                boolean isBlocked = tempUser.getIsblocked();
                if (passMatch && !isBlocked) {
                    activeUser = tempUser;
                    renderLogin();
                } else if (!passMatch) {
                    System.out.println("2");
                    failedLoginMsg = res.getString("template.login.wrongPass");
                } else if (isBlocked) {
                    System.out.println("1");
                    failedLoginMsg = res.getString("template.login.blockedUser");
                }
            } else {
                failedLoginMsg = res.getString("template.login.wrongUsername");
            }
        } else {
            // Active user exists. Logout
            renderLogout();
            activeUser = null;
        }
    }
    
    /**
     * Show login form
     * @param mode
     * @return 
     */
    private String switchButtonDisplay(String mode) {
        String fja = "document.getElementById('loginPopup').style.display = '" + mode + "'";
        return fja;
    }

    public String redirectToUserProfile() {
        return "userProfile.xhtml?id=" + activeUser.getUserId() + "&faces-redirect=true";
    }

    public String redirectToIndex() {
        setFailedLoginMsg("");
        return "index.xhtml?faces-redirect=true";
    }

    public String redirectToBookSearch() {
        setFailedLoginMsg("");
        return "bookSearch.xhtml?faces-redirect=true";
    }

    public String redirectToMyCollecion() {
        setFailedLoginMsg("");
        if (activeUser != null) {
            return "bookCollection.xhtml?faces-redirect=true";
        } else {
            return "index.xhtml?action=openLogin&faces-redirect=true";
        }
    }

    public String redirectToMyReviews() {
        setFailedLoginMsg("");
        if (activeUser != null) {
            return "myReviews.xhmtl?faces-redirect=true";
        } else {
            return "index.xhtml?action=openLogin&faces-redirect=true";
        }
    }

    public String redirectToAddBookRequest() {
        setFailedLoginMsg("");
        if (activeUser != null) {
            return "addBookRequest.xhmtl?faces-redirect=true";
        } else {
            return "index.xhtml?action=openLogin&faces-redirect=true";
        }
    }

    //user action logging
    public <T> void addDataLog(String viewName, String methodName, T params) {
        if (activeUser != null) {
            ObjectMapper mapper = new ObjectMapper();
            DataLog dataLog = new DataLog();
            String stringParams = "";
            if (params != null) {
                try {
                    stringParams = mapper.writeValueAsString(params);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            String shorterParams=StringUtils.abbreviate(stringParams, 995);
            dataLog.setUserId(activeUser);
            dataLog.setActionDate(new Date());
            dataLog.setMethodName(methodName);
            dataLog.setViewName(viewName);
            dataLog.setParametars(shorterParams);
            dataLogFacade.create(dataLog);
        }
    }

}
