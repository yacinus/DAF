package com.example.testcompany.daf.modele;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.outils.GenerateRandomString;
import com.example.testcompany.daf.outils.MySQLiteOpenHelper;
import com.example.testcompany.daf.outils.MyTools;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;

public class AccesLocal {

    //PROPRIETES
    private final String nomBase = "daf.sqlite";
    private final Integer versionBase = 1;
    private final MySQLiteOpenHelper accesBD;
    private SQLiteDatabase bd;
    private final Controle controle;

    /**
     * CONSTRUCTEUR
     *
     * @param contexte
     */
    public AccesLocal(Context contexte) {
        accesBD = new MySQLiteOpenHelper(contexte, nomBase, null, versionBase);
        controle = Controle.getInstance(null);

    }

    /**
     * Ajouter un nouveau client
     *
     * @param client
     */
    public void ajoutClient(Client client, Localisation localisation) {
        bd = accesBD.getWritableDatabase();
        Log.d("client.getDate()", "*************client.getDate()*****************" + client.getDateAjout());
        String random = GenerateRandomString.randomString(4);
        String dateInteger = String.valueOf(MyTools.convertDateToInteger());
        int id = Integer.parseInt(dateInteger.concat(random));
        client.setDate(MyTools.convertDateToString(new Date()));
        String req = "insert into client (id, formeSociale, etablissement,categorie,nom,prenom,adresse,mail,tel,profession,date, idVendeur) values";
        req += "(\"" + id + "\",\"" + client.getFormeSociale() + "\",\"" + client.getEtablissement() + "\",\"" + client.getCategorie() + "\",\"" + client.getNom() + "\",\"" + client.getPrenom() + "\",\"" + client.getAdresse() + "\",\"" + client.getMail() + "\",\"" + client.getTel() + "\",\"" + client.getProfession() + "\",\"" + client.getDateAjout() + "\",\"" + client.getIdVendeur() + "\")";
        bd.execSQL(req);
        controle.addClientFireStore(client, id);
        ajoutLocation(localisation, id);
    }

    /**
     * AJOUT D'UNE LOCATION DANS LA BASE DE DONNEES
     *
     * @param location
     */
    private void ajoutLocation(Localisation location, int idClient) {
        bd = accesBD.getWritableDatabase();
        String random = GenerateRandomString.randomString(4);
        String dateInteger = String.valueOf(MyTools.convertDateToInteger());
        int id = Integer.parseInt(dateInteger.concat(random));
        String req = "insert into location (id, latitude, longitude, idClient) values";
        req += "(\"" + id + "\",\"" + location.getLatitude() + "\",\"" + location.getLongitude() + "\",\"" + idClient + "\")";
        bd.execSQL(req);
        controle.addLocalisationFireStore(id, location, idClient);
    }

    public void addSecteur(ArrayList<Localisation> lesPoints) {
        bd = accesBD.getWritableDatabase();
        clearTableSecteur();
        for (int i = 0; i < lesPoints.size(); i++) {
            int id = lesPoints.get(i).getId();
            Double latitude = lesPoints.get(i).getLatitude();
            Double longitude = lesPoints.get(i).getLongitude();
            String req = "insert into secteur (id, latitude, longitude) values";
            req += "(\"" + id + "\",\"" + latitude + "\",\"" + longitude + "\")";
            bd.execSQL(req);

        }
    }

    /**
     * Insérer une nouvelle vente dans la table vente et appeler la méthode recupIdVente
     *
     * @param vente
     * @param stock
     */
    public void ajouterVente(Vente vente, ArrayList<StockClient> stock) {

        bd = accesBD.getWritableDatabase();
        String random = GenerateRandomString.randomString(4);
        String dateInteger = String.valueOf(MyTools.convertDateToInteger());
        int id = Integer.parseInt(dateInteger.concat(random));
        vente.setDate(MyTools.convertDateToString(new Date()));
        String req = "insert into vente (id, idVendeur, idClient, total, accompte, solde, date) values";
        req += "(\"" + id + "\",\"" + vente.getIdVendeur() + "\",\"" + vente.getIdClient() + "\",\"" + vente.getTotal() + "\",\"" + vente.getAccompte() + "\",\"" + vente.getSolde() + "\",\"" + vente.getDate() + "\")";
        bd.execSQL(req);
        Log.d("ajouterVente()", "******************ajouterVente()***********************");
        controle.addUpdateVenteFireStore(id, vente);
        addStockClientidVente(stock, id);
        checkSituationClient(id, vente.getIdClient(), vente.getTotal(), vente.getSolde(), vente.getDate(), 0);
    }

    private void addStockClientidVente(ArrayList<StockClient> stock, int idVente) {
        bd = accesBD.getWritableDatabase();
        for (int i = 0; i < stock.size(); i++) {
            addRowStockClient(idVente, stock.get(i).getIdProduitStockClient(), stock.get(i).getQteOut(), stock.get(i).getPrixUnitaire());
            updateMyStock(stock.get(i).getIdProduitStockClient(), stock.get(i).getQteOut());
        }
    }

    public void modifierVente(Vente vente, ArrayList<StockClient> stockClient, ArrayList<StockClient> newStockClient) {
        String date = MyTools.convertDateToString(new Date());
        checkSituationClient(vente.getId(), vente.getIdClient(), vente.getTotal(), vente.getSolde(), date, 1);
        bd = accesBD.getWritableDatabase();
        String reqVente = "update vente set idClient = '" + vente.getIdClient() + "', total = '" + vente.getTotal() + "', accompte = '" + vente.getAccompte() + "', solde = '" + vente.getSolde() + "', date = '" + date + "' where id = '" + vente.getId() + "'";
        bd.execSQL(reqVente);
        vente.setDate(date);
        controle.addUpdateVenteFireStore(vente.getId(), vente);
        updateStockClient(vente.getId(), stockClient, newStockClient);
    }

