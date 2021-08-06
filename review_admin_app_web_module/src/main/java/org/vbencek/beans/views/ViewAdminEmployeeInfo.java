/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.views;

import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.facade.EmployeeFacadeLocal;
import org.vbencek.facade.EmployeeRoleFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.Employee;
import org.vbencek.model.EmployeeRole;

/**
 *
 * @author Tino
 */
@Named(value = "viewAdminEmployeeInfo")
@ViewScoped
public class ViewAdminEmployeeInfo implements Serializable {

    @EJB(beanName = "EmployeeFacade")
    EmployeeFacadeLocal employeeFacade;

    @EJB(beanName = "EmployeeRoleFacade")
    EmployeeRoleFacadeLocal employeeRoleFacade;

    @Inject
    ActiveUserSession activeUserSession;

    @Inject
    Localization localization;
    ResourceBundle res;

    //Atributes
    @Getter
    @Setter
    String employeeUsername = "";
    @Getter
    @Setter
    String employeeFirstname = "";
    @Getter
    @Setter
    String employeeLastName = "";
    @Getter
    @Setter
    String employeeEmail = "";
    @Getter
    @Setter
    String employeePassword = "";
    @Getter
    @Setter
    int employeeRoleId=2;

    @Getter
    @Setter
    Employee thisEmployee;
    
    @Getter
    @Setter
    List<EmployeeRole> listEmployeeRole;
    
    public static Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();

    @PostConstruct
    void init() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        thisEmployee=activeUserSession.getActiveUser();
        System.out.println("ViewEmployeeDetails: Opening view for employeeID: " + thisEmployee.getEmployeeId());
        setData();
    }

    public String setTitle() {
        if (thisEmployee != null) {
            return thisEmployee.getUsername();
        } 
        return "";
    }

    private void setData() {
        listEmployeeRole=employeeRoleFacade.findAll();
        if (thisEmployee != null) {
            employeeUsername = thisEmployee.getUsername();
            employeeFirstname = thisEmployee.getFirstname();
            employeeLastName = thisEmployee.getLastname();
            employeeEmail = thisEmployee.getEmail();
            employeePassword = "";
            employeeRoleId = thisEmployee.getEmployeeRoleId().getEmployeeRoleId();
        }
    }

    private void editEmployee() {
        String hashPassword = pbkdf2PasswordEncoder.encode(employeePassword);
        thisEmployee.setUsername(employeeUsername);
        thisEmployee.setFirstname(employeeFirstname);
        thisEmployee.setLastname(employeeLastName);
        thisEmployee.setEmail(employeeEmail);
        thisEmployee.setPassword(hashPassword);
        thisEmployee.setEmployeeRoleId(employeeRoleFacade.find(employeeRoleId));
        employeeFacade.edit(thisEmployee);
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName(), "EMPLOYEE ID: "+thisEmployee.getEmployeeId());
    }

    private void redirect(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void saveData() {
        editEmployee();
        String url = "adminEmployeeInfo.xhtml";
        redirect(url);

    }

    public void refresh() {
        String  url = "adminEmployeeInfo.xhtml";
        redirect(url);
    }
    
}
