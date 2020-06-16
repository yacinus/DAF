package com.example.testcompany.daf.modele;

public class Stock {


    private int idProduit;
    private String designation;
    private String code;
    private int qteRestante;

    public Stock(int idProduit, String designation, String code, int qteRestante) {
        this.idProduit = idProduit;
        this.designation = designation;
        this.code = code;
        this.qteRestante = qteRestante;
    }


    public int getIdProduit() {
        return idProduit;
    }

    public String getDesignation() {
        return designation;
    }

    public String getCode() {
        return code;
    }

    public int getQteRestante() {
        return qteRestante;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setQteRestante(int qteRestante) {
        this.qteRestante = qteRestante;
    }
}
