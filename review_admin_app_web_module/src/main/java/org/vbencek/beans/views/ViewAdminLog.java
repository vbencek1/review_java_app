/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.views;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.component.datatable.DataTable;
import org.vbencek.facade.DataLogEmployeesFacadeLocal;
import org.vbencek.facade.DataLogFacadeLocal;
import org.vbencek.model.DataLog;
import org.vbencek.model.DataLogEmployees;

/**
 * View that shows user actions (Data Log)
 * @author vbencek
 */
@Named(value = "viewAdminLog")
@ViewScoped
public class ViewAdminLog implements Serializable {

    @EJB(beanName = "DataLogFacade")
    DataLogFacadeLocal dataLogFacade;
    
    @EJB(beanName = "DataLogEmployeesFacade")
    DataLogEmployeesFacadeLocal dataLogEmployeesFacade;

    @Getter
    @Setter
    List<DataLog> listDataLog;
    
    @Getter
    @Setter
    List<DataLogEmployees> listDataLogEmployee;
    
    @Getter
    @Setter
    boolean renderUserLog=true;
    
    @Getter
    @Setter
    boolean renderEmployeeLog=false;
    
    @Getter
    @Setter
    String option="2";
    
    @PostConstruct
    public void init() {
        System.out.println("ViewAdminLog: opening view");
        listDataLog = dataLogFacade.findAll();
        listDataLogEmployee=dataLogEmployeesFacade.findAll();
    }
    
    /**
     * Switched between admin/mod log and app user log
     */
    public void changeView(){
        if("2".equals(option)){
            renderUserLog=false;
            renderEmployeeLog=true;
        }else{
            renderEmployeeLog=false;
            renderUserLog=true;
        }
    }
    
    public String convertToFriendlyDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return simpleDateFormat.format(date).toUpperCase();
    }
    
    public void resetvalues(){
        DataTable datatable= (DataTable )FacesContext.getCurrentInstance().getViewRoot().findComponent(":formLog:logs");
        datatable.reset();
        DataTable datatable2= (DataTable )FacesContext.getCurrentInstance().getViewRoot().findComponent(":formLog:logsEmployee");
        datatable2.reset();
    }
    
}
