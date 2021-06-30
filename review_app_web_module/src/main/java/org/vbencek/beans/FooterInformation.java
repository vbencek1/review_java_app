/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.Setter;
import org.vbencek.properties.PropertiesLoader;

/**
 *
 * @author Tino
 */
@Named(value = "footerInformation")
@ApplicationScoped
public class FooterInformation {

    @Getter
    @Setter
    String creator = "";
    @Getter
    @Setter
    String email = "";
    @Getter
    @Setter
    String telephone = "";
    @Getter
    @Setter
    String adress = "";
    @Getter
    @Setter
    String city = "";
    @Getter
    @Setter   
    String postalCode="";

    @PostConstruct
    private void init() {
        loadInformationFromProperties();
    }
    
    private void loadInformationFromProperties(){
        PropertiesLoader propLoader= new PropertiesLoader();
        creator=propLoader.getProperty("footer.creator");
        email=propLoader.getProperty("footer.email");
        telephone=propLoader.getProperty("footer.telephone");
        adress=propLoader.getProperty("footer.adress");
        city=propLoader.getProperty("footer.city");
        postalCode=propLoader.getProperty("footer.postalCode");
    }
    

}
