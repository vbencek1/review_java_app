/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.properties;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Tino
 */
@Named(value = "paramsCaching")
@SessionScoped
public class ParamsCaching implements Serializable {

    @Getter
    @Setter
    int BookIdCache;
    
}
