package com.example.testcompany.daf.modele;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.outils.AsyncResponseClients;
import com.example.testcompany.daf.outils.AsyncResponseLocations;
import com.example.testcompany.daf.outils.AsyncResponseMyStock;
import com.example.testcompany.daf.outils.AsyncResponseProducts;
import com.example.testcompany.daf.outils.AsyncResponseSector;
import com.example.testcompany.daf.outils.AsyncResponseSituation;
import com.example.testcompany.daf.outils.AsyncResponseStockClient;
import com.example.testcompany.daf.outils.AsyncResponseSupervisor;
import com.example.testcompany.daf.outils.AsyncResponseVentes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.GeoPoint;
import java.util.ArrayList;

public class AccesDistant {

    private final Controle controle;
    private final Context context;

    public AccesDistant(Context context) {
        this.controle = Controle.getInstance(null);
        this.context = context;
    }

    public void verifierProfil(User profil) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String username = profil.getUsername();
        String password = profil.getPassword();

        db.collection("users")
                .whereEqualTo("username", username.trim())
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                User profil = document.toObject(User.class);
                                controle.setUser(profil);
                                controle.accessGranted();
                            }
                            if (task.getResult().isEmpty()) {
                                controle.noProfil();
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void downloadProduits(AsyncResponseProducts asyncResponseProducts) {
        ArrayList<Produit> products = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("produits")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                // Produit produit = new Produit(document.toObject(Produit.class).getId(),document.toObject(Produit.class).getCode(),document.toObject(Produit.class).getDesignation(),document.toObject(Produit.class).getCategorie()
                                // , document.toObject(Produit.class).getExpiration(), document.toObject(Produit.class).getDateentree(), document.toObject(Produit.class).getPrixUnitaire().doubleValue());
                                Produit produit = document.toObject(Produit.class);
                                products.add(produit);
                            }
                            task.getResult().isEmpty();//VOTRE STOCK N'A PAS ENCORE ETE ATTRIBUE
                            asyncResponseProducts.onCallback(products);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    public void downloadStock(AsyncResponseMyStock asyncResponseMyStock) {
        ArrayList<MyStock> myStock = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int idUser = controle.getIdUser();

        db.collection("mystock")
                .whereEqualTo("idVendeur", idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                MyStock stock = document.toObject(MyStock.class);
                                myStock.add(stock);
                            }
                            asyncResponseMyStock.onCallBack(myStock);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void downloadClientsOfSupervisor(ArrayList<Integer> ids, AsyncResponseClients asyncResponseClients) {

        ArrayList<Client> clients = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

            for (int i=0;i<ids.size();i++){
                db.collection("clients")
                        .whereEqualTo("idVendeur", ids.get(i))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("TAG", document.getId() + " => " + document.getData());
                                        Client client = document.toObject(Client.class);
                                        clients.add(client);
                                    }
                                    asyncResponseClients.onCallback(clients);
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
    }


    public void downloadClients(AsyncResponseClients asyncResponseClients) {
        ArrayList<Client> clients = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int idUser = controle.getIdUser();

        db.collection("clients")
                .whereEqualTo("idVendeur", idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                Client client = document.toObject(Client.class);
                                clients.add(client);
                            }
                            asyncResponseClients.onCallback(clients);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void downloadLocationClients(ArrayList<Client> clients, AsyncResponseLocations asyncResponseLocations) {
        ArrayList<Localisation> locations = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (int i = 0; i < clients.size(); i++) {

            db.collection("localisations")
                    .whereEqualTo("idClient", clients.get(i).getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    // Produit produit = new Produit(document.toObject(Produit.class).getId(),document.toObject(Produit.class).getCode(),document.toObject(Produit.class).getDesignation(),document.toObject(Produit.class).getCategorie()
                                    // , document.toObject(Produit.class).getExpiration(), document.toObject(Produit.class).getDateentree(), document.toObject(Produit.class).getPrixUnitaire().doubleValue());
                                    Localisation location = document.toObject(Localisation.class);
                                    locations.add(location);
                                }
                                asyncResponseLocations.onCallback(locations);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    public void downloadMySector(AsyncResponseSector asyncResponseSector) {
        ArrayList<Localisation> points = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int idUser = controle.getIdUser();

        db.collection("secteur")
                .whereEqualTo("idVendeur", idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                int id = document.getLong("id").intValue();
                                GeoPoint geoPoint = document.getGeoPoint("point");
                                Localisation point = new Localisation(id, geoPoint.getLatitude(), geoPoint.getLongitude(), 0);
                                points.add(point);
                            }
                            asyncResponseSector.onCallBack(points);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void downloadSituationClients(AsyncResponseSituation asyncResponseSituation) {
        controle.getClients();
        ArrayList<Client> clients = controle.getLesClients();
        ArrayList<Situation> situations = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (int i = 0; i < clients.size(); i++) {

            db.collection("situation")
                    .whereEqualTo("idClient", clients.get(i).getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    Situation situation = document.toObject(Situation.class);
                                    situations.add(situation);
                                }
                                asyncResponseSituation.onCallback(situations);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    public void checkUsersOfSupervisor(User profil, AsyncResponseSupervisor asyncResponseSupervisor) {
        ArrayList<Integer> ids = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("idSuperviseur", profil.getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    ids.add(document.getLong("id").intValue());
                                }
                                asyncResponseSupervisor.onCallback(ids);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
    }

    public void downloadSales(ArrayList<Integer> userIds, AsyncResponseVentes asyncResponseVentes) {
        ArrayList<Vente> ventes = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (int i=0;i<userIds.size();i++){

            db.collection("ventes")
                    .whereEqualTo("idVendeur", userIds.get(i))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    Vente vente = document.toObject(Vente.class);
                                    ventes.add(vente);
                                }
                                asyncResponseVentes.onCallback(ventes);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    public void downloadStockClient(ArrayList<Vente> ventes, AsyncResponseStockClient asyncResponseStockClient) {
        ArrayList<StockClient> stockClients = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (int i=0;i<ventes.size();i++){

            db.collection("stockclient")
                    .whereEqualTo("idVente", ventes.get(i).getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    StockClient stockClient = document.toObject(StockClient.class);
                                    stockClients.add(stockClient);
                                }
                                asyncResponseStockClient.onCallback(stockClients);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }
}