    private void updateStockClient(int idVente, ArrayList<StockClient> stockClient, ArrayList<StockClient> newStockClient) {

        //Comparer les deux tableaux pour éliminer les anciens produits de stockClient et retourner
        //les quantités dans myStock
        for (int i = 0; i < stockClient.size(); i++) {
            for (int j = 0; j < newStockClient.size(); j++) {
                if (stockClient.get(i).getIdProduitStockClient() != newStockClient.get(j).getIdProduitStockClient()) {
                    if (j == newStockClient.size() - 1) {
                        deleteRowStockClient(idVente, stockClient.get(i).getIdProduitStockClient());
                        updateMyStock(stockClient.get(i).getIdProduitStockClient(), -stockClient.get(i).getQteOut());
                    }

                } else {
                    break;
                }
            }
        }

        for (int i = 0; i < newStockClient.size(); i++) {
            for (int j = 0; j < stockClient.size(); j++) {
                Log.d("******", "**********updateStockClient*********");
                //Si les id sont pareils => le produit va être modifié si sa qte != 0
                //ou bien ne rien faire
                // casser la boucle et passer au produit suivant de newStockClient
                if (newStockClient.get(i).getIdProduitStockClient() == stockClient.get(j).getIdProduitStockClient()) {
                    int idProduit = newStockClient.get(i).getIdProduitStockClient();
                    int oldQte = stockClient.get(j).getQteOut();
                    int newQte = newStockClient.get(i).getQteOut();
                    Double oldPrixUnitaire = stockClient.get(j).getPrixUnitaire();
                    Double newPrixUnitaire = newStockClient.get(i).getPrixUnitaire();
                    int qteFinal = stockClient.get(j).getQteOut();
                    if (oldQte != newQte) {
                        if (newQte != 0) {
                            int qteDifference = newQte - oldQte;
                            qteFinal = stockClient.get(j).getQteOut() + qteDifference;
                            stockClient.get(i).setQteOut(qteFinal);
                            updateRowStockClient(stockClient.get(j).getIdProduitStockClient(), qteFinal, newStockClient.get(i).getPrixUnitaire(), idVente);
                            updateMyStock(idProduit, qteDifference);
                        }
                    }

                    if (!oldPrixUnitaire.equals(newPrixUnitaire)) {
                        updateRowStockClient(stockClient.get(j).getIdProduitStockClient(), qteFinal, newStockClient.get(i).getPrixUnitaire(), idVente);
                    }
                    break;
                    //update mystock
                    //dans mystock il peut etre soit ajouté :
                    //int qte =  myStock.get(i).getQteIn() - qteDifference;
                    // soustrait soit créé ajouter la nouvelle
                } else {
                    //Si
                    if (j == stockClient.size() - 1) {
                        addRowStockClient(idVente, newStockClient.get(i).getIdProduitStockClient(), newStockClient.get(i).getQteOut(), newStockClient.get(i).getPrixUnitaire());
                        updateMyStock(newStockClient.get(i).getIdProduitStockClient(), newStockClient.get(i).getQteOut());
                    }
                    //update mystock
                    //dans mystock il peut etre soit ajouté :
                    //int qte =  myStock.get(i).getQteIn() - qteDifference;
                    // soustrait soit créé ajouter la nouvelle
                }
            }
        }
    }

    public void updateMyStock(int idProduit, int qteDifference) {
        int qteFinal;
        bd = accesBD.getReadableDatabase();
        String reqGet = "select idProduit, qteIn from mystock where idProduit = '" + idProduit + "'";
        Cursor curseur = bd.rawQuery(reqGet, null);
        curseur.moveToFirst();
        int idProd = curseur.getInt(0);
        if (idProd == idProduit) {
            int qteIn = curseur.getInt(1);
            qteFinal = qteIn - qteDifference;
            curseur.close();
            updateRowMyStock(idProduit, qteFinal, controle.getIdUser());
        }

    }

    private void updateRowMyStock(int idProduit, int qteFinal, int idVendeur) {
        bd = accesBD.getWritableDatabase();
        String reqUpdate = "update mystock set qteIn = '" + qteFinal + "' where idProduit = '" + idProduit + "'";
        bd.execSQL(reqUpdate);
        addUpdateRowMyStockFireStore(idProduit, qteFinal, idVendeur);
    }

    private void addUpdateRowMyStockFireStore(int idProd, int qteFinal, int idVendeur){
        bd = accesBD.getReadableDatabase();
        String req = "select * from mystock where mystock.idProduit = '"+idProd+"' and mystock.idVendeur = '"+idVendeur+"'";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (!curseur.isAfterLast()){
            int id = curseur.getInt(0);
            controle.addUpdateRowMyStockFireStore(id, idProd, qteFinal, idVendeur);
        }
        curseur.close();
    }

    private void addRowStockClient(int idVente, int idProduit, int qteOut, Double prixUnitaire) {
        bd = accesBD.getWritableDatabase();
        String random = GenerateRandomString.randomString(4);
        String dateInteger = String.valueOf(MyTools.convertDateToInteger());
        int id = Integer.parseInt(dateInteger.concat(random));
        String req = "insert into stockclient (id, idProduit, idVente, qteOut, prixUnitaire) values";
        req += "(\"" + id + "\",\"" + idProduit + "\",\"" + idVente + "\",\"" + qteOut + "\",\"" + prixUnitaire + "\")";
        bd.execSQL(req);
        controle.addUpdateRowStockClientFireStore(id, idVente, idProduit, qteOut, prixUnitaire);
    }

    //==============================================================================================

    private void updateRowStockClient(int idProduit, int qteFinal, Double prixUnitaire, int idVente) {
        bd = accesBD.getWritableDatabase();
        String req = "update stockclient set qteOut = '" + qteFinal + "', prixUnitaire = '" + prixUnitaire + "' where idProduit = '" + idProduit + "' and idVente = '" + idVente + "'";
        bd.execSQL(req);
        addUpdateRowStockClientFireStore(idProduit, qteFinal, prixUnitaire, idVente);
    }

    private void addUpdateRowStockClientFireStore(int idProd, int qteFinal, Double prixUnitaire, int idVente){
        bd = accesBD.getReadableDatabase();
        String req = "select * from stockclient where stockclient.idProduit = '"+idProd+"' and stockclient.idVente = '"+idVente+"'";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (!curseur.isAfterLast()){
            int id = curseur.getInt(0);
            controle.addUpdateRowStockClientFireStore(id, idVente, idProd, qteFinal, prixUnitaire);
        }
        curseur.close();
    }

    //==============================================================================================



