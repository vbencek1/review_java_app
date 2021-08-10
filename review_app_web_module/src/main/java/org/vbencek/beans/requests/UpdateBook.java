/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans.requests;

import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.model.Book;
import org.vbencek.model.Review;

/**
 * Updates book atributes: averageRating and ratinsCount when there's CUD operation on review table.
 * @author vbencek
 */
@Named(value = "updateBook")
@RequestScoped
public class UpdateBook {

    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;
    
    
    /**
     * Calculates new average rating value for book
     * @param review 
     * @param action INSERT,UPDATE,DELETE
     * @param oldRating current rating that will get removed or updated
     * @return 
     */
    private double calculateRating(Review review, String action, double oldRating){
        double result=0;
        Book book = review.getBook();
        int ratingCount=book.getRatingsCount();
        double averageRating=book.getAverageRating();   
        
        double rating = review.getRating();
        
        double totalRatingSum=ratingCount*averageRating;
        
        if(action.equals("INSERT")){
            int newRatingCount=ratingCount+1;
            result=(totalRatingSum+rating)/newRatingCount;   
            System.out.println("CALCULATING: ("+totalRatingSum+"+"+rating+")/"+newRatingCount+"="+result);
        }else if(action.equals("UPDATE")){
            result=(totalRatingSum-oldRating+rating)/ratingCount; 
            System.out.println("CALCULATING: ("+totalRatingSum+"-"+oldRating+"+"+rating+")/"+ratingCount+"="+result);
        }else if(action.equals("DELETE")){
            int newRatingCount=ratingCount-1;
            result=(totalRatingSum-rating)/newRatingCount; 
               
            System.out.println("CALCULATING: ("+totalRatingSum+"-"+rating+")/"+newRatingCount+"="+result);
        }
        return result;
    }
    
    /**
     * update book with new values
     * @param review
     * @param action 
     */
    public void updateRatings(Review review, String action) {
        Book book = review.getBook();
        int ratingCount=book.getRatingsCount();
        book.setAverageRating(calculateRating(review, action,0));
        if(action.equals("INSERT")){
            int newRatingCount=ratingCount+1;
            book.setRatingsCount(newRatingCount); 
        }else if(action.equals("UPDATE")){  

        }else if(action.equals("DELETE")){
            int newRatingCount=ratingCount-1;
            book.setRatingsCount(newRatingCount);
        }  
        bookFacade.edit(book);
    }
    
    /**
     * update book with new values. Used primary for UPDATE function
     * @param review
     * @param action
     * @param oldRating 
     */
    public void updateRatings(Review review, String action,double oldRating) {
        Book book = review.getBook();
        int ratingCount=book.getRatingsCount();
        book.setAverageRating(calculateRating(review, action,oldRating));
        if(action.equals("INSERT")){
            int newRatingCount=ratingCount+1;
            book.setRatingsCount(newRatingCount); 
        }else if(action.equals("UPDATE")){  

        }else if(action.equals("DELETE")){
            int newRatingCount=ratingCount-1;
            book.setRatingsCount(newRatingCount);
        }  
        bookFacade.edit(book);
    }
    

}
