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
import lombok.Getter;
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.model.Book;
import org.vbencek.model.Review;

/**
 *
 * @author Tino
 */
@Named(value = "updateBook")
@RequestScoped
public class UpdateBook {

    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;
    
    
    private double getOldRating(List<Review> reviewList, Review newReview){
        for(Review rev: reviewList){
            if(rev.getBook()==newReview.getBook() && rev.getUserT()==newReview.getUserT()){
                return rev.getRating();
            }
        }return 0;
    }
    
    /*
    action= INSERT,UPDATE,DELETE
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