    private void deleteRowStockClient(int idVente, int idProduit) {
        deleteRowStockClientFireStore(idProduit, idVente);
        bd = accesBD.getWritableDatabase();
        String reqDeleteStock = "delete from stockclient where idVente = '" + idVente + "' and idProduit = '" + idProduit + "'";
        bd.execSQL(reqDeleteStock);
    }

    private void deleteRowStockClientFireStore(int idProd, int idVente){
        bd = accesBD.getReadableDatabase();
        String req = "select * from stockclient where stockclient.idProduit = '"+idProd+"' and stockclient.idVente = '"+idVente+"'";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (!curseur.isAfterLast()){
            int id = curseur.getInt(0);
            controle.deleteRowStockClientFireStore(id);
        }
        curseur.close();
    }

    //==============================================================================================================

    public void modifierInfosClient(Client client, Localisation localisation) {
        bd = accesBD.getWritableDatabase();
        String req = "update client set formeSociale = '" + client.getFormeSociale() + "', etablissement = '" + client.getEtablissement() + "', categorie = '" + client.getCategorie() + "', nom = '" + client.getNom() + "', prenom = '" + client.getPrenom() + "', adresse = '" + client.getAdresse() + "', mail = '" + client.getMail() + "', profession = '" + client.getProfession().replace("\'", " ") + "' where id = '" + client.getId() + "'";
        bd.execSQL(req);
        controle.addClientFireStore(client, client.getId());
        updateLocation(client.getId(), localisation);
    }

    private void updateLocation(int idClient, Localisation localisation) {
        bd = accesBD.getWritableDatabase();
        String req = "update location set longitude = '" + localisation.getLongitude() + "', latitude = '" + localisation.getLatitude() + "' where idClient = '" + idClient + "'";
        bd.execSQL(req);
        updateRowLocationFireStore(idClient, localisation);
    }

    private void updateRowLocationFireStore(int idClient, Localisation localisation){
        bd = accesBD.getReadableDatabase();
        String req = "select * from location where location.idClient = '"+idClient+"'";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (!curseur.isAfterLast()){
            int id = curseur.getInt(0);
            controle.updateRowLocationFireStore(id, idClient, localisation);
        }
        curseur.close();
    }

