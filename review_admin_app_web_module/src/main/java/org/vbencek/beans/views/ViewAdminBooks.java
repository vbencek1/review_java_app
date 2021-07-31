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
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
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

}
