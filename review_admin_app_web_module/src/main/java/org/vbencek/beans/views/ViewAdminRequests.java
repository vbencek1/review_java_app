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
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.facade.RequestFacadeLocal;
import org.vbencek.model.Request;

/**
 *
 * @author Tino
 */
@Named(value = "viewAdminRequests")
@ViewScoped
public class ViewAdminRequests implements Serializable {

    @EJB(beanName = "RequestFacade")
    RequestFacadeLocal requestFacade;

    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;

    @Getter
    @Setter
    List<Request> listRequestIsbn;

    @Getter
    @Setter
    List<Request> listRequestInfo;

    @Getter
    @Setter
    boolean renderRequestIsbn = true;

    @Getter
    @Setter
    boolean renderRequestInfo = false;

    @Getter
    @Setter
    String option = "2";

    @PostConstruct
    public void init() {
        System.out.println("ViewAdminRequests: opening view");
        listRequestIsbn = requestFacade.findRequestsByISBNExists(true);
        listRequestInfo = requestFacade.findRequestsByISBNExists(false);
    }

    public void changeView() {
        if ("2".equals(option)) {
            renderRequestIsbn = false;
            renderRequestInfo = true;
        } else {
            renderRequestInfo = false;
            renderRequestIsbn = true;
        }
    }

    public String convertToFriendlyDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date).toUpperCase();
    }

    public boolean isISBNExists(Request request) {
        return bookFacade.isISBNExists(request.getIsbn());
    }

    public void resetvalues() {
        DataTable datatable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":formRequests:reqIsbn");
        datatable.reset();
        DataTable datatable2 = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":formRequests:reqInfo");
        datatable2.reset();
    }

}
