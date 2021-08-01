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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.datatable.DataTable;
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.model.Book;

/**
 *
 * @author Tino
 */
@Named(value = "viewAdminBooks")
@ViewScoped
public class ViewAdminBooks implements Serializable {

    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;

    @Getter
    @Setter
    List<Book> listBooks;
    
    @Getter
    @Setter
    Book selectedBook;

    @PostConstruct
    public void init() {
        //samo za testiranje, iskoristiti serversko stranicenje
        listBooks = bookFacade.findAll();
    }

    public String convertDateToYear(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
        return simpleDateFormat.format(date).toUpperCase();
    }

    public String makeTextShorter(String text, int maxChars) {
        return StringUtils.abbreviate(text, maxChars);
    }
    
    public void redirectToBookDetails(){
        try {
            System.out.println("test"+selectedBook.getBookId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("adminBookDetails.xhtml?id=" + selectedBook.getBookId());
        } catch (IOException ex) {
            Logger.getLogger(ViewAdminBooks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void resetvalues(){
        DataTable datatable= (DataTable )FacesContext.getCurrentInstance().getViewRoot().findComponent(":formBooks:books");
        datatable.reset();
    }

}
