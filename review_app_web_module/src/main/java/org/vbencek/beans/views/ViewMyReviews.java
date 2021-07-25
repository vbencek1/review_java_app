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
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.beans.TestnaKlasaKomentar;
import org.vbencek.facade.ReviewFacadeLocal;
import org.vbencek.model.Review;
import org.vbencek.properties.ParamsCaching;
import org.vbencek.properties.PropertiesLoader;

/**
 *
 * @author Tino
 */
@Named(value = "viewMyReviews")
@ViewScoped
public class ViewMyReviews implements Serializable {

    @EJB(beanName = "ReviewFacade")
    ReviewFacadeLocal reviewFacade;

    @Inject
    ActiveUserSession activeUserSession;

    @Inject
    ParamsCaching paramsCaching;

    @Getter
    @Setter
    String keyword;

    @Getter
    @Setter
    double minRating;

    @Getter
    @Setter
    String sortOption;

    @Getter
    @Setter
    boolean renderRedirect = false;

    @Getter
    @Setter
    String redirectFunction = "";

    @Getter
    @Setter
    int pageNum = 0;

    int maksReviewsPerPage;
    long numberOfReviews;

    @PostConstruct
    void init() {
        if (activeUserSession.getActiveUser() == null) {
            redirectFunction = "index.xhtml?action=openLogin';";
            renderRedirect = true;
        } else {
            System.out.println("Opening view: ViewMyReviews");
            setSearchParams();
            PropertiesLoader propLoader = new PropertiesLoader();
            try {
                maksReviewsPerPage = Integer.parseInt(propLoader.getProperty("myReviews.maxReviewsPerPage"));
            } catch (NumberFormatException e) {
                maksReviewsPerPage = 10;
            }
            numberOfReviews = reviewFacade.countMyReviewsByCriteria(activeUserSession.getActiveUser(), keyword, minRating);
        }

    }

    private void setSearchParams() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        keyword = params.get("Keyword");
        sortOption = params.get("SortBy");
        try {
            minRating = Double.parseDouble(params.get("MinRating"));
        } catch (Exception e) {
            minRating = 0;
        }
        System.out.println("ViewSearchBooks: Opening view with parametars: "
                + "Keyword: " + keyword + " "
                + "MinRating: " + minRating + " "
                + "SortBy: " + sortOption);

    }

    public List<Review> getListOfReviews(int page) {
        int offset = page * maksReviewsPerPage;
        int size = offset + maksReviewsPerPage;
        return reviewFacade.findMyReviewsByCriteria(activeUserSession.getActiveUser(), keyword, minRating, sortOption, offset, size);
    }

    public String setIMG(Review review) {
        if (review.getBook().getImgPath() != null) {
            return review.getBook().getImgPath();
        } else {
            return "http://covers.openlibrary.org/b/isbn/" + review.getBook().getIsbn() + "-S.jpg";
        }

    }

    public String convertToFriendlyDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date).toUpperCase();
    }

    public void loadNextComments() {
        if (pageNum < numberOfReviews / maksReviewsPerPage) {
            pageNum++;
        }
    }

    public void loadPreviousComments() {
        if (pageNum > 0) {
            pageNum--;
        }
    }

    public String redirectToBookDetails(int bookId) {
        paramsCaching.setBookIdCache(bookId);
        return "bookDetails.xhtml?id=" + bookId + "&faces-redirect=true";
    }

    public void saveData(Review review) {
        review.setRatingDate(new Date());
        reviewFacade.edit(review);
    }
    public void deleteData(Review review) {
        reviewFacade.remove(review);
    }

}
