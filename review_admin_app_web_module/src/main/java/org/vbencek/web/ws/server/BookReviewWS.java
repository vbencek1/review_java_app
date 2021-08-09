package org.vbencek.web.ws.server;

import java.util.List;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.vbencek.model.Book;
import org.vbencek.model.Review;

/**
 * Web service class
 * @author vbencek
 */
@WebService(serviceName = "BookReviewWS")
public class BookReviewWS {
    
    @Inject
    BookReviewData bookReviewData;

    @WebMethod(operationName = "getBooksByCriteria")
    public List<Book> getBooksByCriteria(
            @WebParam(name = "isbn") String isbn, 
            @WebParam(name = "keyword") String keyword, 
            @WebParam(name = "publishYear") int publishYear, 
            @WebParam(name = "publisher") String publisher, 
            @WebParam(name = "minimumAvgRating") double minimumAvgRating, 
            @WebParam(name = "sortOption") String sortOption, 
            @WebParam(name = "offset") int offset, 
            @WebParam(name = "limit") int limit) {
        return bookReviewData.getBooksByCriteria(isbn, keyword, publishYear, publisher, minimumAvgRating, sortOption, offset, limit);
    }
    
    @WebMethod(operationName = "getCountBooksByCriteria")
    public long getCountBooksByCriteria(
            @WebParam(name = "isbn") String isbn, 
            @WebParam(name = "keyword") String keyword, 
            @WebParam(name = "publishYear") int publishYear, 
            @WebParam(name = "publisher") String publisher, 
            @WebParam(name = "minimumAvgRating") double minimumAvgRating) {
        return bookReviewData.getCountBooksByCriteria(isbn, keyword, publishYear, publisher, minimumAvgRating);
    }
    
    @WebMethod(operationName = "isBookExistsByIsbn")
    public boolean isBookExistsByIsbn(
            @WebParam(name = "isbn") String isbn) {
        return bookReviewData.isBookExistsByIsbn(isbn);
    }
    
    @WebMethod(operationName = "getBookById")
    public Book getBookById(
            @WebParam(name = "id") int id) {
        return bookReviewData.getBookById(id);
    }
    
    @WebMethod(operationName = "getBooksWithTextReviews")
    public List<Book> getBooksWithTextReviews() {
        return bookReviewData.getBooksWithTextReviews();
    }
    
    @WebMethod(operationName = "getReviewsByCriteria")
    public List<Review> getReviewsByCriteria(
            @WebParam(name = "bookId") int bookId, 
            @WebParam(name = "userId") int userId, 
            @WebParam(name = "minimumAvgRating") double minimumAvgRating, 
            @WebParam(name = "sortOption") String sortOption, 
            @WebParam(name = "hasDescription") boolean hasDescription,
            @WebParam(name = "isPublic") boolean isPublic,
            @WebParam(name = "offset") int offset,
            @WebParam(name = "limit") int limit) {
        return bookReviewData.getReviewsByCriteria(bookId, userId, minimumAvgRating, sortOption, hasDescription, isPublic, offset, limit);
    }
    
    @WebMethod(operationName = "getCountReviewsByCriteria")
    public long getCountReviewsByCriteria(
            @WebParam(name = "bookId") int bookId, 
            @WebParam(name = "userId") int userId, 
            @WebParam(name = "minimumAvgRating") double minimumAvgRating, 
            @WebParam(name = "hasDescription") boolean hasDescription,
            @WebParam(name = "isPublic") boolean isPublic) {
        return bookReviewData.getCountReviewsByCriteria(bookId, userId, minimumAvgRating, hasDescription, isPublic);
    }
    
    @WebMethod(operationName = "getReviewsByBook")
    public List<Review> getReviewsByBook(
            @WebParam(name = "bookId") int bookId) {
        return bookReviewData.getReviewsByBook(bookId);
    }
    
    
}
