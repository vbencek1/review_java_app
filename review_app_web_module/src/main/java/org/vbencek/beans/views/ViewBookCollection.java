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
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.vbencek.beans.ActiveUserSession;
import org.vbencek.facade.CollectionFacadeLocal;
import org.vbencek.model.Book;
import org.vbencek.properties.ParamsCaching;
import org.vbencek.properties.PropertiesLoader;

/**
 * View that shows books in active user collection
 * @author vbencek
 */
@Named(value = "viewBookCollection")
@ViewScoped
public class ViewBookCollection implements Serializable {
    
    @EJB(beanName = "CollectionFacade")
    CollectionFacadeLocal collectionFacade;
    
    @Inject
    ActiveUserSession activeUserSession;

    @Inject
    ParamsCaching paramsCaching;

    @Getter
    @Setter
    String isbn;

    @Getter
    @Setter
    String keyword;

    @Getter
    @Setter
    int year;

    @Getter
    @Setter
    String publisher;

    @Getter
    @Setter
    double minRating;

    @Getter
    @Setter
    String sortOption;

    @Getter
    @Setter
    int pageNum = 0;

    int maksBooksPerPage;
    long numberOfUserBooks=0;
    
    @Getter
    @Setter
    boolean renderRedirect = false;

    @Getter
    @Setter
    String redirectFunction = "";

    @PostConstruct
    void init() {
        //redirect if not loged in
        if (activeUserSession.getActiveUser() == null) {
            redirectFunction = "location.href = 'index.xhtml?action=openLogin';";
            renderRedirect = true;
        } else {
            setSearchParams();
            PropertiesLoader propLoader = new PropertiesLoader();
            try {
                maksBooksPerPage = Integer.parseInt(propLoader.getProperty("books.maxBooksPerPage"));
            } catch (NumberFormatException e) {
                maksBooksPerPage = 10;
            }
            numberOfUserBooks=collectionFacade.countUserBooksByCriteria(activeUserSession.getActiveUser(), isbn, keyword, year, publisher, minRating);
        }
    }

    private void setSearchParams() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        isbn = params.get("ISBN") != null ? params.get("ISBN") : "";
        keyword = params.get("Keyword") != null ? params.get("Keyword") : "";
        try {
            year = Integer.parseInt(params.get("Year"));
        } catch (Exception e) {
            year = 0;
        }
        publisher = params.get("Publisher") != null ? params.get("Publisher") : "";
        try {
            minRating = Double.parseDouble(params.get("MinRating"));
        } catch (Exception e) {
            minRating = 0;
        }
        sortOption = params.get("SortBy");
        try {
            pageNum = Integer.parseInt(params.get("PageNum"));
        } catch (Exception e) {
            pageNum = 0;
        }
        String stringParams="ViewBookCollection: Opening view with parametars: "
                + "ISBN: " + isbn + " "
                + "Keyword: " + keyword + " "
                + "Year: " + year + " "
                + "Publisher: " + publisher + " "
                + "MinRating: " + minRating + " "
                + "SortBy: " + sortOption;
        System.out.println(stringParams);
        setParamsForPagination();
        activeUserSession.addDataLog(this.getClass().getSimpleName(), new Object(){}.getClass().getEnclosingMethod().getName(), stringParams);
    }
    
    private void setParamsForPagination() {
        String url = "bookCollection.xhtml?ISBN=" + isbn
                + "&Keyword=" + keyword
                + "&Year=" + year
                + "&Publisher=" + publisher
                + "&MinRating=" + minRating
                + "&SortBy=" + sortOption;
        System.out.println(url);
                
        paramsCaching.setNavigationUrl(url);
    }
    
    public String convertDateToYear(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        simpleDateFormat = new SimpleDateFormat("YYYY");
        return simpleDateFormat.format(date).toUpperCase();
    }
    
    public String setIMG(Book book) {
        if (book.getImgPath() != null) {
            return book.getImgPath();
        } else {
            return "http://covers.openlibrary.org/b/isbn/" + book.getIsbn() + "-M.jpg";
        }
    }
    
    public String makeTextShorter(String text, int maxChars){
        return StringUtils.abbreviate(text, maxChars);
    }
    
    public String showPaginationInfo(){
        int offset = pageNum * maksBooksPerPage;
        int size = offset + maksBooksPerPage;
        if(size>numberOfUserBooks){
            int diff=(int) (size-numberOfUserBooks);
            size=size-diff;
        }
        return offset+" - "+size+" (Maks: "+numberOfUserBooks+")";
    }
    
    public List<Book> getListOfBooks() {
        int offset = pageNum * maksBooksPerPage;
        int size = offset + maksBooksPerPage;
        return collectionFacade.findUserBooksByCriteria(activeUserSession.getActiveUser(), isbn, keyword, year, publisher, minRating, sortOption, offset, size);
    }
    
    public void redirectToUrl(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }
    
    public void nextPage() {
        if (pageNum < numberOfUserBooks / maksBooksPerPage) {
            pageNum++;
            String url = paramsCaching.getNavigationUrl()
                    + "&PageNum=" + pageNum;
            redirectToUrl(url);
        }
    }

    public void previousPage() {
        if (pageNum > 0) {
            pageNum--;
            String url = paramsCaching.getNavigationUrl()
                    + "&PageNum=" + pageNum;
            redirectToUrl(url);
        }
    }

    public String redirectToBookDetails(int bookId) {
        paramsCaching.setBookIdCache(bookId);
        return "bookDetails.xhtml?id=" + bookId + "&faces-redirect=true";
    }

}
