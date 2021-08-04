/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.requests;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Tino
 */
@Named(value = "genFunctions")
@RequestScoped
public class GenFunctions {

    public Double roundDouble(double number) {

        number = number * 100;
        number = (double) Math.round(number);
        number = number / 100;
        
        return number;
    }

}
