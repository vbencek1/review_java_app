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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.datatable.DataTable;
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.model.Book;
import org.vbencek.model.Review;

/**
 *
 * @author Tino
 */
@Named(value = "viewAdminReviews")
@ViewScoped
public class ViewAdminReviews implements Serializable {
@EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;

    @Getter
    @Setter
    List<Book> listBooksWithReviews;
    
    @Getter
    @Setter
    Book selectedBook;
    
    @PostConstruct
    public void init() {
        //samo za testiranje, iskoristiti serversko stranicenje
        listBooksWithReviews = bookFacade.findBooksWithReviews();
    }
    
    public String makeTextShorter(String text, int maxChars) {
        return StringUtils.abbreviate(text, maxChars);
    }
    
    public String convertToFriendlyDate(Date date) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return simpleDateFormat.format(date).toUpperCase();
    }
    
    public Date getLatestReview(Book book){
        List<Review> bookReviews=book.getReviewList();
        bookReviews.sort(Comparator.comparing(Review::getRatingDate).reversed());
        Date latestDate=bookReviews.get(0).getRatingDate(); 
        return latestDate;
    }
    
    public void redirectToBookDetails(){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("adminBookReviews.xhtml?id=" + selectedBook.getBookId());
        } catch (IOException ex) {
            Logger.getLogger(ViewAdminBooks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void resetvalues(){
        DataTable datatable= (DataTable )FacesContext.getCurrentInstance().getViewRoot().findComponent(":formReviews:books");
        datatable.reset();
    }
    
}
