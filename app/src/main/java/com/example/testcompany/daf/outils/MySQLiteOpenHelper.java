package com.example.testcompany.daf.outils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    //PROPRIETES
    private final String creationLocation="create table location ("
            + "id INTEGER PRIMARY KEY,"
            + "latitude TEXT NOT NULL,"
            + "longitude TEXT NOT NULL,"
            + "idClient INTEGER NOT NULL);";

    private final String creationSecteur="create table secteur ("
            + "id INTEGER NOT NULL,"
            + "latitude TEXT NOT NULL,"
            + "longitude TEXT NOT NULL);";

    //********************AJOUTER CODE CLIENT EN LOCAL ET EN DISTANT
    private final String creationClient="create table client ("
            + "id INTEGER PRIMARY KEY,"
            + "formesociale TEXT NOT NULL,"
            + "etablissement TEXT NOT NULL,"
            + "categorie TEXT NOT NULL,"
            + "nom TEXT NOT NULL,"
            + "prenom TEXT NOT NULL,"
            + "adresse TEXT NOT NULL,"
            + "mail TEXT NOT NULL,"
            + "tel TEXT NOT NULL,"
            + "profession TEXT NOT NULL,"
            + "date TEXT NOT NULL,"
            + "idVendeur INTEGER NOT NULL);";

    private final String creationProduit="create table produits ("
            + "id INTEGER PRIMARY KEY,"
            + "code TEXT NOT NULL,"
            + "designation TEXT NOT NULL,"
            + "categorie TEXT NOT NULL,"
            + "expiration TEXT NOT NULL,"
            + "dateentree TEXT NOT NULL,"
            + "prix TEXT NOT NULL);";

    private final String creationStockClient="create table stockclient ("
            + "id INTEGER PRIMARY KEY,"
            + "idProduit TEXT NOT NULL,"
            + "idVente INTEGER NOT NULL,"
            + "qteOut TEXT NOT NULL,"
            + "prixUnitaire TEXT NOT NULL);";

    private final String creationMyStock="create table mystock ("
            + "id INTEGER PRIMARY KEY,"
            + "idProduit TEXT NOT NULL,"
            + "qteIn TEXT NOT NULL,"
            + "idVendeur INTEGER NOT NULL);";

    private final String creationVentes="create table vente ("
            + "id INTEGER PRIMARY KEY,"
            + "idvendeur INTEGER NOT NULL,"
            + "idclient INTEGER NOT NULL,"
            + "total TEXT NOT NULL,"
            + "accompte TEXT NOT NULL,"
            + "solde TEXT NOT NULL,"
            + "date TEXT NOT NULL);";

    private final String creationSituation="create table situation ("
            + "id INTEGER PRIMARY KEY,"
            + "idclient INTEGER NOT NULL,"
            + "total TEXT NOT NULL,"
            + "solde TEXT NOT NULL,"
            + "date TEXT NOT NULL);";

    /**
     * CONSTRUCTEUR
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * SI CHANGEMENT DE BASE DE DONNEES
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(creationLocation);
        sqLiteDatabase.execSQL(creationSecteur);
        sqLiteDatabase.execSQL(creationClient);
        sqLiteDatabase.execSQL(creationProduit);
        sqLiteDatabase.execSQL(creationStockClient);
        sqLiteDatabase.execSQL(creationMyStock);
        sqLiteDatabase.execSQL(creationVentes);
        sqLiteDatabase.execSQL(creationSituation);
    }

    /**
     * SI CHANGEMENT DE VERSION
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
