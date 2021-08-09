package org.vbencek.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class ApplicationListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        //to do
        System.out.println("ADMIN APP: Aplication started!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ADMIN APP: Aplication stopped!");
    }

}
