/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.localization;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Tino
 */
@Named(value = "localization")
@SessionScoped
public class Localization implements Serializable {

    @Getter
    @Setter
    private String language="hr";

    @Inject
    private FacesContext facesContext;

    public Localization() {
    }

    public Object selectLanguage() {
        facesContext.getViewRoot().setLocale(new Locale(language));
        return "";

    }
}
