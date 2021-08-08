/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.facade;

import java.util.List;
import javax.ejb.Local;
import org.vbencek.model.Book;

/**
 *
 * @author Tino
 */
@Local
public interface BookFacadeLocal {

    void create(Book book);

    void edit(Book book);

    void remove(Book book);

    Book find(Object id);

    List<Book> findAll();

    List<Book> findRange(int[] range);

    int count();

    List<Book> findBooksByCriteria(String isbn, String keyword, int publishYear, String publisher, double minimumAvgRating, String sortOption, int offset, int limit);

    long countBooksByCriteria(String isbn, String keyword, int publishYear, String publisher, double minimumAvgRating);

    List<String> findAllYears();

    List<String> findAllPublishers();

    boolean isISBNExists(String isbn);

    List<Book> findBooksWithReviews();

    Book findBookById(int id);

    List<Book> findRecommendedBooks(String keyword, double minimumAvgRating, int minimumRatingsCount, String sortOption, int offset, int limit);

}
