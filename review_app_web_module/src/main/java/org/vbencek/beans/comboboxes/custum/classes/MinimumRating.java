/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.comboboxes.custum.classes;

/**
 *
 * @author Tino
 */
public class MinimumRating {
    String name;
    double value;

    public MinimumRating(String name, double value) {
        this.name = name;
        this.value = value;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double values) {
        this.value = values;
    }
    
}
