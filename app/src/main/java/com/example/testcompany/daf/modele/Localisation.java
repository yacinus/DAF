package com.example.testcompany.daf.modele;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Localisation {

    private int id;
    private Double latitude;
    private Double longitude;
    private int idClient;

    public Localisation(int id, Double latitude, Double longitude, int idClient){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.idClient = idClient;
    }

    public Localisation(){

    }

    public int getId() {
        return id;
    }

    public Double getLatitude() { return latitude; }

    public Double getLongitude() { return longitude; }

    public int getIdClient(){
        return idClient;
    }
    
}
