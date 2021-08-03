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
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.beans.requests.ViewFileUpload;
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

    @Inject
    ViewFileUpload fileUpload;

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
    String bookImgPath = "resources/images/book_placeholder.jpg";

    @Getter
    @Setter
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
            bookPublishYear = thisBook.getPublicationDate();
            bookPublisher = thisBook.getPublisher();
            bookDescription = thisBook.getDescription();
            bookLanguage = thisBook.getLanguageCode();
            bookPages = thisBook.getNumPages();
            bookReviewsCount = thisBook.getRatingsCount();
            bookAverageRating = thisBook.getAverageRating();
            bookImgPath = setIMG();
        }
    }

    private String setIMG() {
        if (thisBook.getImgPath() != null) {
            System.out.println(thisBook.getImgPath());
            return thisBook.getImgPath();
        } else {
            return "http://covers.openlibrary.org/b/isbn/" + thisBook.getIsbn() + "-M.jpg";
        }
    }

    private void setUnknown() {
        if (bookTitle == null || bookTitle.length() == 0) {
            bookTitle = "UNKNOWN";
        }
        if (bookAuthor == null || bookAuthor.length() == 0) {
            bookAuthor = "UNKNOWN";
        }
        if (bookIsbn == null || bookIsbn.length() == 0) {
            bookIsbn = "UNKNOWN";
        }
        if (bookPublisher == null || bookPublisher.length() == 0) {
            bookPublisher = "UNKNOWN";
        }
        if (bookLanguage == null || bookLanguage.length() == 0) {
            bookLanguage = "UNKNOWN";
        }
        if (bookPages == null || bookPages.length() == 0) {
            bookPages = "UNKNOWN";
        }

    }

    private void createBook() {
        setUnknown();
        Book newBook = new Book();
        newBook.setTitle(bookTitle);
        newBook.setAuthors(bookAuthor);
        newBook.setDescription(bookDescription);
        newBook.setIsbn(bookIsbn);
        newBook.setIsbn13("UNKNOWN");
        newBook.setPublisher(bookPublisher);
        newBook.setPublicationDate(bookPublishYear);
        newBook.setLanguageCode(bookLanguage);
        newBook.setNumPages(bookPages);
        if (fileUpload.getFile() != null) {
            newBook.setImgPath("resources/images/covers/" + fileUpload.getFile().getFileName());
        }
        bookFacade.create(newBook);
        thisBook = newBook;
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName(), newBook);
    }

    private void editBook() {
        setUnknown();
        thisBook.setTitle(bookTitle);
        thisBook.setAuthors(bookAuthor);
        thisBook.setDescription(bookDescription);
        thisBook.setIsbn(bookIsbn);
        thisBook.setIsbn13("UNKNOWN");
        thisBook.setPublisher(bookPublisher);
        thisBook.setPublicationDate(bookPublishYear);
        thisBook.setLanguageCode(bookLanguage);
        thisBook.setNumPages(bookPages);
        if (fileUpload.getFile() != null) {
            thisBook.setImgPath("resources/images/covers/" + fileUpload.getFile().getFileName());
        }
        bookFacade.edit(thisBook);
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName(), "EDIT BOOKID: " + thisBook.getBookId());
    }

    private void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void redirect(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void saveData() {
        fileUpload.handleFileUpload();
        if (thisBook == null) {
            createBook();
        } else {
            editBook();
        }
        String url = "adminBookDetails.xhtml?id=" + thisBook.getBookId();
        redirect(url);

    }

    public void removeBook() {
        bookFacade.remove(thisBook);
        addMessage("Confirmed", "Record deleted");
        refresh();
    }

    public void refresh() {
        String url;
        if (thisBook != null) {
             url = "adminBookDetails.xhtml?id=" + thisBook.getBookId(); 
        }else{
             url = "adminBookDetails.xhtml?";
        }
        redirect(url);
    }
}
