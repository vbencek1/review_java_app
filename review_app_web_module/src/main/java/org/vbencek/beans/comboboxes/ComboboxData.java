/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.comboboxes;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.Setter;
import org.vbencek.beans.comboboxes.custum.classes.MinimumRating;
import org.vbencek.beans.comboboxes.custum.classes.SortOptions;

/**
 *
 * @author Tino
 */
@Named(value = "comboboxData")
@ApplicationScoped
public class ComboboxData {

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
       minimumRating.add(new MinimumRating("(1) One Star",1));
       minimumRating.add(new MinimumRating("(2) Two Stars",2));
       minimumRating.add(new MinimumRating("(3) Three Stars",3));
       minimumRating.add(new MinimumRating("(4) Four Stars",4));
       minimumRating.add(new MinimumRating("(5) Five Stars",5));
   }
   
   //pogledat kad bu v bazi i na tome slozit
   private void fillSortOptions(){
       sortOption.add(new SortOptions("Sort by Name","1"));
       sortOption.add(new SortOptions("Sort by Rating","1"));
       sortOption.add(new SortOptions("Sort by Number of Reviews","1"));
   }
    
}
