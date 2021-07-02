/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans;

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
import org.vbencek.properties.ParamsCaching;
import org.vbencek.properties.PropertiesLoader;

/**
 *
 * @author Tino
 */
@Named(value = "viewBookDetails")
@ViewScoped
public class ViewBookDetails implements Serializable {
    
    @Inject
    ParamsCaching paramsCaching;
    
    @Getter
    @Setter
    int bookID = 0;

    @Getter
    @Setter
    String bookName = "";

    @Getter
    @Setter
    boolean notFavorite = true;

    @Getter
    @Setter
    int offsetComments = 0;

    @Getter
    @Setter
    int sizeComments = 5;

    @Getter
    @Setter
    int pageNum = 0;
    List<TestnaKlasaKomentar> komentari = new ArrayList<>();

    int maksCommentsPerPage;

    //maknuti hardkodirane poslje
    //mogu dodati rendere za prijavljene korisnike naprimjer
    //staviti u zasebnu metodu mozda
    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String bookid = params.get("id");
        if(bookid==null){
            bookid=String.valueOf(paramsCaching.getBookIdCache());
        }
        System.out.println("ViewBookDetails: Opening view for bookID: " + bookid);
        bookID = Integer.parseInt(bookid);
        bookName = "Druzba pere kvrzice";
        komentari.add(new TestnaKlasaKomentar(1, "Hejter 1", "Smece obicno ne valja nista", 1));
        komentari.add(new TestnaKlasaKomentar(1, "Hejter 2", "Smece obicno ne valja nista", 1));
        komentari.add(new TestnaKlasaKomentar(1, "Hejter 3", "Smece obicno ne valja nista", 1));
        komentari.add(new TestnaKlasaKomentar(1, "Hejter 4", "Smece obicno ne valja nista", 1));
        komentari.add(new TestnaKlasaKomentar(1, "Hejter 5", "Smece obicno ne valja nista", 1));
        komentari.add(new TestnaKlasaKomentar(1, "Hejter 6", "Smece obicno ne valja nista", 1));
        komentari.add(new TestnaKlasaKomentar(1, "Hejter 7", "Smece obicno ne valja nista", 1));
        komentari.add(new TestnaKlasaKomentar(1, "Sara K 8", "Mnogo dobra knjiga preporučila bih svima", 5));
        komentari.add(new TestnaKlasaKomentar(1, "Mirko  9", "Volim tu knjigu mmm", 4.1));
        komentari.add(new TestnaKlasaKomentar(1, "Sorko  10", "Dobro je, prosjek, likovi dosadni malo zzz", 3.3));
        komentari.add(new TestnaKlasaKomentar(1, "Sorko  11", "Dobro je, prosjek, likovi dosadni malo zzz", 3.3));
        PropertiesLoader propLoader = new PropertiesLoader();
        try {
            maksCommentsPerPage = Integer.parseInt(propLoader.getProperty("details.maxCommentsPerPage"));
        } catch (NumberFormatException e) {
            maksCommentsPerPage = 5;
        }
    }

    public List<TestnaKlasaKomentar> komentari(int page) {
        List<TestnaKlasaKomentar> temp = new ArrayList<>();
        int offset = page * maksCommentsPerPage;
        int size = offset + maksCommentsPerPage;
        for (int i = offset; i < size; i++) {
            if (komentari.size() <= i) {
                break;
            }
            temp.add(komentari.get(i));
        }
        return temp;
    }

    public void loadNextComments() {
        if (pageNum < komentari.size() / maksCommentsPerPage) {
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
        String width = "0%";
        Double convert = (rating / 5) * 100;
        int roundNum = (int) Math.round(convert);
        width = String.valueOf(roundNum) + "%";
        return width;
    }

}
