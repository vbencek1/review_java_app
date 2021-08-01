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
import org.vbencek.localization.Localization;
import org.vbencek.model.Book;

/**
 *
 * @author Tino
 */
@Named(value = "viewAdminBookDetails")
@ViewScoped
public class ViewAdminBookDetails implements Serializable {

    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;

    @Inject
    ActiveUserSession activeUserSession;

    @Inject
    Localization localization;
    ResourceBundle res;

    //Atributes
    @Getter
    @Setter
    String bookTitle = "";
    @Getter
    @Setter
    String bookIsbn = "";
    @Getter
    @Setter
    String bookAuthor = "";
    @Getter
    @Setter
    Date bookPublishYear;
    @Getter
    @Setter
    String bookPublisher = "";
    @Getter
    @Setter
    String bookDescription = "";
    @Getter
    @Setter
    String bookLanguage = "";
    @Getter
    @Setter
    String bookPages = "";
    @Getter
    @Setter
    int bookReviewsCount = 0;
    @Getter
    @Setter
    double bookAverageRating = 0;
    @Getter
    @Setter
    String bookImgPath="resources/images/book_placeholder.jpg";

    Book thisBook;

    @PostConstruct
    void init() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String bookId = params.get("id");
        int intBookId = 0;
        try {
            intBookId = Integer.parseInt(bookId);
        } catch (Exception e) {
            System.out.println("ViewBookDetails: Opening view to insert new book");
        }
        if (intBookId != 0) {
            thisBook = bookFacade.find(intBookId);
            if (thisBook != null) {
                System.out.println("ViewBookDetails: Opening view for bookID: " + intBookId);
                setData();
            } else {
                System.out.println("ViewBookDetails: bookID: " + intBookId + " doesn't exist. Opening view to insert new book");
            }
        }
    }

    public String setTitle() {
        if (thisBook != null) {
            return thisBook.getTitle();
        } else {
            return res.getString("admin.viewAdminBooks.newBook").toLowerCase();
        }
    }

    private void setData() {
        if (thisBook != null) {
            bookTitle = thisBook.getTitle();
            bookIsbn = thisBook.getIsbn();
            bookAuthor = thisBook.getAuthors();
            bookPublishYear=thisBook.getPublicationDate();
            bookPublisher = thisBook.getPublisher();
            bookDescription = thisBook.getDescription();
            bookLanguage = thisBook.getLanguageCode();
            bookPages = thisBook.getNumPages();
            bookReviewsCount = thisBook.getRatingsCount();
            bookAverageRating = thisBook.getAverageRating();
            bookImgPath=setIMG();
        }
    }
    
    private String setIMG() {
            if (thisBook.getImgPath() != null) {
                return thisBook.getImgPath();
            } else {
                return "http://covers.openlibrary.org/b/isbn/" + thisBook.getIsbn() + "-M.jpg";
            }
        }
}
