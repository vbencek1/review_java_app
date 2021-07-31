package org.vbencek.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesLoader {
    
    public String getProperty(String propName){
        try (InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("PropertiesLoader: unable to find config.properties");
                return "";
            }
            prop.load(input);
            String result=prop.getProperty(propName);
            //System.out.println("Loading property: "+propName+"="+result);
            return result;
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }
    
}
