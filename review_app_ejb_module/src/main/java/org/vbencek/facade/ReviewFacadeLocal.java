/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.facade;

import java.util.List;
import javax.ejb.Local;
import org.vbencek.model.Book;
import org.vbencek.model.Review;
import org.vbencek.model.UserT;

/**
 *  
 * @author Tino
 */
@Local
public interface ReviewFacadeLocal {

    void create(Review review);

    void edit(Review review);

    void remove(Review review);

    Review find(Object id);

    List<Review> findAll();

    List<Review> findRange(int[] range);

    int count();
    
    List<Review> findReviewsByCriteria(Book book, UserT userT, double minimumAvgRating, String sortOption, boolean hasDescription, boolean isPublic, int offset, int limit);
    
    long countReviewsByCriteria(Book book, UserT userT, double minimumAvgRating, boolean hasDescription, boolean isPublic);
}
