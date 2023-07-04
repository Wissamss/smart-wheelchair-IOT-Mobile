package com.example.recyclerview;

import java.io.Serializable;

public class Contact implements Serializable {
    private String nom;
    private String prenom;
    private String service;
    private String email;
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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Contact(String nom, String prenom, String email, String tel, String service, String imgUrl) {
        this.email = email;
        this.imgUrl = imgUrl;
        this.nom = nom;
        this.service = service;
        this.Tel = tel;
        this.prenom = prenom;
    }
}
