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
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.facade.ReviewFacadeLocal;
import org.vbencek.model.Book;
import org.vbencek.model.Review;
import org.vbencek.properties.ParamsCaching;
import org.vbencek.properties.PropertiesLoader;

/**
 *
 * @author Tino
 */
@Named(value = "viewBookDetails")
@ViewScoped
public class ViewBookDetails implements Serializable {

    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;

    @EJB(beanName = "ReviewFacade")
    ReviewFacadeLocal reviewFacade;

    @Inject
    ParamsCaching paramsCaching;

    @Getter
    @Setter
    int bookID = 0;

    @Getter
    @Setter
    Book thisBook;

    @Getter
    @Setter
    String bookName = "";

    @Getter
    @Setter
    boolean notFavorite = true;

    @Getter
    @Setter
    int pageNum = 0;

    int maksCommentsPerPage;

    @Getter
    @Setter
    boolean renderRedirect = false;

    @Getter
    @Setter
    String redirectFunction = "";
    
    @Getter
    @Setter
    boolean renderNoCommentMsg = false;

    long numberOfComments;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String bookid = params.get("id");
        if (bookid == null) {
            bookid = String.valueOf(paramsCaching.getBookIdCache());
        }
        System.out.println("ViewBookDetails: Opening view for bookID: " + bookid);
        try {
            bookID = Integer.parseInt(bookid);
            thisBook = bookFacade.find(bookID);
        } catch (NumberFormatException e) {
            redirectFunction = "location.href = 'index.xhtml';";
            renderRedirect = true;
        }
        if (thisBook == null) {
            redirectFunction = "location.href = 'index.xhtml';";
            renderRedirect = true;
        } else {
            bookName = thisBook.getTitle();
            //get maks number of comments
            numberOfComments = reviewFacade.countReviewsByCriteria(thisBook, null, 0,true,true);
            renderNoCommentMsg = numberOfComments==0;
        }
        PropertiesLoader propLoader = new PropertiesLoader();
        try {
            maksCommentsPerPage = Integer.parseInt(propLoader.getProperty("details.maxCommentsPerPage"));
        } catch (NumberFormatException e) {
            maksCommentsPerPage = 5;
        }
    }

    public String setIMG() {
        if (thisBook != null) {
            if (thisBook.getImgPath() != null) {
                return thisBook.getImgPath();
            } else {
                return "http://covers.openlibrary.org/b/isbn/" + thisBook.getIsbn() + "-L.jpg";
            }
        }
        return "";
    }

    public String convertDateToYear(Date date) {
        if (thisBook != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
            simpleDateFormat = new SimpleDateFormat("YYYY");
            return simpleDateFormat.format(date).toUpperCase();
        }
        return "";
    }
    
    public String convertToFriendlyDate(Date date) {
        if (thisBook != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return simpleDateFormat.format(date).toUpperCase();
        }
        return "";
    }

    public List<Review> getListOfComments(int page) {
        int offset = page * maksCommentsPerPage;
        int size = offset + maksCommentsPerPage;
        return reviewFacade.findReviewsByCriteria(thisBook, null, 0, "",true,true, offset, size);
    }

    public void loadNextComments() {
        if (pageNum < numberOfComments / maksCommentsPerPage) {
            pageNum++;
        }

    }

    public void loadPreviousComments() {
        if (pageNum > 0) {
            pageNum--;
        }

    }

    public void addBookToCollection() {

        if (notFavorite) {
            System.out.println("notFavorite");
        } else {
            System.out.println("Favorite");
        }
    }

    public String convertToWidth(Double rating) {
        if (thisBook != null) {
            String width = "0%";
            Double convert = (rating / 5) * 100;
            int roundNum = (int) Math.round(convert);
            width = String.valueOf(roundNum) + "%";
            return width;
        }
        return "0";
    }

}
