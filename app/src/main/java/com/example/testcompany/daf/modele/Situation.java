package com.example.testcompany.daf.modele;

public class Situation {

    private int id;
    private int idClient;
    private Double total;
    private Double solde;
    private String date;
    private String ets;

    public Situation(int id, int idClient, Double total, Double solde, String date, String ets) {
        this.id = id;
        this.idClient = idClient;
        this.total = total;
        this.solde = solde;
        this.date = date;
        this.ets = ets;
    }

    public Situation(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEts(String ets) {
        this.ets = ets;
    }

    public int getId() {
        return id;
    }

    public int getIdClient() {
        return idClient;
    }

    public Double getTotal() {
        return total;
    }

    public Double getSolde() {
        return solde;
    }

    public String getDate() {
        return date;
    }

    public String getEts() {
        return ets;
    }
}
