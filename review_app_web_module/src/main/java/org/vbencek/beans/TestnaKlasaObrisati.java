/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans;

import java.util.Date;

/**
 *
 * @author Tino
 */
public class TestnaKlasaObrisati {
    
    private int id;
    private String naziv;
    private int godina;
    private String autor;
    private String Izdavac;
    private String opis;
    private double ocjena;
    private int brojOcjena;
    private Date zadnjiReview;

    public TestnaKlasaObrisati(int id, String naziv, int godina, String autor, String Izdavac, String opis, double ocjena, int brojOcjena, Date zadnjiReview) {
        this.id = id;
        this.naziv = naziv;
        this.godina = godina;
        this.autor = autor;
        this.Izdavac = Izdavac;
        this.opis = opis;
        this.ocjena = ocjena;
        this.brojOcjena = brojOcjena;
        this.zadnjiReview = zadnjiReview;
    }

    public Date getZadnjiReview() {
        return zadnjiReview;
    }

    public void setZadnjiReview(Date zadnjiReview) {
        this.zadnjiReview = zadnjiReview;
    }
    
    
    
    
    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public int getGodina() {
        return godina;
    }

    public String getAutor() {
        return autor;
    }

    public String getIzdavac() {
        return Izdavac;
    }

    public String getOpis() {
        return opis;
    }

    public double getOcjena() {
        return ocjena;
    }

    public int getBrojOcjena() {
        return brojOcjena;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setGodina(int godina) {
        this.godina = godina;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setIzdavac(String Izdavac) {
        this.Izdavac = Izdavac;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setOcjena(double ocjena) {
        this.ocjena = ocjena;
    }

    public void setBrojOcjena(int brojOcjena) {
        this.brojOcjena = brojOcjena;
    }
    
    
    
}
