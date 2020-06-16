package com.example.testcompany.daf.modele;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class User {

    //propriétés
    private int id;
    private String username;
    private String password;
    private int idSuperviseur;
    private boolean superviseur;

    public User(int id, String username, String password, int idSuperviseur, boolean superviseur) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.idSuperviseur = idSuperviseur;
        this.superviseur = superviseur;
    }

    public User(){

    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSuperviseur() {
        return superviseur;
    }

}
