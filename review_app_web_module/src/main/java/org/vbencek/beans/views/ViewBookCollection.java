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
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.beans.TestnaKlasaObrisati;
import org.vbencek.properties.ParamsCaching;
import org.vbencek.properties.PropertiesLoader;

/**
 *
 * @author Tino
 */
@Named(value = "viewBookCollection")
@ViewScoped
public class ViewBookCollection implements Serializable {

    @Inject
    ActiveUserSession activeUserSession;

    @Inject
    ParamsCaching paramsCaching;

    @Getter
    @Setter
    String isbn;

    @Getter
    @Setter
    String keyword;

    @Getter
    @Setter
    int year;

    @Getter
    @Setter
    String publisher;

    @Getter
    @Setter
    double minRating;

    @Getter
    @Setter
    String sortOption;

    @Getter
    @Setter
    List<TestnaKlasaObrisati> testList = new ArrayList<>();

    @Getter
    @Setter
    int pageNum = 0;

    int maksBooksPerPage;

    @Getter
    @Setter
    boolean renderRedirect = false;

    @Getter
    @Setter
    String redirectFunction = "";

    @PostConstruct
    void init() {
        if (activeUserSession.getActiveUser() == null) {
            redirectFunction = "location.href = 'index.xhtml';";
            renderRedirect = true;
        } else {
            setSearchParams();
            testList.add(new TestnaKlasaObrisati(6, "Sveta knjiga o svetome", 1002, "Tino Tinek", "Nekaj trece", "Svetlo na kraju tunela za mnoge ljudei", 5.00, 11, new Date(1234576)));
            testList.add(new TestnaKlasaObrisati(7, "Sveta knjiga o svetome", 1002, "Tino Tinek", "Nekaj trece", "Svetlo na kraju tunela za mnoge ljudei", 5.00, 11, new Date(1234576)));
            testList.add(new TestnaKlasaObrisati(8, "Skupljanje kurvi: Zavjet o Penisu", 1002, "Tino Tinek", "Nekaj trece", "Svetlo na kraju tunela za mnoge ljudei", 5.00, 11, new Date(1234576)));
            PropertiesLoader propLoader = new PropertiesLoader();
            try {
                maksBooksPerPage = Integer.parseInt(propLoader.getProperty("books.maxBooksPerPage"));
            } catch (NumberFormatException e) {
                maksBooksPerPage = 10;
            }
        }
    }

    private void setSearchParams() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        isbn = params.get("ISBN");
        keyword = params.get("Keyword");
        try {
            year = Integer.parseInt(params.get("Year"));
        } catch (Exception e) {
            year = 0;
        }
        publisher = params.get("Publisher");
        try {
            minRating = Double.parseDouble(params.get("MinRating"));
        } catch (Exception e) {
            minRating = 0;
        }
        sortOption = params.get("SortBy");
        System.out.println("ViewBookCollection: Opening view with parametars: "
                + "ISBN: " + isbn + " "
                + "Keyword: " + keyword + " "
                + "Year: " + year + " "
                + "Publisher: " + publisher + " "
                + "MinRating: " + minRating + " "
                + "SortBy: " + sortOption);

    }

    public List<TestnaKlasaObrisati> testnaLista(int page) {
        List<TestnaKlasaObrisati> temp = new ArrayList<>();
        int offset = page * maksBooksPerPage;
        int size = offset + maksBooksPerPage;
        for (int i = offset; i < size; i++) {
            if (testList.size() <= i) {
                break;
            }
            if (keyword != null && testList.get(i).getNaziv().contains(keyword)) {
                temp.add(testList.get(i));
            }

        }
        return temp;
    }

    public void nextPage() {
        if (pageNum < testList.size() / maksBooksPerPage) {
            pageNum++;
        }
    }

    public void previousPage() {
        if (pageNum > 0) {
            pageNum--;
        }
    }

    public String redirectToBookDetails(int bookId) {
        paramsCaching.setBookIdCache(bookId);
        return "bookDetails.xhtml?id=" + bookId + "&faces-redirect=true";
    }

}
