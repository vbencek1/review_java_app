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
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.beans.requests.UpdateBook;
import org.vbencek.facade.ReviewFacadeLocal;
import org.vbencek.model.Review;
import org.vbencek.properties.ParamsCaching;
import org.vbencek.properties.PropertiesLoader;

/**
 * View that shows user review
 *
 * @author vbencek
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

    @Inject
    UpdateBook updateBook;

    @Getter
    @Setter
    String keyword = "";

    @Getter
    @Setter
    double minRating;

    @Getter
    @Setter
    Boolean isPublic = null;

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
            numberOfReviews = reviewFacade.countMyReviewsByCriteria(activeUserSession.getActiveUser(), keyword, minRating, isPublic);
        }

    }

    private void setSearchParams() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        keyword = params.get("Keyword") != null ? params.get("Keyword") : "";
        String strIsPublic = params.get("IsPublic");
        if (strIsPublic != null && strIsPublic.equals("true")) {
            isPublic = true;
        } else if (strIsPublic != null && strIsPublic.equals("false")) {
            isPublic = false;
        } else {
            isPublic = null;
        }
        sortOption = params.get("SortBy");
        try {
            minRating = Double.parseDouble(params.get("MinRating"));
        } catch (Exception e) {
            minRating = 0;
        }
        try {
            pageNum = Integer.parseInt(params.get("PageNum"));
        } catch (Exception e) {
            pageNum = 0;
        }
        String stringParams = "ViewSearchBooks: Opening view with parametars: "
                + "Keyword: " + keyword + " "
                + "MinRating: " + minRating + " "
                + "isPublic: " + isPublic + " "
                + "SortBy: " + sortOption;
        System.out.println(stringParams);
        setParamsForPagination();
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName(), stringParams);

    }

    private void setParamsForPagination() {
        String url = "myReviews.xhtml?Keyword=" + keyword
                + "&MinRating=" + minRating
                + "&IsPublic=" + isPublic
                + "&SortBy=" + sortOption;
        paramsCaching.setNavigationUrl(url);
    }

    public List<Review> getListOfReviews() {
        int offset = pageNum * maksReviewsPerPage;
        int size = offset + maksReviewsPerPage;
        return reviewFacade.findMyReviewsByCriteria(activeUserSession.getActiveUser(), keyword, minRating, isPublic, sortOption, offset, size);
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
    
    public String showPaginationInfo(){
        int offset = pageNum * maksReviewsPerPage;
        int size = offset + maksReviewsPerPage;
        if(size>numberOfReviews){
            int diff=(int) (size-numberOfReviews);
            size=size-diff;
        }
        return offset+" - "+size+" (Maks: "+numberOfReviews+")";
    }

    public void redirectToUrl(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }

    public void loadNextComments() {
        if (pageNum < numberOfReviews / maksReviewsPerPage) {
            pageNum++;
            String url = paramsCaching.getNavigationUrl()
                    + "&PageNum=" + pageNum;
            redirectToUrl(url);
        }
    }

    public void loadPreviousComments() {
        if (pageNum > 0) {
            pageNum--;
            String url = paramsCaching.getNavigationUrl()
                    + "&PageNum=" + pageNum;
            redirectToUrl(url);
        }
    }

    public String redirectToBookDetails(int bookId) {
        paramsCaching.setBookIdCache(bookId);
        return "bookDetails.xhtml?id=" + bookId + "&faces-redirect=true";
    }

    public void saveData(Review review) {
        review.setRatingDate(new Date());
        Review oldReview = reviewFacade.findReviewByBookAndUser(review.getUserT(), review.getBook());
        reviewFacade.edit(review);
        updateBook.updateRatings(review, "UPDATE", oldReview.getRating());
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName(), "Review ID: " + review.getReviewPK());
        String url = paramsCaching.getNavigationUrl()
                + "&PageNum=" + pageNum;
        redirectToUrl(url);
    }

    public void deleteData(Review review) {
        reviewFacade.remove(review);
        updateBook.updateRatings(review, "DELETE");
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName(), "Review ID: " + review.getReviewPK());
        String url = paramsCaching.getNavigationUrl()
                + "&PageNum=" + pageNum;
        redirectToUrl(url);
    }

}
