/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.web.ws.server;

import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.vbencek.facade.BookFacadeLocal;
import org.vbencek.facade.ReviewFacadeLocal;
import org.vbencek.facade.UserTFacadeLocal;
import org.vbencek.model.Book;
import org.vbencek.model.Review;
import org.vbencek.model.UserT;

/**
 * Class that calls functions from ejb modul that fetched data from DB.
 * Class is primarly used as "helping class" to fetch data for WS
 * @author vbencek
 */
@Named(value = "bookReviewData")
@RequestScoped
public class BookReviewData {

    @EJB(beanName = "BookFacade")
    BookFacadeLocal bookFacade;

    @EJB(beanName = "UserTFacade")
    UserTFacadeLocal userTFacade;

    @EJB(beanName = "ReviewFacade")
    ReviewFacadeLocal reviewFacade;

    public List<Book> getBooksByCriteria(String isbn, String keyword, int publishYear, String publisher, double minimumAvgRating, String sortOption, int offset, int limit) {
        return bookFacade.findBooksByCriteria(isbn, keyword, publishYear, publisher, minimumAvgRating, sortOption, offset, limit);
    }

    public long getCountBooksByCriteria(String isbn, String keyword, int publishYear, String publisher, double minimumAvgRating) {
        return bookFacade.countBooksByCriteria(isbn, keyword, publishYear, publisher, minimumAvgRating);
    }

    public boolean isBookExistsByIsbn(String isbn) {
        return bookFacade.isISBNExists(isbn);
    }

    public Book getBookById(int id) {
        return bookFacade.findBookById(id);
    }

    public List<Book> getBooksWithTextReviews() {
        return bookFacade.findBooksWithReviews();
    }
    
    public List<Review> getReviewsByCriteria(int bookId, int userId, double minimumAvgRating, String sortOption, boolean hasDescription, boolean isPublic, int offset, int limit) {
        Book book = null;
        UserT user = null;
        try {
            book = bookFacade.find(bookId);
        } catch (Exception e) {
            System.out.println("WS ERROR: Book doesn't exist");
        }
        try {
            user = userTFacade.find(userId);
        } catch (Exception e) {
            System.out.println("WS ERROR: User doesn't exist");
        }
        //Creating temporary list to remove password field
        List<Review> tempList=reviewFacade.findReviewsByCriteria(book, user, minimumAvgRating, sortOption, hasDescription, isPublic, offset, limit);
        for(Review rew:tempList){
            rew.getUserT().setPassword("");
        }
        return tempList;
    }

    public long getCountReviewsByCriteria(int bookId, int userId, double minimumAvgRating, boolean hasDescription, boolean isPublic) {
        Book book = null;
        UserT user = null;
        try {
            book = bookFacade.find(bookId);
        } catch (Exception e) {
            System.out.println("WS ERROR: Book doesn't exist");
        }
        try {
            user = userTFacade.find(userId);
        } catch (Exception e) {
            System.out.println("WS ERROR: User doesn't exist");
        }
        return reviewFacade.countReviewsByCriteria(book, user, minimumAvgRating, hasDescription, isPublic);
    }

    public List<Review> getReviewsByBook(int bookId) {
        Book book = null;
        try {
            book = bookFacade.find(bookId);
        } catch (Exception e) {
            System.out.println("WS ERROR: Book doesn't exist");
        }
        //Creating temporary list to remove password field
        List<Review> tempList=reviewFacade.findReviewByBook(book);
        for(Review rew:tempList){
            rew.getUserT().setPassword("");
        }
        return tempList;
    }

}
