package com.example.testcompany.daf.modele;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class StockClient {

    private int id;
    private int idProduit;
    private int idVente;
    private int qteOut = 0;
    private Double prixUnitaire;

    public StockClient(int id, int idProduit, int idVente, int qteOut, Double prixUnitaire) {
        this.id = id;
        this.idProduit = idProduit;
        this.idVente = idVente;
        this.qteOut = qteOut;
        this.prixUnitaire = prixUnitaire;
    }

    public StockClient(){

    }

    public int getId() {
        return id;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public int getIdVente() {
        return idVente;
    }

    public int getIdProduitStockClient() {
        return idProduit;
    }

    public int getQteOut() {
        return qteOut;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setQteOut(int qteOut) {
        this.qteOut = qteOut;
    }

}
