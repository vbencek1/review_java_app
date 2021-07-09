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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.vbencek.beans.TestnaKlasaObrisati;
import org.vbencek.properties.ParamsCaching;

/**
 *
 * @author Tino
 */
@Named(value = "viewHome")
@ViewScoped
public class ViewHome implements Serializable {
    
    @Inject
    ParamsCaching paramsCaching;
    
    @Getter
    @Setter
    List<TestnaKlasaObrisati> testList = new ArrayList<>();

    @PostConstruct
    public void test() {
        testList.add(new TestnaKlasaObrisati(1, "Garry Lotter: part one", 2002, "Tino Rinek", "Izdavac _45", "Knjiga koja je dobra", 4.56, 20, new Date()));
        testList.add(new TestnaKlasaObrisati(2, "Larry Lotter: part two three", 2302, "Tino Vinek", "Izdavac _45", "Knjiga govori o covjeku", 3.56, 21, new Date()));
        testList.add(new TestnaKlasaObrisati(3, "Osman Lotter", 2012, "Tino Vinek", "Izdavac _45", "Neki opis knjige", 2.56, 200, new Date(1234124)));
        testList.add(new TestnaKlasaObrisati(4, "Lome tro kl", 1111, "Maki Daki", "Neki tam izdavac", "Knjiga o necemu i svemu", 1.56, 24, new Date(123522344)));
        testList.add(new TestnaKlasaObrisati(5, "Garry Lotter: part two", 2002, "Tino Tinek", "Izdavac _45", "Knjiga o svemu i govori sve", 1.52, 14, new Date(3456576)));
        testList.add(new TestnaKlasaObrisati(6, "Neka tamo knjiga test", 1002, "Tino Test", "Nekaj trece", "Svetlo na kraju tunela za mnoge ljudei", 5.00, 11, new Date(1234576)));
    }

    public List<TestnaKlasaObrisati> getSixRecomendedbooks(int startingFrom, int totalNumber) {
        List<TestnaKlasaObrisati> temp = new ArrayList<>();
        int listEnd = startingFrom + totalNumber;
        Collections.sort(testList, Comparator.comparingDouble(TestnaKlasaObrisati::getOcjena).reversed());
        
        if (testList.size() >= listEnd - 1) {
            for (int i = startingFrom; i < listEnd; i++) {
                temp.add(testList.get(i));
            }
        }
        return temp;
    }

    public List<TestnaKlasaObrisati> getSixPopularbooks(int startingFrom, int totalNumber) {
        List<TestnaKlasaObrisati> temp = new ArrayList<>();
        int listEnd = startingFrom + totalNumber;
        Collections.sort(testList, Comparator.comparingInt(TestnaKlasaObrisati::getBrojOcjena).reversed());
        if (testList.size() >= listEnd - 1) {
            for (int i = startingFrom; i < listEnd; i++) {
                temp.add(testList.get(i));
            }
        }
        return temp;
    }

    public List<TestnaKlasaObrisati> getSixLatestbooks(int startingFrom, int totalNumber) {
        List<TestnaKlasaObrisati> temp = new ArrayList<>();
        int listEnd = startingFrom + totalNumber;
        if (testList.size() >= listEnd - 1) {
            for (int i = startingFrom; i < listEnd; i++) {
                temp.add(testList.get(i));
            }
        }
        return temp;
    }
    
    public String redirectToBookDetails(int bookId){
        paramsCaching.setBookIdCache(bookId);
        return "bookDetails.xhtml?id="+bookId+"&faces-redirect=true";
    }

}
