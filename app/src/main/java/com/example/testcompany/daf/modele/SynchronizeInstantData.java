package com.example.testcompany.daf.modele;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.testcompany.daf.controleur.Controle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class SynchronizeInstantData {

    private final Controle controle;
    private final Context context;


    public SynchronizeInstantData(Context context) {
        this.controle = Controle.getInstance(null);
        this.context = context;
    }

    public void ajoutClient(Client client, int id){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String formeSociale = client.getFormeSociale();
        String etablissement = client.getEtablissement();
        String categorie = client.getCategorie();
        String nom = client.getNom();
        String prenom = client.getPrenom();
        String adresse = client.getAdresse();
        String tel = client.getTel();
        String mail = client.getMail();
        String profession = client.getProfession();
        String dateAjout = client.getDateAjout();
        int idVendeur = client.getIdVendeur();

        Map<String, Object> leClient = new HashMap<>();
        leClient.put("id", id);
        leClient.put("formeSociale", formeSociale);
        leClient.put("etablissement", etablissement);
        leClient.put("categorie", categorie);
        leClient.put("nom", nom);
        leClient.put("prenom", prenom);
        leClient.put("adresse", adresse);
        leClient.put("mail", mail);
        leClient.put("tel", tel);
        leClient.put("profession", profession);
        leClient.put("dateAjout", dateAjout);
        leClient.put("idVendeur", idVendeur);

        db.collection("clients")
                .document(String.valueOf(id))
                .set(leClient)
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

    public void addLocalisationClient(int id, Localisation localisation, int idClient){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

            Double longitude = localisation.getLongitude();
            Double latitude = localisation.getLatitude();

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

    public void addUpdateVente(int id, Vente vente){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

            int idVendeur = vente.getIdVendeur();
            int idClient = vente.getIdClient();
            Float total = vente.getTotal();
            Float accompte = vente.getAccompte();
            Float solde = vente.getSolde();
            String date = vente.getDate();

            Map<String, Object> laVente = new HashMap<>();
            laVente.put("id", id);
            laVente.put("idVendeur", idVendeur);
            laVente.put("idClient", idClient);
            laVente.put("total", total);
            laVente.put("accompte", accompte);
            laVente.put("solde", solde);
            laVente.put("date", date);


            db.collection("ventes")
                    .document(String.valueOf(id))
                    .set(laVente)
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

    public void addUpdateMyStock(int id, int idProduit, int qteFinal, int idVendeur){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> stockObject = new HashMap<>();
            stockObject.put("id", id);
            stockObject.put("idProduit", idProduit);
            stockObject.put("idVendeur",idVendeur);
            stockObject.put("qteIn",qteFinal);

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

    public void addUpdateStockClient(int id, int idVente, int idProduit, int qteFinal, Double prixUnitaire){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> stockClients = new HashMap<>();
            stockClients.put("id", id);
            stockClients.put("idProduit", idProduit);
            stockClients.put("idVente", idVente);
            stockClients.put("qteOut", qteFinal);
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

    public void deleteRowStockClientFireStore(int id){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("stockclient")
                .document(String.valueOf(id))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
    }

    public void updateRowLocationFireStore(int id, int idClient, Localisation localisation){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> location = new HashMap<>();
            location.put("id", id);
            location.put("longitude", localisation.getLongitude());
            location.put("latitude", localisation.getLatitude());
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

    public void addUpdateSituation(Situation situation){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        int id = situation.getId();
        int idClient = situation.getIdClient();
        Double total = situation.getTotal();
        Double solde = situation.getSolde();
        String date = situation.getDate();

        Map<String, Object> situationFinal = new HashMap<>();
        situationFinal.put("id", id);
        situationFinal.put("idClient", idClient);
        situationFinal.put("total", total);
        situationFinal.put("solde", solde);
        situationFinal.put("date", date);

        db.collection("situation")
                .document(String.valueOf(id))
                .set(situationFinal)
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
