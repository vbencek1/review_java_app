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
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.primefaces.component.datatable.DataTable;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.facade.ReviewFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.Book;
import org.vbencek.model.Review;

/**
 *
 * @author Tino
 */
@Named(value = "viewAdminBookReviews")
@ViewScoped
public class ViewAdminBookReviews implements Serializable {
    
    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;
    
    @EJB(beanName = "ReviewFacade")
    ReviewFacadeLocal reviewFacade;

    @Inject
    ActiveUserSession activeUserSession;
    
    @Inject
    Localization localization;
    ResourceBundle res;
    
    @Getter
    @Setter
    Book thisBook;
    
    @Getter
    @Setter
    List<Review> listBookReviews;

    @PostConstruct
    void init() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String bookId = params.get("id");
        int intBookId = 0;
        try {
            intBookId = Integer.parseInt(bookId);
        } catch (Exception e) {
            System.out.println("ViewBookReviews: Can't open view. Redirecting...");
            redirectToReviews();
        }
        if (intBookId != 0) {
            thisBook = bookFacade.find(intBookId);
            if (thisBook != null) {
                System.out.println("ViewBookReviews: Opening view for bookID: " + intBookId);
                listBookReviews=thisBook.getReviewList();
            } else {
                redirectToReviews();
                System.out.println("ViewBookDetails: bookID: " + intBookId + " doesn't exist. Redirecting...");
            }
        }
    }
    
    public void redirectToReviews(){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("adminReviews.xhtml");
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }
    
    public String convertToFriendlyDate(Date date) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return simpleDateFormat.format(date).toUpperCase();
    }
    
    public String translateIsPublic(boolean isPublic){
        if(isPublic){
            return res.getString("combobox.comment.public");
        }else{
            return res.getString("combobox.comment.private");
        }
    }
    
    public void resetvalues(){
        DataTable datatable= (DataTable )FacesContext.getCurrentInstance().getViewRoot().findComponent(":formBookReviews:reviews");
        datatable.reset();
    }
    
    public void deleteReview(Review review){
        reviewFacade.remove(review);
    }
    
}
