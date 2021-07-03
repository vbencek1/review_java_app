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
public class SortOptions {
    String optionName;
    String value;

    public SortOptions(String optionName, String value) {
        this.optionName = optionName;
        this.value = value;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
