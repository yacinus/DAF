package com.example.testcompany.daf.modele;
Test
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Produit {

    private int id;
    private String code;
    private String designation;
    private String categorie;
    private String expiration;
    private String dateentree;
    private Double prixUnitaire;

    public Produit(int id, String code, String designation, String categorie, String expiration, String dateentree, Double prixUnitaire) {
        this.id = id;
        this.code = code;
        this.designation = designation;
        this.categorie = categorie;
        this.expiration = expiration;
        this.dateentree = dateentree;
        this.prixUnitaire = prixUnitaire;
    }

    public Produit(){

    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDesignation() {
        return designation;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getExpiration() {
        return expiration;
    }

    public String getDateentree() {
        return dateentree;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setId(int id) {
        this.id = id;
    }

}
