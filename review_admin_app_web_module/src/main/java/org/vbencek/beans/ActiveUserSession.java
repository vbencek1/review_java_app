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
import org.vbencek.facade.EmployeeFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.DataLogEmployees;
import org.vbencek.model.Employee;
import org.vbencek.properties.PropertiesLoader;

/**
 * Class that stores active employee (user) informations.
 * Used for login and logout
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
    boolean renderLoginRedirect = false;
    @Getter
    @Setter
    Employee activeUser = null;

    @PostConstruct
    public void init() {
        //prijevodi
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
    }

    /**
     * Method first checks if username exists, then it's matching password using encripter and finnaly it checks if user is blocked.
     * Stores user in active user variable or prints apropriate message if failed.
     */
    public void loginUser() {
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
                    failedLoginMsg = res.getString("template.login.wrongPass");
                } else if (isBlocked) {
                    failedLoginMsg = res.getString("template.login.blockedUser");
                }
            } else {
                failedLoginMsg = res.getString("template.login.wrongUsername");
            }
        }
    }

    public void logoutUser() {
        if (activeUser != null) {
            renderLogout();
            activeUser = null;
        }
    }

    private void renderLogin() {
        failedLoginMsg = "";
        renderLoginRedirect = true;
        addDataLog(this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName(), "login");
    }

    private void renderLogout() {
        renderLoginRedirect = false;
        addDataLog(this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName(), "logout");
    }
    
    /**
     * Method is used to render admin view so that moderators can't see admin options
     * @return 
     */
    public boolean isUserAdmin() {
        if (activeUser != null) {
            PropertiesLoader propLoader = new PropertiesLoader();
            int adminRole = 0;
            try {
                adminRole = Integer.parseInt(propLoader.getProperty("admin.role"));
            } catch (Exception e) {
                System.out.println("ADMIN.ROLE ->Cant convert from properties!");
            }
            return activeUser.getEmployeeRoleId().getEmployeeRoleId() == adminRole;
        }
        return false;
    }

    public String redirectToUserProfile() {
        return "adminEmployeeInfo.xhmtl?&faces-redirect=true";
    }

    /**
     * Used for logging. Will be replaced with filter in web.xml
     * @param <T>
     * @param viewName
     * @param methodName
     * @param params 
     */
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
            String shorterParams = StringUtils.abbreviate(stringParams, 995);
            dataLog.setEmployeeId(activeUser);
            dataLog.setActionDate(new Date());
            dataLog.setMethodName(methodName);
            dataLog.setViewName(viewName);
            dataLog.setParametars(shorterParams);
            dataLogEmployeesFacade.create(dataLog);
        }
    }

}
