/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.views;

import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.email.EmailSender;
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.facade.RequestFacadeLocal;
import org.vbencek.localization.Localization;
import org.vbencek.model.Request;
import org.vbencek.model.UserT;
import org.vbencek.pdf.PdfGenerator;
import org.vbencek.properties.PropertiesLoader;

/**
 * View that shows all book requests
 * @author vbencek
 */
@Named(value = "viewAdminRequests")
@ViewScoped
public class ViewAdminRequests implements Serializable {

    @EJB(beanName = "RequestFacade")
    RequestFacadeLocal requestFacade;

    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;
    
    @Inject 
    ActiveUserSession activeUserSession;
    
    @Inject
    Localization localization;
    ResourceBundle res;

    @Getter
    @Setter
    List<Request> listRequestIsbn;

    @Getter
    @Setter
    List<Request> listRequestInfo;

    @Getter
    @Setter
    boolean renderRequestIsbn = true;

    @Getter
    @Setter
    boolean renderRequestInfo = false;

    @Getter
    @Setter
    String option = "2";
    
    //PDF ATRIBUTES
    @Getter
    @Setter
    String pdfLink;
    @Getter
    @Setter
    String pdfLinkDisplayName;
    @Getter
    @Setter
    boolean renderPdfLink=false;

    @PostConstruct
    public void init() {
        res = ResourceBundle.getBundle("org.vbencek.localization.Translations", new Locale(localization.getLanguage()));
        System.out.println("ViewAdminRequests: opening view");
        listRequestIsbn = requestFacade.findRequestsByISBNExists(true);
        listRequestInfo = requestFacade.findRequestsByISBNExists(false);
    }
    
    /**
     * Switched between requests with ISBN and request withput ISBN
     */
    public void changeView() {
        if ("2".equals(option)) {
            renderRequestIsbn = false;
            renderRequestInfo = true;
        } else {
            renderRequestInfo = false;
            renderRequestIsbn = true;
        }
    }

    public String convertToFriendlyDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date).toUpperCase();
    }
    
    /**
     * check if request has ISBN set
     * @param request
     * @return 
     */
    public boolean isISBNExists(Request request) {
        return bookFacade.isISBNExists(request.getIsbn());
    }

    public void resetvalues() {
        DataTable datatable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":formRequests:reqIsbn");
        datatable.reset();
        DataTable datatable2 = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":formRequests:reqInfo");
        datatable2.reset();
    }

    private void redirect(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void openBookDetailView(Request request) {
        String url = "adminBookDetails.xhtml?requestId=" + request.getRequestId();
        redirect(url);
    }
    
    /**
     * Send email to user that created request
     * @param user 
     */
    private void notifyUser(UserT user){
        EmailSender emailSender = new EmailSender();
        String msgSubject = res.getString("admin.viewBookDetails.email.subject");
        String msgText = res.getString("admin.viewBookDetails.email.text.notOk");
        String msgTO = user.getEmail();
        emailSender.sendMessage(msgTO, msgSubject, msgText);
    }
    
    private void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void removeRequestFromList(Request request) {
        if (request.getIsbn() != null) {
            listRequestIsbn.remove(request);
        } else {
            listRequestInfo.remove(request);
        }
    }
    
    public void removeRequest(Request request) {
        notifyUser(request.getUserId());
        requestFacade.remove(request);
        removeRequestFromList(request);
        addMessage(res.getString("admin.viewAdminRequests.delete.summary"),res.getString("admin.viewAdminRequests.delete.details"));
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), "DELETED REQUEST_ID: "+request.getRequestId());
    }
    
    private void generatePdfLink(String pdfName){
        PropertiesLoader propLoader = new PropertiesLoader();
        String path=propLoader.getProperty("pdf.path.requestList");
        String fullURL=path+pdfName;
        pdfLink=fullURL;
        renderPdfLink=true;
        pdfLinkDisplayName=pdfName;
        PrimeFaces.current().ajax().update("generatePdf:pdfLink");
        
    }
    
    public void generatePdf(){
        PdfGenerator pdfGenerator= new PdfGenerator();
        String pdfName=activeUserSession.getUsername()+"_requests_";       
        if ("2".equals(option)) {
            pdfName=pdfName+"isbn.pdf";
            pdfGenerator.generatePdfForRequests(listRequestIsbn,pdfName);
        } else {
            pdfName=pdfName+"titles.pdf";
            pdfGenerator.generatePdfForRequests(listRequestInfo,pdfName);
        }
        generatePdfLink(pdfName);
        String summary=res.getString("admin.viewAdminRequests.pdf.summary");
        String details=res.getString("admin.viewAdminRequests.pdf.details");
        addMessage(summary, details);
    }
}
