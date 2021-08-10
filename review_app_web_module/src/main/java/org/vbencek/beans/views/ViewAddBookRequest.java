/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.views;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
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
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.facade.DataLogFacadeLocal;
import org.vbencek.facade.RequestFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.Request;

/**
 * View for adding new book request
 * @author vbencek
 */
@Named(value = "viewAddBookRequest")
@ViewScoped
public class ViewAddBookRequest implements Serializable {
    
    @EJB(beanName = "RequestFacade")
    RequestFacadeLocal requestFacade;
    
    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;
    
    @EJB(beanName = "DataLogFacade")
    DataLogFacadeLocal dataLogFacade;
    
    @Inject
    ActiveUserSession activeUserSession;

    @Inject
    Localization localization;
    ResourceBundle res;

    @Getter
    @Setter
    String isbn;

    @Getter
    @Setter
    String bookTitle;

    @Getter
    @Setter
    String bookDescription;

    @Getter
    @Setter
    boolean renderReplyIsbn = false;

    @Getter
    @Setter
    boolean renderReplyInfo = false;

    @Getter
    @Setter
    String messageIsbn;

    @Getter
    @Setter
    String messageInfo;

    @Getter
    @Setter
    boolean renderRedirect = false;

    @Getter
    @Setter
    String redirectFunction = "";

    @PostConstruct
    void init() {
        if (activeUserSession.getActiveUser() == null) {
            redirectFunction = "location.href = 'index.xhtml?action=openLogin';";
            renderRedirect = true;
        } else {
            setParams();
        }
    }

    private void setParams() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        isbn = params.get("ISBN");
        bookTitle = params.get("BookTitle");
        bookDescription = params.get("BookDescription");
        System.out.println("ViewAddBookCollection: Opening view with parametars: "
                + "ISBN: " + isbn + " "
                + "BookTitle: " + bookTitle + " "
                + "BookDescription: " + bookDescription);
        if (isbn != null) {
            checkEmptyISBNandSend();

        } else if (bookTitle != null) {
            checkEmptyInfoandSend();
        }
    }

    /**
     * Method that checks if isbn isn't empty or not equal to length of 10. IF isbn is in correct format: send request.
     */
    private void checkEmptyISBNandSend() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        if (!"".equals(isbn.trim()) && isbn.length()==10) {
            sendISBNBookRequest();
        } else {
            renderReplyIsbn = true;
            messageIsbn =res.getString("viewAddBookRequest.messageRequiredISBN");
        }
    }
    
    /**
     * Method that checks if title isn't empty . IF title is in correct format: send request.
     */
    private void checkEmptyInfoandSend() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        if (!"".equals(bookTitle.trim())) {
            sendInfoBookRequest();
        } else {
            renderReplyInfo = true;
            messageInfo = res.getString("viewAddBookRequest.messageRequiredInfo");
        }
    }

    /**
     * Method checks if ISBN already exist in DB. if exists: dont add to DB, otherwise, add request
     */
    private void sendISBNBookRequest() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        renderReplyIsbn = true;
        if (!bookFacade.isISBNExists(isbn)) {
            messageIsbn = res.getString("viewAddBookRequest.messageISBN.ok");
            addRequest();
        } else {
            messageIsbn = res.getString("viewAddBookRequest.messageISBN.notOk");
        }
    }

    private void sendInfoBookRequest() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        renderReplyInfo = true;
        messageInfo = res.getString("viewAddBookRequest.messageInfo.ok");
        addRequest();
    }
    
    /**
     * Method adds book request to DB
     */
    private void addRequest(){
        Request request= new Request();
        request.setIsbn(isbn);
        request.setTitle(bookTitle);
        request.setDescription(bookDescription);
        request.setRequestDate(new Date());
        //Sending object to be converted to json before I add user to avoid infinite loop. Alternative is to use  @JsonIgnoreProperties or similar anotation
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), request);
        request.setUserId(activeUserSession.getActiveUser());
        requestFacade.create(request);
    }
}
