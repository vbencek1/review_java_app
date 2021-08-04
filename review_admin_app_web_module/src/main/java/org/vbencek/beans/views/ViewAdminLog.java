/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.views;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
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
 *
 * @author Tino
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
    String option="0";
    
    @PostConstruct
    public void init() {
        //samo za testiranje, iskoristiti serversko stranicenje
        listDataLog = dataLogFacade.findAll();
        listDataLogEmployee=dataLogEmployeesFacade.findAll();
    }
    
    public void changeView(){
        if("2".equals(option)){
            renderUserLog=false;
            renderEmployeeLog=true;
            System.out.println("test1");
        }else{
            renderEmployeeLog=false;
            renderUserLog=true;
            System.out.println("test2");
        }
    }
    
    public void resetvalues(){
        DataTable datatable= (DataTable )FacesContext.getCurrentInstance().getViewRoot().findComponent(":formLog:logs");
        datatable.reset();
        DataTable datatable2= (DataTable )FacesContext.getCurrentInstance().getViewRoot().findComponent(":formLog:logsEmployee");
        datatable2.reset();
    }
    
}
