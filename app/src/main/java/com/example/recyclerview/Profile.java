package com.example.recyclerview;

import java.io.Serializable;

public class Profile implements Serializable {
    private String nom;
    private String prenom;
    private String dateNai;
    private String adresse;
    private String Tel;
    private String imgUrl;
    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }



    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDateNai() {
        return dateNai;
    }

    public void setDateNai(String dateNai) {
        this.dateNai = dateNai;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Profile(String nom, String prenom, String adresse, String tel, String dateNai, String imgUrl) {
        this.adresse = adresse;
        this.imgUrl = imgUrl;
        this.nom = nom;
        this.dateNai= dateNai;
        this.Tel = tel;
        this.prenom = prenom;
    }
}

