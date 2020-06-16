package com.example.testcompany.daf.modele;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Client {

    //propriétés
    private int id;
    private String formeSociale;
    private String etablissement;
    private String categorie;
    private String nom;
    private String prenom;
    private String adresse;
    private String mail;
    private String tel;
    private String profession;
    private String dateAjout;
    private int idVendeur;


    public Client(int id,String formeSociale, String etablissement, String categorie, String nom, String prenom, String adresse, String mail, String tel, String profession, String dateAjout, int idVendeur) {
        this.id = id;
        this.formeSociale = formeSociale;
        this.etablissement = etablissement;
        this.categorie = categorie;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.mail = mail;
        this.tel = tel;
        this.profession = profession;
        this.dateAjout = dateAjout;
        this.idVendeur = idVendeur;
    }

    public Client(){

    }

    public void setDate(String dateAjout) {
        this.dateAjout = dateAjout;
    }

    public String getFormeSociale() {
        return formeSociale;
    }

    public int getId(){return id;}

    public String getEtablissement() {
        return etablissement;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getMail() {
        return mail;
    }

    public String getTel() {
        return tel;
    }

    public String getProfession() {
        return profession;
    }

    public String getDateAjout() {
        return dateAjout;
    }

    public int getIdVendeur() {
        return idVendeur;
    }

}
