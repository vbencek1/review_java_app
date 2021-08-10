/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.requests;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 * Class that holds general function used across all other views
 * @author vbencek
 */
@Named(value = "genFunctions")
@RequestScoped
public class GenFunctions {

    /**
     * Rounds double to 2 decimal spots
     * @param number
     * @return 
     */
    public Double roundDouble(double number) {

        number = number * 100;
        number = (double) Math.round(number);
        number = number / 100;
        
        return number;
    }

}
