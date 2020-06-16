package com.example.testcompany.daf.modele;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.testcompany.daf.controleur.Controle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SynchronizeData {

    private final Controle controle;


    public SynchronizeData() {
        this.controle = Controle.getInstance(null);
    }

    public void envoiNouveauxClients(ArrayList<Client> nouveauxClients){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (int i=0;i<nouveauxClients.size();i++){

            int id = nouveauxClients.get(i).getId();
            String formeSociale = nouveauxClients.get(i).getFormeSociale();
            String etablissement = nouveauxClients.get(i).getEtablissement();
            String categorie = nouveauxClients.get(i).getCategorie();
            String nom = nouveauxClients.get(i).getNom();
            String prenom = nouveauxClients.get(i).getPrenom();
            String adresse = nouveauxClients.get(i).getAdresse();
            String tel = nouveauxClients.get(i).getTel();
            String mail = nouveauxClients.get(i).getMail();
            String profession = nouveauxClients.get(i).getProfession();
            String dateAjout = nouveauxClients.get(i).getDateAjout();
            int idVendeur = nouveauxClients.get(i).getIdVendeur();

            Map<String, Object> client = new HashMap<>();
            client.put("id", id);
            client.put("formeSociale", formeSociale);
            client.put("etablissement", etablissement);
            client.put("categorie", categorie);
            client.put("nom", nom);
            client.put("prenom", prenom);
            client.put("adresse", adresse);
            client.put("mail", mail);
            client.put("tel", tel);
            client.put("profession", profession);
            client.put("dateAjout", dateAjout);
            client.put("idVendeur", idVendeur);

            db.collection("clients")
                    .document(String.valueOf(id))
                    .set(client)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);
                        }
                    });
        }
    }

    public void envoiLocalisationsClients(ArrayList<Localisation> localisations){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (int i=0;i<localisations.size();i++){

            int id = localisations.get(i).getId();
            Double longitude = localisations.get(i).getLongitude();
            Double latitude = localisations.get(i).getLatitude();
            int idClient = localisations.get(i).getIdClient();

            Map<String, Object> location = new HashMap<>();
            location.put("id", id);
            location.put("longitude", longitude);
            location.put("latitude", latitude);
            location.put("idClient", idClient);

            db.collection("localisations")
                    .document(String.valueOf(id))
                    .set(location)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);
                        }
                    });
        }
    }

    public void envoiVentes(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        controle.recupVentes();
        ArrayList<Vente> ventes = controle.retournerVentes();

        for (int i=0;i<ventes.size();i++){

            int id = ventes.get(i).getId();
            int idVendeur = ventes.get(i).getIdVendeur();
            int idClient = ventes.get(i).getIdClient();
            Float total = ventes.get(i).getTotal();
            Float accompte = ventes.get(i).getAccompte();
            Float solde = ventes.get(i).getSolde();
            String date = ventes.get(i).getDate();

            Map<String, Object> vente = new HashMap<>();
            vente.put("id", id);
            vente.put("idVendeur", idVendeur);
            vente.put("idClient", idClient);
            vente.put("total", total);
            vente.put("accompte", accompte);
            vente.put("solde", solde);
            vente.put("date", date);


            db.collection("ventes")
                    .document(String.valueOf(id))
                    .set(vente)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);
                        }
                    });
        }
    }

    public void envoiStockClient(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        controle.getStockClient();
        ArrayList<StockClient> stockClient = controle.getLeStockClient();

        for (int i=0;i<stockClient.size();i++){

            int id = stockClient.get(i).getId();
            int idProduit = stockClient.get(i).getIdProduit();
            int idVente = stockClient.get(i).getIdVente();
            int qteOut = stockClient.get(i).getQteOut();
            Double prixUnitaire = stockClient.get(i).getPrixUnitaire();

            Map<String, Object> stockClients = new HashMap<>();
            stockClients.put("id", id);
            stockClients.put("idProduit", idProduit);
            stockClients.put("idVente", idVente);
            stockClients.put("qteOut", qteOut);
            stockClients.put("prixUnitaire", prixUnitaire);

            db.collection("stockclient")
                    .document(String.valueOf(id))
                    .set(stockClients)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);
                        }
                    });
        }
    }

    public void envoiMyStock(){
        ArrayList<MyStock> myStock;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        controle.getMyStock();
        myStock = controle.getMyFinalStock();

        for (int i=0;i<myStock.size();i++){

            int id = myStock.get(i).getId();
            int idProduit = myStock.get(i).getIdProduit();
            int idVendeur = myStock.get(i).getIdVendeur();
            int qteIn = myStock.get(i).getQteIn();

            Map<String, Object> stockObject = new HashMap<>();
            stockObject.put("id", id);
            stockObject.put("idProduit", idProduit);
            stockObject.put("idVendeur",idVendeur);
            stockObject.put("qteIn",qteIn);

            db.collection("mystock")
                    .document(String.valueOf(id))
                    .set(stockObject)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);
                        }
                    });

        }
    }
}