    public ArrayList<Client> recupClients() {
        bd = accesBD.getReadableDatabase();
        ArrayList<Client> lesClients = new ArrayList<Client>();
        String req = "select * from client";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            int id = curseur.getInt(0);
            Log.d("id ", "**************************************************" + id);
            String formeSociale = curseur.getString(1);
            Log.d("formeSociale ", "**************************************************" + formeSociale);
            String ets = curseur.getString(2);
            Log.d("ets ", "**************************************************" + ets);
            String categorie = curseur.getString(3);
            Log.d("categorie ", "**************************************************" + categorie);
            String nom = curseur.getString(4);
            Log.d("nom ", "**************************************************" + nom);
            String prenom = curseur.getString(5);
            Log.d("prenom ", "**************************************************" + prenom);
            String adresse = curseur.getString(6);
            Log.d("adresse ", "**************************************************" + adresse);
            String adrMail = curseur.getString(7);
            Log.d("mail ", "**************************************************" + adrMail);
            String tel = curseur.getString(8);
            Log.d("tel ", "**************************************************" + tel);
            String profession = curseur.getString(9);
            Log.d("profession ", "**************************************************" + profession);
            String date = curseur.getString(10);
            Log.d("date ", "**************************************************" + date);
            int idVendeur = curseur.getInt(11);
            Log.d("idVendeur ", "**************************************************" + idVendeur);
            Client client = new Client(id, formeSociale, ets, categorie, nom, prenom, adresse, adrMail, tel, profession, date, idVendeur);
            lesClients.add(client);
            Log.d("curseur position ", "**************************************************" + lesClients);
            curseur.moveToNext();
        }
        curseur.close();
        return lesClients;
    }


    public ArrayList<ClientVente> recupVentesClients() {
        bd = accesBD.getReadableDatabase();
        ArrayList<ClientVente> ventesClients = new ArrayList<ClientVente>();
        String req = "select vente.id,vente.idvendeur,vente.idclient,client.etablissement,vente.total,vente.accompte,vente.solde,vente.date from vente inner join client on vente.idClient = client.id ORDER BY vente.idclient,vente.date DESC";
        Cursor curseur = bd.rawQuery(req, null);
        Log.d("CURSEUR : ", "******************************" + curseur);
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            int idVente = Integer.parseInt(curseur.getString(0));
            Log.d("idVente ", "**************************************************" + idVente);
            int idVendeur = Integer.parseInt(curseur.getString(1));
            Log.d("idVendeur ", "**************************************************" + idVendeur);
            int idClient = Integer.parseInt(curseur.getString(2));
            Log.d("idClient ", "**************************************************" + idClient);
            String etablissement = curseur.getString(3);
            Log.d("etablissement ", "**************************************************" + etablissement);
            Double total = curseur.getDouble(4);
            Log.d("total ", "**************************************************" + total);
            Double accompte = curseur.getDouble(5);
            Log.d("accompte ", "**************************************************" + accompte);
            Double solde = curseur.getDouble(6);
            Log.d("solde ", "**************************************************" + solde);
            String date = curseur.getString(7);
            Log.d("date ", "***********************date***************************" + date);
            ClientVente lesVentesClients = new ClientVente(0, idVente, idVendeur, idClient, etablissement, total, accompte, solde, date);
            ventesClients.add(lesVentesClients);
            Log.d("curseur position ", "**************************************************" + lesVentesClients.toString());
            curseur.moveToNext();
        }
        curseur.close();
        return ventesClients;
    }

    public String recupEtsClient(int idVente) {
        Log.d("recupNomClient() ", "*******RECUP NOM CLIENT***********");
        String etablissement = null;
        bd = accesBD.getReadableDatabase();
        String req = "select c.etablissement from client as c inner join vente as v on  c.id = v.idclient where v.id = " + idVente + "";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToLast();
        //si le dernier = apres le dernier  => la liste est vide. C'est pour cela qu'on ajoute la condition si dessous :
        if (!curseur.isAfterLast()) {
            etablissement = curseur.getString(0);
            //Log.d("idClient ", "**************************************************RecupVente " + idClient);
        }
        curseur.close();
        return etablissement;
    }

    public int recupIdClient(int idVente) {
        Log.d("recupIdClient() ", "*******RECUP ID CLIENT***********");
        int idClient = 0;
        bd = accesBD.getReadableDatabase();
        String req = "select c.id from client as c inner join vente as v on  c.id = v.idclient where v.id = " + idVente + "";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToLast();
        //si le dernier = apres le dernier  => la liste est vide. C'est pour cela qu'on ajoute la condition si dessous :
        if (!curseur.isAfterLast()) {
            idClient = curseur.getInt(0);
            //Log.d("idClient ", "**************************************************RecupVente " + idClient);
        }
        curseur.close();
        return idClient;
    }


    public Double recupAccompteClient(int idVente) {
        Log.d("recupIdClient() ", "*******RECUP ID CLIENT***********");
        Double accompte = 0.0;
        bd = accesBD.getReadableDatabase();
        String req = "select v.accompte from vente as v where v.id = " + idVente + "";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToLast();
        //si le dernier = apres le dernier  => la liste est vide. C'est pour cela qu'on ajoute la condition si dessous :
        if (!curseur.isAfterLast()) {
            accompte = curseur.getDouble(0);
        }
        curseur.close();
        return accompte;
    }


    public Double recupSoldeClient(int idVente) {
        Log.d("recupIdClient() ", "*******RECUP ID CLIENT***********");
        Double solde = 0.0;
        bd = accesBD.getReadableDatabase();
        String req = "select v.solde from vente as v where v.id = " + idVente + "";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToLast();
        //si le dernier = apres le dernier  => la liste est vide. C'est pour cela qu'on ajoute la condition si dessous :
        if (!curseur.isAfterLast()) {
            solde = curseur.getDouble(0);
        }
        curseur.close();
        return solde;
    }

    public Vente recupVenteDuClient(int idVente) {
        Log.d("idVente = ", "**********idVente*************" + idVente);
        bd = accesBD.getReadableDatabase();
        Vente venteDuClient = new Vente();
        String req = "select * from vente where vente.id = " + idVente + "";
        Cursor curseur = bd.rawQuery(req, null);
        Log.d("CURSEUR : ", "******************************" + curseur);
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            int idVe = Integer.parseInt(curseur.getString(0));
            Log.d("idVente ", "**************************************************" + idVe);
            int idVendeur = Integer.parseInt(curseur.getString(1));
            Log.d("idVendeur ", "**************************************************" + idVendeur);
            int idCl = Integer.parseInt(curseur.getString(2));
            Log.d("idClient ", "**************************************************" + idCl);
            Float total = curseur.getFloat(3);
            Log.d("total ", "**************************************************" + total);
            Float accompte = curseur.getFloat(4);
            Log.d("accompte ", "**************************************************" + accompte);
            Float solde = curseur.getFloat(5);
            Log.d("solde ", "**************************************************" + solde);
            String date = curseur.getString(6);
            Log.d("date ", "**************************************************" + date);
            venteDuClient = new Vente(idVe, idVendeur, idCl, total, accompte, solde, date);
            curseur.moveToNext();
        }
        curseur.close();
        return venteDuClient;
    }

    public ArrayList<StockClient> recupStockClient(int idVente) {
        Log.d("recupStockClient : ", "++++++++++++++++++++++++++++++++++++++++");
        bd = accesBD.getReadableDatabase();
        ArrayList<StockClient> leStock = new ArrayList<StockClient>();
        String req = "select s.id,s.idProduit,s.idVente,s.qteOut,s.prixUnitaire from stockclient as s inner join vente as v on s.idVente = v.id where s.idVente ='" + idVente + "'";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            int id = curseur.getInt(0);
            Log.d("id ", "**************************************************" + id);
            int idProduit = Integer.parseInt(curseur.getString(1));
            Log.d("idProduit ", "**************************************************" + idProduit);
            int idVe = Integer.parseInt(curseur.getString(2));
            Log.d("idVente ", "**************************************************" + idVe);
            int qteOut = Integer.parseInt(curseur.getString(3));
            Log.d("qteOut ", "**************************************************" + qteOut);
            Double prixUnitaire = Double.parseDouble(curseur.getString(4));
            Log.d("prixUnitaire ", "**************************************************" + prixUnitaire);
            StockClient stock = new StockClient(id, idProduit, idVente, qteOut, prixUnitaire);
            leStock.add(stock);
            Log.d("curseur position ", "**************************************************" + leStock);
            curseur.moveToNext();
        }
        curseur.close();
        return leStock;
    }

    public ArrayList<Produit> recupProduits() {
        Log.d("recupProduits() ", "**************************************************" + toString());
        bd = accesBD.getReadableDatabase();
        ArrayList<Produit> lesProduits = new ArrayList<Produit>();
        String req = "select * from produits";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            Log.d("while() ", "**************************************************");
            int id = Integer.parseInt(curseur.getString(0));
            Log.d("id ", "**************************************************" + id);
            String code = curseur.getString(1);
            Log.d("code ", "**************************************************" + code);
            String designation = curseur.getString(2);
            Log.d("designation ", "**************************************************" + designation);
            Date date = MyTools.convertStringToDate(curseur.getString(5));
            Log.d("date ", "**************************************************" + date);
            Double prixUnitaire = curseur.getDouble(6);
            Log.d("prixUnitaire ", "**************************************************" + prixUnitaire);
            Produit produit = new Produit(id, code, designation, null, null, null, prixUnitaire);
            lesProduits.add(produit);
            Log.d("curseur position ", "**************************************************" + lesProduits.toString());
            curseur.moveToNext();
        }
        curseur.close();
        return lesProduits;
    }

    public ArrayList<Produit> recupProduitsMyStock() {
        Log.d("recupProduits() ", "**************************************************" + toString());
        bd = accesBD.getReadableDatabase();
        ArrayList<Produit> lesProduits = new ArrayList<Produit>();
        String req = "select * from produits as p inner join mystock as s on p.id = s.idProduit where s.qteIn != 0";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            Log.d("while() ", "**************************************************");
            int id = Integer.parseInt(curseur.getString(0));
            Log.d("id ", "**************************************************" + id);
            String code = curseur.getString(1);
            Log.d("code ", "**************************************************" + code);
            String designation = curseur.getString(2);
            Log.d("designation ", "**************************************************" + designation);
            String date = curseur.getString(5);
            Log.d("date ", "**************************************************" + date);
            Double prixUnitaire = curseur.getDouble(6);
            Log.d("prixUnitaire ", "**************************************************" + prixUnitaire);
            Produit produit = new Produit(id, code, designation, null, null, null, prixUnitaire);
            lesProduits.add(produit);
            Log.d("curseur position ", "**************************************************" + lesProduits.toString());
            curseur.moveToNext();
        }
        curseur.close();
        return lesProduits;
    }

    public Produit recupProduit(int idProduit) {
        Produit leProduit = null;
        Log.d("recupProduit() ", "**************************************************" + toString());
        bd = accesBD.getReadableDatabase();
        String req = "select id,code,designation,prix from produits where produits.id = " + idProduit + "";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            Log.d("while() ", "**************************************************");
            int id = Integer.parseInt(curseur.getString(0));
            Log.d("id ", "**************************************************" + id);
            String code = curseur.getString(1);
            Log.d("code ", "**************************************************" + code);
            String designation = curseur.getString(2);
            Log.d("designation ", "**************************************************" + designation);
            Double prixUnitaire = curseur.getDouble(3);
            Log.d("prixUnitaire ", "**************************************************" + prixUnitaire);
            leProduit = new Produit(id, code, designation, null, null, null, prixUnitaire);
            Log.d("curseur position ", "**************************************************" + leProduit.toString());
            curseur.moveToNext();
        }
        curseur.close();
        return leProduit;

    }

    public int recupQteStock(int idProduit) {

        bd = accesBD.getReadableDatabase();
        int qteIn = 0;
        String req = "select qteIn from mystock where idProduit = '" + idProduit + "'";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (curseur.getCount() != 0) {
            qteIn = curseur.getInt(0);
        }
        curseur.close();
        return qteIn;
    }

    public Client recupInfosClient(int idClient) {
        bd = accesBD.getReadableDatabase();
        String req = "select * from client where id = '" + idClient + "'";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        int id = curseur.getInt(0);
        String formeSociale = curseur.getString(1);
        String etablissement = curseur.getString(2);
        String categorie = curseur.getString(3);
        String nom = curseur.getString(4);
        String prenom = curseur.getString(5);
        String adresse = curseur.getString(6);
        String mail = curseur.getString(7);
        String tel = curseur.getString(8);
        String profession = curseur.getString(9);
        String date = curseur.getString(10);
        int idVendeur = curseur.getInt(11);
        Client client = new Client(id, formeSociale, etablissement, categorie, nom, prenom, adresse, mail, tel, profession, date, idVendeur);
        curseur.close();
        return client;
    }

    public ArrayList<ClientVente> recupVentesClient(int idClien){

        bd = accesBD.getReadableDatabase();
        ArrayList<ClientVente> ventesClients = new ArrayList<ClientVente>();
        String req = "select vente.id,vente.idvendeur,vente.idclient,client.etablissement,vente.total,vente.accompte,vente.solde,vente.date from vente inner join client on vente.idClient = client.id where vente.idClient = '"+idClien+"' ORDER BY vente.idclient,vente.date DESC";
        Cursor curseur = bd.rawQuery(req, null);
        Log.d("CURSEUR : ", "******************************" + curseur);
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            int idVente = Integer.parseInt(curseur.getString(0));
            Log.d("idVente ", "**************************************************" + idVente);
            int idVendeur = Integer.parseInt(curseur.getString(1));
            Log.d("idVendeur ", "**************************************************" + idVendeur);
            int idClient = Integer.parseInt(curseur.getString(2));
            Log.d("idClient ", "**************************************************" + idClient);
            String etablissement = curseur.getString(3);
            Log.d("etablissement ", "**************************************************" + etablissement);
            Double total = curseur.getDouble(4);
            Log.d("total ", "**************************************************" + total);
            Double accompte = curseur.getDouble(5);
            Log.d("accompte ", "**************************************************" + accompte);
            Double solde = curseur.getDouble(6);
            Log.d("solde ", "**************************************************" + solde);
            String date = curseur.getString(7);
            Log.d("date ", "***********************date***************************" + date);
            ClientVente lesVentesClients = new ClientVente(0, idVente, idVendeur, idClient, etablissement, total, accompte, solde, date);
            ventesClients.add(lesVentesClients);
            Log.d("curseur position ", "**************************************************" + lesVentesClients.toString());
            curseur.moveToNext();
        }
        curseur.close();
        return ventesClients;

    }

    public ArrayList<StockClient> recupAllStocksClients() {

        bd = accesBD.getReadableDatabase();
        ArrayList<StockClient> leStock = new ArrayList<StockClient>();
        String req = "select * from stockClient";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            int id = Integer.parseInt(curseur.getString(0));
            Log.d("id ", "**************************************************" + id);
            int idProduit = Integer.parseInt(curseur.getString(1));
            Log.d("idProduit ", "**************************************************" + idProduit);
            int idVente = Integer.parseInt(curseur.getString(2));
            Log.d("idVente ", "**************************************************" + idVente);
            int qteOut = Integer.parseInt(curseur.getString(3));
            Log.d("qteOut ", "**************************************************" + qteOut);
            Double prixUnitaire = Double.parseDouble(curseur.getString(4));
            Log.d("prixUnitaire ", "**************************************************" + prixUnitaire);
            StockClient stock = new StockClient(id, idProduit, idVente, qteOut, prixUnitaire);
            leStock.add(stock);
            Log.d("curseur position ", "**************************************************" + leStock);
            curseur.moveToNext();
        }
        curseur.close();
        return leStock;
    }

    public ArrayList<MyStock> recupAllMyStock() {
        ArrayList<MyStock> leStock = new ArrayList<MyStock>();
        if (checkCountTableMyStock()) {
            bd = accesBD.getReadableDatabase();
            String req = "select * from mystock";
            Cursor curseur = bd.rawQuery(req, null);
            curseur.moveToFirst();
            while (!curseur.isAfterLast()) {
                int id = Integer.parseInt(curseur.getString(0));
                Log.d("id ", "**************************************************" + id);
                int idProduit = Integer.parseInt(curseur.getString(1));
                Log.d("idProduit ", "**************************************************" + idProduit);
                int qteIn = Integer.parseInt(curseur.getString(2));
                Log.d("qteIn ", "**************************************************" + qteIn);
                int idVendeur = Integer.parseInt(curseur.getString(3));
                Log.d("idVendeur ", "**************************************************" + idVendeur);
                MyStock stock = new MyStock(id, idProduit, qteIn, idVendeur);
                leStock.add(stock);
                Log.d("curseur position ", "**************************************************" + leStock);
                curseur.moveToNext();
            }
            curseur.close();
            return leStock;
        }
        return leStock;
    }

    public ArrayList<Localisation> recupSecteur() {
        bd = accesBD.getWritableDatabase();
        ArrayList<Localisation> secteur = new ArrayList<>();
        String req = "select * from secteur order by id asc";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            Double latitude = curseur.getDouble(1);
            Double longitude = curseur.getDouble(2);
            Localisation localisation = new Localisation(0, latitude, longitude, 0);
            secteur.add(localisation);
            curseur.moveToNext();
        }
        curseur.close();
        return secteur;
    }

    private boolean checkCountTableMyStock() {
        bd = accesBD.getReadableDatabase();
        boolean bool;
        String req = "select * from mystock";
        Cursor curseur = bd.rawQuery(req, null);
        bool = curseur.getCount() != 0;
        curseur.close();
        return bool;
    }

    //==============================================================================================
    //OPERATIONS FIREBASE-FIRESTORE
    //==============================================================================================

    public ArrayList<Client> recupNouveauxClients() {
        bd = accesBD.getReadableDatabase();
        ArrayList<Client> clientArrayList = new ArrayList<>();
        String dateWtime = MyTools.convertDateToStringWtime(new Date());
        String req = "select * from client";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            String dateBdd = MyTools.removeTime(curseur.getString(10));
            if (dateWtime.equals(dateBdd)) {
                int id = curseur.getInt(0);
                String formeSociale = curseur.getString(1);
                String etablissement = curseur.getString(2);
                String categorie = curseur.getString(3);
                String nom = curseur.getString(4);
                String prenom = curseur.getString(5);
                String adresse = curseur.getString(6);
                String mail = curseur.getString(7);
                String tel = curseur.getString(8);
                String profession = curseur.getString(9);
                String date = curseur.getString(10);
                int idVendeur = curseur.getInt(11);
                Client client = new Client(id, formeSociale, etablissement, categorie, nom, prenom, adresse, mail, tel, profession, date, idVendeur);
                clientArrayList.add(client);
            }
            curseur.moveToNext();
        }
        curseur.close();
        return clientArrayList;
    }

    public ArrayList<Localisation> recupNouvellesLocalisations(ArrayList<Client> nouveauxClients) {
        ArrayList<Localisation> locations = new ArrayList<>();
        bd = accesBD.getReadableDatabase();

        for (int i = 0; i < nouveauxClients.size(); i++) {

            String req = "select * from location where idClient = '" + nouveauxClients.get(i).getId() + "'";
            Cursor curseur = bd.rawQuery(req, null);
            if (curseur.getCount() != 0) {
                curseur.moveToLast();
                int id = curseur.getInt(0);
                Double latitude = Double.valueOf(curseur.getString(1));
                Double longitude = Double.valueOf(curseur.getString(2));
                int idClient = curseur.getInt(3);
                Localisation localisation = new Localisation(id, latitude, longitude, idClient);
                locations.add(localisation);
            }
            curseur.close();
        }

        return locations;
    }

    public ArrayList<Localisation> recupLocalisations() {
        ArrayList<Localisation> locations = new ArrayList<>();
        bd = accesBD.getReadableDatabase();

        String req = "select * from location";
        Cursor curseur = bd.rawQuery(req, null);
        if (curseur.getCount() != 0) {
            curseur.moveToFirst();
            while (!curseur.isAfterLast()) {
                int id = curseur.getInt(0);
                Double latitude = Double.valueOf(curseur.getString(1));
                Double longitude = Double.valueOf(curseur.getString(2));
                int idClient = curseur.getInt(3);
                Localisation localisation = new Localisation(id, latitude, longitude, idClient);
                locations.add(localisation);
                curseur.moveToNext();
            }
            curseur.close();
        }
        return locations;
    }

    public GeoPoint recupLocalisationClient(int idClient) {
        bd = accesBD.getReadableDatabase();
        GeoPoint point = new GeoPoint(0,0);
        String req = "select latitude,longitude from location where idClient = '"+idClient+"'";
        Cursor curseur = bd.rawQuery(req, null);
        if (curseur.getCount() != 0) {
            curseur.moveToFirst();
            if (!curseur.isAfterLast()) {
                Double latitude = Double.valueOf(curseur.getString(0));
                Double longitude = Double.valueOf(curseur.getString(1));
                point = new GeoPoint(latitude,longitude);
            }
            curseur.close();
        }
        return point;
    }

    public void addMyNewStock(ArrayList<MyStock> myStock) {
        bd = accesBD.getWritableDatabase();
        String req = "delete from mystock";
        bd.execSQL(req);
        for (int i = 0; i < myStock.size(); i++) {
            int id = myStock.get(i).getId();
            int idProduit = myStock.get(i).getIdProduit();
            int idVendeur = myStock.get(i).getIdVendeur();
            int qteIn = myStock.get(i).getQteIn();
            addNewRowMyStock(id, idProduit, qteIn, idVendeur);
        }
    }

    private void addNewRowMyStock(int id, int idProduit, int qteIn, int idVendeur) {
        bd = accesBD.getWritableDatabase();
        String req = "insert into mystock (id, idProduit, qteIn, idVendeur) values";
        req += "(\"" + id + "\",\"" + idProduit + "\",\"" + qteIn + "\",\"" + idVendeur + "\")";
        bd.execSQL(req);
    }

    public void refreshProducts(ArrayList<Produit> produits) {
        bd = accesBD.getWritableDatabase();
        clearTableProduits();
        for (int i = 0; i < produits.size(); i++) {
            int id = produits.get(i).getId();
            String code = produits.get(i).getCode();
            String designation = produits.get(i).getDesignation();
            String categorie = produits.get(i).getCategorie();
            String expiration = produits.get(i).getExpiration();
            String dateEntree = produits.get(i).getDateentree();
            Double prixUnitaire = produits.get(i).getPrixUnitaire();
            addRowProduit(id, code, designation, categorie, expiration, dateEntree, prixUnitaire);
        }
    }

    private void addRowProduit(int id, String code, String designation, String categorie, String expiration, String dateEntree, Double prixUnitaire) {
        bd = accesBD.getWritableDatabase();
        String req = "insert into produits (id, code, designation, categorie, expiration, dateEntree, prix) values";
        req += "(\"" + id + "\",\"" + code + "\",\"" + designation + "\", \"" + categorie + "\",\"" + expiration + "\",\"" + dateEntree + "\",\"" + prixUnitaire + "\")";
        bd.execSQL(req);
    }

    public void addNewClients(ArrayList<Client> clients) {
        clearTableClients();
        bd = accesBD.getWritableDatabase();
        for (int i = 0; i < clients.size(); i++) {
            String req = "insert into client (id, formesociale, etablissement,categorie,nom,prenom,adresse,mail,tel,profession,date, idVendeur) values";
            req += "(\"" + clients.get(i).getId() + "\",\"" + clients.get(i).getFormeSociale() + "\",\"" + clients.get(i).getEtablissement() + "\",\"" + clients.get(i).getCategorie() + "\",\"" + clients.get(i).getNom() + "\",\"" + clients.get(i).getPrenom() + "\",\"" + clients.get(i).getAdresse() + "\",\"" + clients.get(i).getMail() + "\",\"" + clients.get(i).getTel() + "\",\"" + clients.get(i).getProfession() + "\",\"" + clients.get(i).getDateAjout() + "\",\"" + clients.get(i).getIdVendeur() + "\")";
            bd.execSQL(req);
        }
    }

    public void addNewLocations(ArrayList<Localisation> locations) {
        clearTableLocations();
        bd = accesBD.getWritableDatabase();
        for (int i = 0; i < locations.size(); i++) {
            String req = "insert into location (id, latitude, longitude, idClient) values";
            req += "(\"" + locations.get(i).getId() + "\",\"" + locations.get(i).getLatitude() + "\",\"" + locations.get(i).getLongitude() + "\",\"" + locations.get(i).getIdClient() + "\")";
            bd.execSQL(req);
        }
    }

    public void addSituationClient(ArrayList<Situation> situations) {
        clearTableSituation();
        bd = accesBD.getWritableDatabase();
        for (int i = 0; i < situations.size(); i++) {

            String req = "insert into situation (id, idClient, total, solde, date) values";
            req += "(\"" + situations.get(i).getId() + "\",\"" + situations.get(i).getIdClient() + "\",\"" + situations.get(i).getTotal() + "\",\"" + situations.get(i).getSolde() + "\",\"" + situations.get(i).getDate() + "\")";
            bd.execSQL(req);

        }
    }

    public void addVentesClients(ArrayList<Vente> ventes) {
        clearTableVentes();
        for (int i = 0; i < ventes.size(); i++) {

            String req = "insert into vente (id, idVendeur, idClient, total, accompte, solde, date) values";
            req += "(\"" + ventes.get(i).getId() + "\",\"" + ventes.get(i).getIdVendeur() + "\",\"" + ventes.get(i).getIdClient() + "\"," +
                    "\"" + ventes.get(i).getTotal() + "\",\"" + ventes.get(i).getAccompte() + "\"," +
                    "\"" + ventes.get(i).getSolde() + "\",\"" + ventes.get(i).getDate() + "\")";
            bd.execSQL(req);

        }

    }

    public void addStockClient(ArrayList<StockClient> stockClients) {
        clearTableStockClient();
        int id;
        int idVente;
        int idProduit;
        int qteOut;
        Double prixUnitaire;

        for (int i = 0; i < stockClients.size(); i++) {

            id = stockClients.get(i).getId();
            idVente = stockClients.get(i).getIdVente();
            idProduit = stockClients.get(i).getIdProduitStockClient();
            qteOut = stockClients.get(i).getQteOut();
            prixUnitaire = stockClients.get(i).getPrixUnitaire();

            addRowStockClientFromFireBase(id, idVente, idProduit, qteOut, prixUnitaire);

        }

    }

    private void addRowStockClientFromFireBase(int id, int idVente, int idProduit, int qteOut, Double prixUnitaire) {
        bd = accesBD.getWritableDatabase();
        String req = "insert into stockclient (id, idProduit, idVente, qteOut, prixUnitaire) values";
        req += "(\"" + id + "\",\"" + idProduit + "\",\"" + idVente + "\",\"" + qteOut + "\",\"" + prixUnitaire + "\")";
        bd.execSQL(req);
    }

    public ArrayList<Situation> recupSituationsClients(){
        ArrayList<Situation> situations = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String req = "select situation.id, situation.idclient, situation.total, situation.solde, situation.date, client.etablissement from situation inner join client on situation.idclient = client.id order by situation.date";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        while(!curseur.isAfterLast()){
            int id = curseur.getInt(0);
            int idClient = curseur.getInt(1);
            Double total= curseur.getDouble(2);
            Double solde = curseur.getDouble(3);
            String date = curseur.getString(4);
            String ets = curseur.getString(5);
            Situation situation = new Situation(id, idClient, total, solde, date, ets);
            situations.add(situation);
            curseur.moveToNext();
        }
        curseur.close();
        return situations;
    }

    private void checkSituationClient(int idVente, int idClient, Float total, Float solde, String date, int number){

        bd = accesBD.getReadableDatabase();
        String req = "select idclient from situation where idclient = '"+idClient+"'";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (!curseur.isAfterLast()){
            if (number == 0){
                updateSituationClientNewSale(idVente, idClient, total, solde, date);
            }else{
                updateSituationClientOldSale(idVente, idClient, total, solde, date);
            }
        }else{
            addRowSituationClient(idClient, total, solde, date);
        }
        curseur.close();
    }

    private Vente checkVente(int idVente){
        Vente vente = new Vente();
        bd = accesBD.getReadableDatabase();
        String req = "select total, solde from vente where id = '"+idVente+"'";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToLast();
        if ((!curseur.isAfterLast())){
            Float total = curseur.getFloat(0);
            Float solde = curseur.getFloat(1);
            vente = new Vente(0,0,0,total,null,solde,null);
        }
        curseur.close();
        return vente;
    }

    public boolean checkSaleMemberShip(int idVente, int idVendeur){
        boolean memberShip = false;
        bd = accesBD.getReadableDatabase();
        String req = "select count(*) from vente where vente.id = '"+idVente+"' and vente.idvendeur = '"+idVendeur+"'";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (curseur.getCount() > 0){

            if (curseur.getInt(0) != 0){
                memberShip = true;
            }

        }
        curseur.close();

        return memberShip;
    }

    private void updateSituationClientNewSale(int idVente, int idClient, Float totale, Float sold, String date) {

        bd = accesBD.getReadableDatabase();

            String req = "select total,solde from situation where situation.idclient = '" + idClient + "'";
            Cursor curseur = bd.rawQuery(req, null);
            curseur.moveToFirst();
            if (!curseur.isAfterLast()) {
                Float totalSituation = curseur.getFloat(0);
                Float soldeSituation = curseur.getFloat(1);

                totalSituation += totale;
                soldeSituation += sold;

                updateRowSituationClient(idClient, totalSituation, soldeSituation, date);
            }
            curseur.close();
    }

    private void updateSituationClientOldSale(int idVente, int idClient, Float totale, Float sold, String date) {

        Vente vente =  checkVente(idVente);

        bd = accesBD.getReadableDatabase();

        String req = "select total,solde from situation where situation.idclient = '" + idClient + "'";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (!curseur.isAfterLast()) {
            Float totalSituation = curseur.getFloat(0);
            Float soldeSituation = curseur.getFloat(1);
            Float diffTotal = totale - vente.getTotal();
            Float diffSolde = sold - vente.getSolde();

            if (diffTotal != 0){
                totalSituation += diffTotal;
            }
            if (diffSolde != 0){
                soldeSituation += diffSolde;
            }
            updateRowSituationClient(idClient, totalSituation, soldeSituation, date);
        }
        curseur.close();
    }

    private void updateRowSituationClient(int idClient, Float total, Float solde, String date){
        bd = accesBD.getWritableDatabase();
        String req = "update situation set total = '"+total+"', solde = '"+solde+"', date = '"+date+"' where idclient = '"+idClient+"'";
        bd.execSQL(req);
        Situation situation = new Situation(0, idClient, total.doubleValue(), solde.doubleValue(), date, null);
        addUpdateRowSituationFirestore(situation);
    }

        private void addRowSituationClient(int idClient, Float total, Float solde, String date){
        bd = accesBD.getWritableDatabase();
        String random = GenerateRandomString.randomString(4);
        String dateInteger = String.valueOf(MyTools.convertDateToInteger());
        int id = Integer.parseInt(dateInteger.concat(random));
        String req = "insert into situation (id, idclient, total, solde, date) values";
        req += "(\"" + id +"\",\"" + idClient + "\",\"" + total + "\",\"" + solde + "\",\"" + date + "\")";
        bd.execSQL(req);
        Situation situation = new Situation(id, idClient, total.doubleValue(), solde.doubleValue(), date, null);
        addUpdateRowSituationFirestore(situation);
    }

    private void addUpdateRowSituationFirestore(Situation situation){

        bd = accesBD.getReadableDatabase();
        if (situation.getId() == 0){
            String req = "select * from situation where situation.idclient = '"+situation.getIdClient()+"'";
            Cursor curseur = bd.rawQuery(req, null);
            curseur.moveToFirst();
            if (!curseur.isAfterLast()){
                int id = curseur.getInt(0);
                situation.setId(id);
                controle.addUpdateRowSituationFirestore(situation);
            }
            curseur.close();
        }else{
                controle.addUpdateRowSituationFirestore(situation);
        }


    }


    //==============================================================================================

    public void clearTableClients() {
        bd = accesBD.getWritableDatabase();
        String req = "delete from client";
        bd.execSQL(req);
    }

    public void clearTableProduits() {
        bd = accesBD.getWritableDatabase();
        String req = "delete from produits";
        bd.execSQL(req);
    }

    public void clearTableLocations() {
        bd = accesBD.getWritableDatabase();
        String req = "delete from location";
        bd.execSQL(req);

    }

    public void clearTableSituation() {
        bd = accesBD.getWritableDatabase();
        String req = "delete from situation";
        bd.execSQL(req);

    }

    public void clearTableVentes() {
        bd = accesBD.getWritableDatabase();
        String req = "delete from vente";
        bd.execSQL(req);

    }

    public void clearTableSecteur() {
        bd = accesBD.getWritableDatabase();
        String req = "delete from secteur";
        bd.execSQL(req);

    }

    public void clearTableStockClient() {
        bd = accesBD.getWritableDatabase();
        String req = "delete from stockclient";
        bd.execSQL(req);

    }

    public ArrayList<Vente> recupVentes() {
        bd = accesBD.getReadableDatabase();
        ArrayList<Vente> lesVentes = new ArrayList<Vente>();
        String req = "select * from vente";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            int id = Integer.parseInt(curseur.getString(0));
            Log.d("id ", "**************************************************" + id);
            int idVendeur = curseur.getInt(1);
            Log.d("idVendeur ", "**************************************************" + idVendeur);
            int idClient = curseur.getInt(2);
            Log.d("idClient ", "**************************************************" + idClient);
            Float total = curseur.getFloat(3);
            Log.d("total ", "**************************************************" + total);
            Float accompte = curseur.getFloat(4);
            Log.d("accompte ", "**************************************************" + accompte);
            Float solde = curseur.getFloat(5);
            Log.d("solde ", "**************************************************" + solde);
            String date = curseur.getString(6);
            Log.d("date ", "**************************************************" + date);
            Vente vente = new Vente(id, idVendeur, idClient, total, accompte, solde, date);
            lesVentes.add(vente);
            Log.d("curseur position ", "**************************************************" + lesVentes);
            curseur.moveToNext();
        }
        curseur.close();
        return lesVentes;
    }
}