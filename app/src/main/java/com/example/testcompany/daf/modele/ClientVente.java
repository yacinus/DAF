package com.example.testcompany.daf.modele;

public class ClientVente {

    private int id;
    private int idVente;
    private int idVendeur;
    private int idClient;
    private String etablissement;
    private Double total;
    private Double accompte;
    private Double solde;
    private String date;

    public ClientVente(int id, int idVente, int idVendeur, int idClient, String etablissement, Double total, Double accompte, Double solde, String date) {
        this.id = id;
        this.idVente = idVente;
        this.idVendeur = idVendeur;
        this.idClient = idClient;
        this.etablissement = etablissement;
        this.total = total;
        this.accompte = accompte;
        this.solde = solde;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getIdVente() {
        return idVente;
    }

    public int getIdVendeur() {
        return idVendeur;
    }

    public int getIdClient() {
        return idClient;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public Double getTotal() {
        return total;
    }

    public Double getAccompte() {
        return accompte;
    }

    public Double getSolde() {
        return solde;
    }

    public String getDate() {
        return date;
    }

    public void setEtablissementClient(String etablissementClient) {
        this.etablissement = etablissementClient;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdVente(int idVente) {
        this.idVente = idVente;
    }

    public void setIdVendeur(int idVendeur) {
        this.idVendeur = idVendeur;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setAccompte(Double accompte) {
        this.accompte = accompte;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public void setDate(String date) {
        this.date = date;
    }
}