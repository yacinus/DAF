package com.example.testcompany.daf.modele;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MyStock {

    private int id;
    private int idProduit;
    private int qteIn;
    private int idVendeur;

    public MyStock(int id, int idProduit, int qteIn, int idVendeur) {
        this.id = id;
        this.idProduit = idProduit;
        this.qteIn = qteIn;
        this.idVendeur = idVendeur;
    }

    public MyStock(){

    }

    public int getId() {
        return id;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public int getQteIn() {
        return qteIn;
    }

    public int getIdVendeur() {
        return idVendeur;
    }

    public void setQteIn(int qteIn) {
        this.qteIn = qteIn;
    }
}
