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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tino
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Collection.findAll", query = "SELECT c FROM Collection c")})
public class Collection implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CollectionPK collectionPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_ADDED", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateAdded;
    @JoinColumn(name = "BOOK_ID", referencedColumnName = "BOOK_ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Book book;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UserT userT;

    public Collection() {
    }

    public Collection(CollectionPK collectionPK) {
        this.collectionPK = collectionPK;
    }

    public Collection(CollectionPK collectionPK, Date dateAdded) {
        this.collectionPK = collectionPK;
        this.dateAdded = dateAdded;
    }

    public Collection(int bookId, int userId) {
        this.collectionPK = new CollectionPK(bookId, userId);
    }

    public CollectionPK getCollectionPK() {
        return collectionPK;
    }

    public void setCollectionPK(CollectionPK collectionPK) {
        this.collectionPK = collectionPK;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
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
        hash += (collectionPK != null ? collectionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Collection)) {
            return false;
        }
        Collection other = (Collection) object;
        if ((this.collectionPK == null && other.collectionPK != null) || (this.collectionPK != null && !this.collectionPK.equals(other.collectionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.vbencek.model.Collection[ collectionPK=" + collectionPK + " ]";
    }
    
}
