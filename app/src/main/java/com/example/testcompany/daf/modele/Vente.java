package com.example.testcompany.daf.modele;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Vente {

    private int id;
    private int idVendeur;
    private int idClient;
    private Float total;
    private Float accompte;
    private Float solde;
    private String date;

    public Vente(int id, int idVendeur, int idClient, Float total, Float accompte, Float solde, String date) {
        this.id = id;
        this.idVendeur = idVendeur;
        this.idClient = idClient;
        this.total = total;
        this.accompte = accompte;
        this.solde = solde;
        this.date = date;
    }

    public Vente(){

    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIdVendeur() {
        return idVendeur;
    }

    public Float getAccompte() {
        return accompte;
    }

    public int getId() {
        return id;
    }

    public int getIdClient() {
        return idClient;
    }

    public Float getTotal() {
        return total;
    }

    public Float getSolde() {
        return solde;
    }

    public String getDate() {
        return date;
    }

}
