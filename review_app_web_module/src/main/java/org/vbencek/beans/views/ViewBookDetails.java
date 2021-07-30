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
import org.vbencek.facade.CollectionFacadeLocal;
import org.vbencek.facade.ReviewFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.Book;
import org.vbencek.model.Collection;
import org.vbencek.model.CollectionPK;
import org.vbencek.model.Review;
import org.vbencek.model.ReviewPK;
import org.vbencek.model.UserT;
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

    @EJB(beanName = "CollectionFacade")
    CollectionFacadeLocal collectionFacade;

    @Inject
    ActiveUserSession activeUserSession;

    @Inject
    ParamsCaching paramsCaching;

    @Inject
    Localization localization;
    ResourceBundle res;

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

    //add review atrributes
    @Getter
    @Setter
    String addReviewText;

    @Getter
    @Setter
    double addReviewRating;

    @Getter
    @Setter
    boolean addReviewIsPublic = true;

    @Getter
    @Setter
    String reviewOkMsg = "USPJEH";
    @Getter
    @Setter
    String reviewNotOkMsg = "NEUSPJEH";

    @Getter
    @Setter
    boolean renderReviewOkMsg = false;
    @Getter
    @Setter
    boolean renderReviewNotOkMsg = false;

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
            setTitleAndCommentsMsg();
            setFavoriteStatus();
            activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "Opening BookView ID: "+thisBook.getBookId());
        }
        loadProperties();
    }

    private void setTitleAndCommentsMsg() {
        bookName = thisBook.getTitle();
        //get maks number of comments
        numberOfComments = reviewFacade.countReviewsByCriteria(thisBook, null, 0, true, true);
        //set msg if there's no comments
        renderNoCommentMsg = numberOfComments == 0;
    }

    private void setFavoriteStatus() {
        UserT userT = activeUserSession.getActiveUser();
        if (userT != null) {
            notFavorite = !collectionFacade.isBookInCollection(thisBook, userT);
        }
    }

    private void loadProperties() {
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
        return reviewFacade.findReviewsByCriteria(thisBook, null, 0, "", true, true, offset, size);
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

    private void addBookToUserCollection(UserT user) {
        Collection collection=new Collection();
        CollectionPK collectionPK= new CollectionPK();
        collectionPK.setBookId(thisBook.getBookId());
        collectionPK.setUserId(user.getUserId());
        collection.setBook(thisBook);
        collection.setUserT(user);
        collection.setCollectionPK(collectionPK);
        collection.setDateAdded(new Date());
        collectionFacade.create(collection);
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "Book ID: "+thisBook.getBookId());
    }

    private void removeBookFromUserCollection(UserT user) {
        Collection collectionToDelete=collectionFacade.find(new CollectionPK(thisBook.getBookId(),user.getUserId()));
        collectionFacade.remove(collectionToDelete);
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "Book ID: "+thisBook.getBookId());
    }

    public void addBookToCollection() {
        paramsCaching.setBookIdCache(bookID);
        UserT userT = activeUserSession.getActiveUser();
        if (userT != null) {
            if (notFavorite) {
                System.out.println("notFavorite");
                addBookToUserCollection(userT);
                notFavorite=false;
            } else {
                System.out.println("Favorite");
                removeBookFromUserCollection(userT);
                notFavorite=true;
            }
        } else {
            //TO DO -> message for non logged in users
            notFavorite=true;
        }

    }

    private void clearReviewMessages() {
        renderReviewOkMsg = false;
        renderReviewNotOkMsg = false;
        reviewNotOkMsg = "";
        reviewOkMsg = "";
    }

    private void addReviewToDatabase(UserT user) {
        Review review = new Review();
        ReviewPK reviewPK = new ReviewPK();
        reviewPK.setBookId(thisBook.getBookId());
        reviewPK.setUserId(user.getUserId());
        review.setReviewPK(reviewPK);
        review.setBook(thisBook);
        review.setUserT(user);
        review.setDescription(addReviewText);
        review.setIspublic(addReviewIsPublic);
        review.setRating(addReviewRating);
        review.setRatingDate(new Date());
        reviewFacade.create(review);
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "Review ID: "+review.getReviewPK());
        
    }

    public void addReview() {
        paramsCaching.setBookIdCache(bookID);
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        clearReviewMessages();
        UserT activeUser = activeUserSession.getActiveUser();
        if (activeUser != null) {
            if (!reviewFacade.isUserReviewedBook(activeUser, thisBook)) {
                addReviewToDatabase(activeUser);
                renderReviewOkMsg = true;
                renderReviewNotOkMsg = false;
                reviewOkMsg = res.getString("viewBookDetails.addReviewMsg.ok");
            } else {
                renderReviewOkMsg = false;
                renderReviewNotOkMsg = true;
                reviewNotOkMsg = res.getString("viewBookDetails.addReviewMsg.exists");
            }
        } else {
            renderReviewOkMsg = false;
            renderReviewNotOkMsg = true;
            reviewNotOkMsg = res.getString("viewBookDetails.addReviewMsg.login");
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
