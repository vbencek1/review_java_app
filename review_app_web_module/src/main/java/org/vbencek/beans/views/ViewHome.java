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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.beans.TestnaKlasaObrisati;
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.model.Book;
import org.vbencek.properties.ParamsCaching;

/**
 *
 * @author Tino
 */
@Named(value = "viewHome")
@ViewScoped
public class ViewHome implements Serializable {

    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;

    @Inject
    ActiveUserSession activeUserSession;

    @Inject
    ParamsCaching paramsCaching;

    @PostConstruct
    public void init() {
        //DO NOTHING FOR NOW
    }

    public String convertDateToYear(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        simpleDateFormat = new SimpleDateFormat("YYYY");
        return simpleDateFormat.format(date).toUpperCase();
    }

    public String setIMG(Book book) {
        if (book.getImgPath() != null) {
            return book.getImgPath();
        } else {
            return "http://covers.openlibrary.org/b/isbn/" + book.getIsbn() + "-M.jpg";
        }
    }

    public String makeTextShorter(String text, int maxChars) {
        return StringUtils.abbreviate(text, maxChars);
    }

    public List<Book> getRecomendedbooks() {
        if (activeUserSession.getActiveUser() == null) {
            Random rand = new Random();
            int rndOffset = rand.nextInt(100);
            return  bookFacade.findBooksByCriteria("", "", 0, "", 0, "rating", rndOffset, 2);
        } else {
            //TO DO za sad nek bu defaultno
            Random rand = new Random();
            int rndOffset = rand.nextInt(100);
            return  bookFacade.findBooksByCriteria("", "", 0, "", 0, "rating", rndOffset, 2);
        }
    }

    public List<Book> getPopularbooks(int startingFrom, int totalNumber) {
        return bookFacade.findBooksByCriteria("", "", 0, "", 0, "reviews", startingFrom, totalNumber);
    }
    
    public List<Book> getLatestbooks(int startingFrom, int totalNumber) {
        return bookFacade.findBooksByCriteria("", "", 0, "", 0, "date", startingFrom, totalNumber);
    }

    public String redirectToBookDetails(int bookId) {
        paramsCaching.setBookIdCache(bookId);
        return "bookDetails.xhtml?id=" + bookId + "&faces-redirect=true";
    }

}
