/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.views;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.component.datatable.DataTable;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.facade.EmployeeFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.Employee;

/**
 * View that shows all mods/admins
 * @author vbencek
 */
@Named(value = "viewAdminModerators")
@ViewScoped
public class ViewAdminModerators implements Serializable{

    @EJB(beanName = "EmployeeFacade")
    EmployeeFacadeLocal employeeFacade;
    
    @Inject
    ActiveUserSession activeUserSession;
    
    @Inject
    Localization localization;
    ResourceBundle res;
    
    @Getter
    @Setter
    List<Employee> listEmployee;
    
    @Getter
    @Setter
    Employee selectedEmployee;
    
    @Getter
    @Setter
    private SelectItem[] blockedOptions = new SelectItem[3];

    @PostConstruct
    public void init() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        listEmployee = employeeFacade.findAll();
        blockedOptions[0] = new SelectItem("", res.getString("admin.viewAdminModerators.combo.all"));
        blockedOptions[1] = new SelectItem(Boolean.TRUE.toString(), res.getString("admin.viewAdminModerators.combo.blocked"));
        blockedOptions[2] = new SelectItem(Boolean.FALSE.toString(), res.getString("admin.viewAdminModerators.combo.notBlocked"));
    }
    
    public void unblockUser(Employee employee){
        employee.setIsblocked(Boolean.FALSE);
        employeeFacade.edit(employee);
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "EMPLOYEE ID: "+employee.getEmployeeId());
    }
    
    public void blockUser(Employee employee){
        employee.setIsblocked(Boolean.TRUE);
        employeeFacade.edit(employee);
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "EMPLOYEE ID: "+employee.getEmployeeId());
    }
    
    public void redirectToModDetails(){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("adminModeratorDetails.xhtml?id=" + selectedEmployee.getEmployeeId());
        } catch (IOException ex) {
            System.out.println("ViewAdminModerator: can't redirect");
        }
    }
    
    public void resetvalues() {
        DataTable datatable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":formEmployees:employees");
        datatable.reset();
    }
    
}
