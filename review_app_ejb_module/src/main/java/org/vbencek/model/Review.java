/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tino
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Review.findAll", query = "SELECT r FROM Review r")})
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReviewPK reviewPK;
    @Size(max = 1000)
    @Column(length = 1000)
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private double rating;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RATING_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ratingDate;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Boolean ispublic;
    @JoinColumn(name = "BOOK_ID", referencedColumnName = "BOOK_ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Book book;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UserT userT;

    public Review() {
    }

    public Review(ReviewPK reviewPK) {
        this.reviewPK = reviewPK;
    }

    public Review(ReviewPK reviewPK, double rating, Date ratingDate, Boolean ispublic) {
        this.reviewPK = reviewPK;
        this.rating = rating;
        this.ratingDate = ratingDate;
        this.ispublic = ispublic;
    }

    public Review(int bookId, int userId) {
        this.reviewPK = new ReviewPK(bookId, userId);
    }

    public ReviewPK getReviewPK() {
        return reviewPK;
    }

    public void setReviewPK(ReviewPK reviewPK) {
        this.reviewPK = reviewPK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Date getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }

    public Boolean getIspublic() {
        return ispublic;
    }

    public void setIspublic(Boolean ispublic) {
        this.ispublic = ispublic;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public UserT getUserT() {
        return userT;
    }

    public void setUserT(UserT userT) {
        this.userT = userT;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reviewPK != null ? reviewPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Review)) {
            return false;
        }
        Review other = (Review) object;
        if ((this.reviewPK == null && other.reviewPK != null) || (this.reviewPK != null && !this.reviewPK.equals(other.reviewPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.vbencek.model.Review[ reviewPK=" + reviewPK + " ]";
    }
    
}
