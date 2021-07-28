/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Tino
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Book.findAll", query = "SELECT b FROM Book b")})
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BOOK_ID", nullable = false)
    private Integer bookId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 750)
    @Column(nullable = false, length = 750)
    private String authors;
    @Size(max = 1000)
    @Column(length = 1000)
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AVERAGE_RATING", nullable = false)
    private double averageRating;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(nullable = false, length = 10)
    private String isbn;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(nullable = false, length = 13)
    private String isbn13;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "LANGUAGE_CODE", nullable = false, length = 25)
    private String languageCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "NUM_PAGES", nullable = false, length = 25)
    private String numPages;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RATINGS_COUNT", nullable = false)
    private int ratingsCount;
    @Column(name = "PUBLICATION_DATE")
    @Temporal(TemporalType.DATE)
    private Date publicationDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 95)
    @Column(nullable = false, length = 95)
    private String publisher;
    @Size(max = 255)
    @Column(name = "IMG_PATH", length = 255)
    private String imgPath;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    private List<Collection> collectionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    private List<Review> reviewList;

    public Book() {
    }

    public Book(Integer bookId) {
        this.bookId = bookId;
    }

    public Book(Integer bookId, String title, String authors, double averageRating, String isbn, String isbn13, String languageCode, String numPages, int ratingsCount, String publisher) {
        this.bookId = bookId;
        this.title = title;
        this.authors = authors;
        this.averageRating = averageRating;
        this.isbn = isbn;
        this.isbn13 = isbn13;
        this.languageCode = languageCode;
        this.numPages = numPages;
        this.ratingsCount = ratingsCount;
        this.publisher = publisher;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getNumPages() {
        return numPages;
    }

    public void setNumPages(String numPages) {
        this.numPages = numPages;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @XmlTransient
    public List<Collection> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(List<Collection> collectionList) {
        this.collectionList = collectionList;
    }

    @XmlTransient
    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookId != null ? bookId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.bookId == null && other.bookId != null) || (this.bookId != null && !this.bookId.equals(other.bookId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.vbencek.model.Book[ bookId=" + bookId + " ]";
    }
    
}
