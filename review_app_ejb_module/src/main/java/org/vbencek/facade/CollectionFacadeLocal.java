/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.facade;

import java.util.List;
import javax.ejb.Local;
import org.vbencek.model.Book;
import org.vbencek.model.Collection;
import org.vbencek.model.UserT;

/**
 *
 * @author Tino
 */
@Local
public interface CollectionFacadeLocal {

    void create(Collection collection);

    void edit(Collection collection);

    void remove(Collection collection);

    Collection find(Object id);

    List<Collection> findAll();

    List<Collection> findRange(int[] range);

    int count();
    
    boolean isBookInCollection(Book book, UserT userT);
    
    List<Book> findUserBooksByCriteria(UserT userT,String isbn, String keyword, int publishYear, String publisher, double minimumAvgRating,String sortOption,int offset,int limit);
    
    long countUserBooksByCriteria(UserT userT,String isbn, String keyword, int publishYear, String publisher, double minimumAvgRating);
}
