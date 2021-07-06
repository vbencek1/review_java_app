/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.beans;

/**
 *
 * @author Tino
 */
public class TestnaKlasaKomentar {
    private int id;
    private String korisnik;
    private String komentar;
    private int ocjena;

    public TestnaKlasaKomentar(int id, String korisnik, String komentar, int ocjena) {
        this.id = id;
        this.korisnik = korisnik;
        this.komentar = komentar;
        this.ocjena = ocjena;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public int getOcjena() {
        return ocjena;
    }

    public void setOcjena(int ocjena) {
        this.ocjena = ocjena;
    }
    
}
