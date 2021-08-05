/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.views;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.component.datatable.DataTable;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.facade.UserTFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.UserT;

/**
 *
 * @author Tino
 */
@Named(value = "viewAdminUsers")
@ViewScoped
public class ViewAdminUsers implements Serializable {

    @EJB(beanName = "UserTFacade")
    UserTFacadeLocal userTFacade;
    
    @Inject
    ActiveUserSession activeUserSession;
    
    @Inject
    Localization localization;
    ResourceBundle res;
    
    @Getter
    @Setter
    List<UserT> listUsers;

    @Getter
    @Setter
    private SelectItem[] blockedOptions = new SelectItem[3];

    @PostConstruct
    public void init() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        listUsers = userTFacade.findAll();
        blockedOptions[0] = new SelectItem("", res.getString("admin.viewAdminUsers.combo.all"));
        blockedOptions[1] = new SelectItem(Boolean.TRUE.toString(), res.getString("admin.viewAdminUsers.combo.blocked"));
        blockedOptions[2] = new SelectItem(Boolean.FALSE.toString(), res.getString("admin.viewAdminUsers.combo.notBlocked"));
    }
    
    public void unblockUser(UserT user){
        user.setIsblocked(Boolean.FALSE);
        userTFacade.edit(user);
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "USER ID: "+user.getUserId());
    }
    
    public void blockUser(UserT user){
        user.setIsblocked(Boolean.TRUE);
        userTFacade.edit(user);
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "USER ID: "+user.getUserId());
    }
    
    public void resetvalues() {
        DataTable datatable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":formUsers:users");
        datatable.reset();
    }

}
