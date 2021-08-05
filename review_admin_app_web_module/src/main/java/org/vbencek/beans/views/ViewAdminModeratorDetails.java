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
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
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
@Named(value = "viewAdminModeratorDetails")
@ViewScoped
public class ViewAdminModeratorDetails implements Serializable {

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

    @PostConstruct
    void init() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String employeeId = params.get("id");
        int intEmployeeId = 0;
        try {
            intEmployeeId = Integer.parseInt(employeeId);
        } catch (Exception e) {
            System.out.println("ViewEmployeeDetails: Opening view to insert new employee");
        }
        if (intEmployeeId != 0) {
            thisEmployee = employeeFacade.find(intEmployeeId);
            if (thisEmployee != null) {
                System.out.println("ViewEmployeeDetails: Opening view for employeeID: " + intEmployeeId);   
            } else {
                System.out.println("ViewEmployeeDetails: employeeID: " + intEmployeeId + " doesn't exist. Opening view to insert new employee");
            }
        }
        setData();
    }

    public String setTitle() {
        if (thisEmployee != null) {
            return thisEmployee.getUsername();
        } else {
            return res.getString("admin.viewAdminModeratorDetails.title");
        }
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
    
    private void createEmployee() {
        Employee employee = new Employee();
        employee.setUsername(employeeUsername);
        employee.setFirstname(employeeFirstname);
        employee.setLastname(employeeLastName);
        employee.setEmail(employeeEmail);
        employee.setPassword(employeePassword);
        employee.setEmployeeRoleId(employeeRoleFacade.find(employeeRoleId));
        employee.setIsblocked(Boolean.FALSE);
        employeeFacade.create(employee);
        thisEmployee=employee;
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName(), "EMPLOYEE ID: "+thisEmployee.getEmployeeId());
    }

    private void editEmployee() {
        thisEmployee.setUsername(employeeUsername);
        thisEmployee.setFirstname(employeeFirstname);
        thisEmployee.setLastname(employeeLastName);
        thisEmployee.setEmail(employeeEmail);
        thisEmployee.setPassword(employeePassword);
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
        if (thisEmployee == null) {
            createEmployee();
        } else {
            editEmployee();
        }
        String url = "adminModeratorDetails.xhtml?id=" + thisEmployee.getEmployeeId();
        redirect(url);

    }

    public void refresh() {
        String url;
        if (thisEmployee != null) {
             url = "adminModeratorDetails.xhtml?id=" + thisEmployee.getEmployeeId(); 
        }else{
             url = "adminModeratorDetails.xhtml?";
        }
        redirect(url);
    }

}
