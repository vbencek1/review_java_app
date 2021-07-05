/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.views;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.beans.TestnaKlasaKomentar;
import org.vbencek.properties.ParamsCaching;
import org.vbencek.properties.PropertiesLoader;

/**
 *
 * @author Tino
 */
@Named(value = "viewMyReviews")
@ViewScoped
public class ViewMyReviews implements Serializable {

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
    boolean renderRedirect = false;

    @Getter
    @Setter
    String redirectFunction = "";

    @Getter
    @Setter
    int pageNum = 0;
    List<TestnaKlasaKomentar> komentari = new ArrayList<>();

    int maksReviewsPerPage;

    @PostConstruct
    void init() {
        if (activeUserSession.getActiveUser() == null) {
            redirectFunction = "location.href = 'index.xhtml';";
            renderRedirect = true;
        } else {
            setSearchParams();
            PropertiesLoader propLoader = new PropertiesLoader();
            try {
                maksReviewsPerPage = Integer.parseInt(propLoader.getProperty("myReviews.maxReviewsPerPage"));
            } catch (NumberFormatException e) {
                maksReviewsPerPage = 10;
            }

            komentari.add(new TestnaKlasaKomentar(1, "Knjiga 1", "Smece obicno ne valja nista", 1));
            komentari.add(new TestnaKlasaKomentar(2, "Knjiga 2", "Smece obicno ne valja nista", 1));
            komentari.add(new TestnaKlasaKomentar(2, "Knjiga 3", "Smece obicno ne valja nista", 1));
            komentari.add(new TestnaKlasaKomentar(3, "Knjiga 4", "Smece obicno ne valja nista", 1));
            komentari.add(new TestnaKlasaKomentar(4, "Knjiga 5", "Smece obicno ne valja nista", 1));
            komentari.add(new TestnaKlasaKomentar(5, "Knjiga 6", "Smece obicno ne valja nista", 1));
            komentari.add(new TestnaKlasaKomentar(5, "Knjiga 7", "Smece obicno ne valja nista", 1));
            komentari.add(new TestnaKlasaKomentar(6, "Knjiga K 8", "Mnogo dobra knjiga preporučila bih svima", 5));
            komentari.add(new TestnaKlasaKomentar(6, "Knjiga  9", "Volim tu knjigu mmm", 4));
            komentari.add(new TestnaKlasaKomentar(6, "Knjiga  10", "Dobro je, prosjek, likovi dosadni malo zzz", 3));
            komentari.add(new TestnaKlasaKomentar(6, "Knjiga  11", "Dobro je, prosjek, likovi dosadni malo zzz", 3));
        }

    }

    private void setSearchParams() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        keyword = params.get("Keyword");
        try {
            minRating = Double.parseDouble(params.get("MinRating"));
        } catch (Exception e) {
            minRating = 0;
        }
        System.out.println("ViewSearchBooks: Opening view with parametars: "
                + "Keyword: " + keyword + " "
                + "MinRating: " + minRating);

    }

    public List<TestnaKlasaKomentar> komentari(int page) {
        List<TestnaKlasaKomentar> temp = new ArrayList<>();
        int offset = page * maksReviewsPerPage;
        int size = offset + maksReviewsPerPage;
        for (int i = offset; i < size; i++) {
            if (komentari.size() <= i) {
                break;
            }
            temp.add(komentari.get(i));
        }
        return temp;
    }

    public void loadNextComments() {
        if (pageNum < komentari.size() / maksReviewsPerPage) {
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

}
