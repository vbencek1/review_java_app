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
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.vbencek.beans.comboboxes.custum.classes.MinimumRating;
import org.vbencek.beans.comboboxes.custum.classes.SortOptions;
import org.vbencek.localization.Localization;

/**
 *
 * @author Tino
 */
@Named(value = "comboboxData")
@ApplicationScoped
public class ComboboxData {
    
   @Inject
   Localization localization;
    
   @Getter
   @Setter
   List<String> publishYears=new ArrayList<>();
   
   @Getter
   @Setter
   List<String> publishers=new ArrayList<>();
      
   @Getter
   @Setter
   List<MinimumRating> minimumRating= new ArrayList<>();
   
   @Getter
   @Setter
   List<SortOptions> sortOption= new ArrayList<>();
   
   @PostConstruct
   void init(){
       fillPublishYears();
       fillPublishers();
       fillminimumRatings();
       fillSortOptions();
   }
   
   private void fillPublishYears(){
       publishYears.add("2011");
       publishYears.add("2012");
       publishYears.add("2014");
   }
   
   private void fillPublishers(){
       publishers.add("Tino Tinek");
       publishers.add("Isus");
   }
   
   private void fillminimumRatings(){
       ResourceBundle res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
       minimumRating.add(new MinimumRating(res.getString("combobox.rating.one"),1));
       minimumRating.add(new MinimumRating(res.getString("combobox.rating.two"),2));
       minimumRating.add(new MinimumRating(res.getString("combobox.rating.three"),3));
       minimumRating.add(new MinimumRating(res.getString("combobox.rating.four"),4));
       minimumRating.add(new MinimumRating(res.getString("combobox.rating.five"),5));
   }
   
   //pogledat kad bu v bazi i na tome slozit
   private void fillSortOptions(){
       ResourceBundle res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
       sortOption.add(new SortOptions(res.getString("combobox.sort.name"),"1"));
       sortOption.add(new SortOptions(res.getString("combobox.sort.rating"),"2"));
       sortOption.add(new SortOptions(res.getString("combobox.sort.reviews"),"3"));
   }
    
}
