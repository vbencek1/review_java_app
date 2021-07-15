/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.comboboxes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.vbencek.beans.comboboxes.custum.classes.MinimumRating;
import org.vbencek.beans.comboboxes.custum.classes.SortOptions;
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.localization.Localization;

/**
 *
 * @author Tino
 */
@Named(value = "comboboxData")
@ApplicationScoped
public class ComboboxData {

    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;

    @Inject
    Localization localization;

    @Getter
    @Setter
    List<String> publishYears = new ArrayList<>();

    @Getter
    @Setter
    List<String> publishers = new ArrayList<>();

    @Getter
    @Setter
    List<MinimumRating> minimumRating = new ArrayList<>();

    @Getter
    @Setter
    List<SortOptions> sortOption = new ArrayList<>();

    @PostConstruct
    void init() {
        fillPublishYears();
        fillPublishers();
        fillminimumRatings();
        fillSortOptions();
    }

    private void fillPublishYears() {
       publishYears=bookFacade.findAllYears();
    }

    private void fillPublishers() {
        publishers=bookFacade.findAllPublishers();
    }

    private void fillminimumRatings() {
        ResourceBundle res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        minimumRating.add(new MinimumRating(res.getString("combobox.rating.one"), 1));
        minimumRating.add(new MinimumRating(res.getString("combobox.rating.two"), 2));
        minimumRating.add(new MinimumRating(res.getString("combobox.rating.three"), 3));
        minimumRating.add(new MinimumRating(res.getString("combobox.rating.four"), 4));
        minimumRating.add(new MinimumRating(res.getString("combobox.rating.five"), 5));
    }

    
    private void fillSortOptions() {
        ResourceBundle res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        sortOption.add(new SortOptions(res.getString("combobox.sort.name"), "title"));
        sortOption.add(new SortOptions(res.getString("combobox.sort.rating"), "rating"));
        sortOption.add(new SortOptions(res.getString("combobox.sort.reviews"), "reviews"));
    }

}
