/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.vbencek.facade.DataLogEmployeesFacadeLocal;
import org.vbencek.facade.DataLogFacadeLocal;
import org.vbencek.facade.EmployeeFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.DataLog;
import org.vbencek.model.DataLogEmployees;
import org.vbencek.model.Employee;

/**
 *
 * @author Tino
 */
@Named(value = "activeUserSession")
@SessionScoped
public class ActiveUserSession implements Serializable {

    @EJB(beanName = "EmployeeFacade")
    EmployeeFacadeLocal employeeFacade;

    @EJB(beanName = "DataLogEmployeesFacade")
    DataLogEmployeesFacadeLocal dataLogEmployeesFacade;

    @Inject
    Localization localization;
    ResourceBundle res;

    @Getter
    @Setter
    String username = "";
    @Getter
    @Setter
    String password = "";
    @Getter
    @Setter
    String failedLoginMsg = "";
    @Getter
    @Setter
    boolean renderLoginRedirect=false;
    @Getter
    @Setter
    Employee activeUser = null;

    @PostConstruct
    public void init() {
        //prijevodi
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
    }
    
    public void loginUser(){
        if (activeUser == null) {
            Employee tempUser = employeeFacade.findEmployeeByUsername(username);
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
        } 
    }
    
    public void logoutUser(){
        if (activeUser != null){
            renderLogout();
            activeUser = null;
        }
    }
    
    private void renderLogin() {
        failedLoginMsg = "";
        renderLoginRedirect=true;
        addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "login");
    }

    private void renderLogout() {
        renderLoginRedirect=false;
        addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "logout");
    }

    public String redirectToUserProfile() {
        return "userProfile.xhmtl?id=" + activeUser.getEmployeeId() + "&faces-redirect=true";
    }


    //user action logging
    public <T> void addDataLog(String viewName, String methodName, T params) {
        if (activeUser != null) {
            ObjectMapper mapper = new ObjectMapper();
            DataLogEmployees dataLog = new DataLogEmployees();
            String stringParams = "";
            if (params != null) {
                try {
                    stringParams = mapper.writeValueAsString(params);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            String shorterParams=StringUtils.abbreviate(stringParams, 995);
            dataLog.setEmployeeId(activeUser);
            dataLog.setActionDate(new Date());
            dataLog.setMethodName(methodName);
            dataLog.setViewName(viewName);
            dataLog.setParametars(shorterParams);
            dataLogEmployeesFacade.create(dataLog);
        }
    }

}
