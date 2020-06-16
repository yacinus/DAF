package com.example.testcompany.daf.controleur;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.testcompany.daf.R;
import com.example.testcompany.daf.modele.AccesDistant;
import com.example.testcompany.daf.modele.AccesLocal;
import com.example.testcompany.daf.modele.Client;
import com.example.testcompany.daf.modele.ClientVente;
import com.example.testcompany.daf.modele.Localisation;
import com.example.testcompany.daf.modele.MyStock;
import com.example.testcompany.daf.modele.NetworkConnectivity;
import com.example.testcompany.daf.modele.Produit;
import com.example.testcompany.daf.modele.Situation;
import com.example.testcompany.daf.modele.StockClient;
import com.example.testcompany.daf.modele.SynchronizeData;
import com.example.testcompany.daf.modele.SynchronizeInstantData;
import com.example.testcompany.daf.modele.ThreadCheckVariablesFirestore;
import com.example.testcompany.daf.modele.User;
import com.example.testcompany.daf.modele.Vente;
import com.example.testcompany.daf.outils.AsyncResponseClients;
import com.example.testcompany.daf.outils.AsyncResponseLocations;
import com.example.testcompany.daf.outils.AsyncResponseMyStock;
import com.example.testcompany.daf.outils.AsyncResponseProducts;
import com.example.testcompany.daf.outils.AsyncResponseSector;
import com.example.testcompany.daf.outils.AsyncResponseSituation;
import com.example.testcompany.daf.outils.AsyncResponseStockClient;
import com.example.testcompany.daf.outils.AsyncResponseSupervisor;
import com.example.testcompany.daf.outils.AsyncResponseVentes;
import com.example.testcompany.daf.vue.AccueilActivity;
import com.example.testcompany.daf.vue.ListClientsActivity;
import com.example.testcompany.daf.vue.MainActivity;
import com.example.testcompany.daf.vue.NewSaleActivity;
import com.example.testcompany.daf.vue.StockActivity;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public final class Controle {

    private static Controle instance = null;
    public int idVente = 0;
    private int idClient = 0;
    private Double accompteClient;
    private Double soldeClient;
    public int qteRestante;
    private int sizeOfResultProduct;
    private int sizeOfResultClient;
    private String etablissementClient;
    private int qteStock;
    private static User profil;
    private static Client client;
    public static Vente vente;
    private static MyStock myStock;
    private static SynchronizeData synchronizeData;
    private static AccesDistant accesDistant;
    private static SynchronizeInstantData synchronizeInstantData;
    private static Produit produit;
    private static Produit leProduit;
    private static Localisation location;
    private ArrayList<Client> nouveauxClients;
    private ArrayList<Localisation> localisations;
    private static Context contexte;
    private static AccesLocal accesLocal;
    private ArrayList<Client> lesClients = new ArrayList<>();
    private Vente laVenteDuClient;
    private ArrayList<ClientVente> lesVentesClient = new ArrayList<>();
    private ArrayList<Localisation> lesPoints = new ArrayList<>();
    private ArrayList<MyStock> arrayMyStock = new ArrayList<>();
    private ArrayList<Localisation> secteur = new ArrayList<>();
    private ArrayList<StockClient> leStockClient = new ArrayList<>();
    private ArrayList<StockClient> arrayStockClient = new ArrayList<>();
    private ArrayList<Produit> lesProduits = new ArrayList<>();
    private ArrayList<Vente> ventes = new ArrayList<>();
    private ArrayList<Localisation> locations = new ArrayList<>();
    private ArrayList<Situation> arraySituation = new ArrayList<>();
    private ArrayList<Integer> usersId = new ArrayList<>();
    private static NetworkConnectivity networkConnectivity;
    private boolean usersIdIsFull = false;
    private boolean lesClientsIsFull = false;
    private boolean locationsIsFull = false;
    private boolean arraySituationIsFull = false;
    private boolean ventesIsFull = false;
    private boolean arrayStockClientIsFull = false;
    private boolean arrayMyStockIsFull = false;
    private boolean lesProduitsIsFull = false;
    private boolean lesPointsIsFull = false;
    private boolean check = false;
    private boolean isSupervisor = false;

    /**
     * constructeur privé
     */
    private Controle(){
        super();
    }


    //--------------------------------------------------------------------------------------------
    //---------------------------PRODUIT----------------------------------------------------------
    //--------------------------------------------------------------------------------------------

    /**
     * récupérer l'id du produit depuis AutoCompleteProductsAdapter et l'injecter dans StockActivity
     * @param produit
     */
    public void setIdProdStockActivity(Produit produit){
        Controle.produit = produit;
        ((StockActivity)contexte).ecouteAjout();
    }

    /**
     * récupérer l'id du produit depuis AutoCompleteProductsAdapter et l'injecter dans NewSaleActivity
     * @param produit
     */
    public void setIdProdNewSaleActivity(Produit produit){
        Controle.produit = produit;
        Log.d("****************","**********produit.getId()************"+produit.getId());
        ((NewSaleActivity)contexte).ecouteAjout();
    }

    /**
     * récupérer la taille du résultat de la rechercher
     * depuis AutoCompleteProductsAdapter et l'injecter dans StockActivity
     * @param sizeOfResultProduct
     */
    public void checkSizeOfResultProductStock (int sizeOfResultProduct){
        this.sizeOfResultProduct = sizeOfResultProduct;
        //Log.d("****************","**********sizeOfResultProduct************"+sizeOfResultProduct);
        ((StockActivity)contexte).ecouteAjout();
    }

    /**
     *  récupérer la taille du résultat de la rechercher
     *      * depuis AutoCompleteProductsAdapter et l'injecter dans NewSaleActivity
     * @param sizeOfResultProduct
     */
    public void checkSizeOfResultProductSale (int sizeOfResultProduct){
        this.sizeOfResultProduct = sizeOfResultProduct;
        //Log.d("****************","**********sizeOfResultProduct************"+sizeOfResultProduct);
    }

    /**
     * retourner la valeur de la taille du résultat de la recherche Adapter sur Produit
     * @return
     */
    public int getSizeOfResultProduct(){
        return sizeOfResultProduct;
    }

    /**
     * récupérer la taille du résultat de la rechercher
     * depuis AutoCompleteClientAdapter et l'injecter dans NewSaleActivity
     * @param sizeOfResultClient
     */
    public void checkSizeOfResultClient (int sizeOfResultClient){
        this.sizeOfResultClient = sizeOfResultClient;
        Log.d("****************","**********sizeOfResultClient******wscdlifvnd**"+sizeOfResultClient);
    }

    /**
     * retourner la valeur de la taille du résultat de la recherche Adapter sur Client
     * @return
     */
     public int getSizeOfResultClient(){
        return sizeOfResultClient;
    }

    /**
     * Retourner la valeur de l'id de l'objet Produit
     * @return
     */
    public int getIdProd(){
        return produit.getId();
    }

    /**
     * Retourner la valeur du prix unitiare du produit
     */
    public Double getPrixProduit(){
        return produit.getPrixUnitaire();
    }

    /**
     * Retourner la valeur du code de l'objet Produit
     * @return
     */
    public String getCodeProd(){
        return produit.getCode();
    }

    /**
     * Retourner la valeur de désignation de l'objet Produit
     * @return
     */
    public String getDesignation(){
        return produit.getDesignation();
    }

    /**
     * Récupérer la liste de tous les produits depuis la BDD Locale
     */
    public void getProduits(){

        //********ACCES DISTANT*******************
        //Log.d("verifierProfil() ", "**************************************************"+toString());
        //accesDistant = new AccesDistant();
        //accesDistant.envoi("getProduits", new JSONArray());
        //****************************************

        //********ACCES LOCAL*********************
        accesLocal = new AccesLocal(contexte);
        Log.d("getProduits() ", "**************************************************"+toString());
        lesProduits = accesLocal.recupProduits();
        //****************************************

    }

    /**
     * Retourner un ArrayList<Produit> qui contient tous les produits
     * @return
     */
    public ArrayList<Produit> getLesProduits(){
        return lesProduits;
    }

    /**
     * Récupérer la liste de tous les produits depuis la BDD Locale
     */
    public void getProduitsMyStock(){

        //********ACCES DISTANT*******************
        //Log.d("verifierProfil() ", "**************************************************"+toString());
        //accesDistant = new AccesDistant();
        //accesDistant.envoi("getProduits", new JSONArray());
        //****************************************

        //********ACCES LOCAL*********************
        accesLocal = new AccesLocal(contexte);
        Log.d("getProduits() ", "**************************************************"+toString());
        lesProduits = accesLocal.recupProduitsMyStock();
        //****************************************

    }

    /**
     * Retourner un ArrayList<Produit> qui contient tous les produits
     * @return
     */
    public ArrayList<Produit> getLesProduitsMyStock(){
        return lesProduits;
    }


    /**
     * Récuperer un objet Produit depuis la BDD Locale selon idProduit
     * @param idProduit
     */
    public void getProduit(int idProduit){

        //********ACCES DISTANT*******************
        //Log.d("verifierProfil() ", "**************************************************"+toString());
        //accesDistant = new AccesDistant();
        //accesDistant.envoi("getProduits", new JSONArray());
        //****************************************

        //********ACCES LOCAL*********************
        accesLocal = new AccesLocal(contexte);
        leProduit = accesLocal.recupProduit(idProduit);
        //****************************************

    }

    /**
     * Retourner l'objet Produit récupéré de la BDD Locale
     * @return
     */
    public Produit getLeProduit(){
        return leProduit;
    }

    //--------------------------------------------------------------------------------------------
    //---------------------------MY STOCK----------------------------------------------------------
    //--------------------------------------------------------------------------------------------

    public void addUpdateMyStock(int idProduit, int qteIn){
        //********ACCES LOCAL*********************
        accesLocal = new AccesLocal(contexte);
        Log.d("getStock() ", "**************************************************"+toString());
        accesLocal.updateMyStock(idProduit, -qteIn);
        //****************************************
    }

    /**
     * Récupérer tout le MyStock depuis la BDD
     */
    public void getMyStock(){

        //********ACCES DISTANT*******************
        //Log.d("verifierProfil() ", "**************************************************"+toString());
        //accesDistant = new AccesDistant();
        //accesDistant.envoi("getProduits", new JSONArray());
        //****************************************

        //********ACCES LOCAL*********************
        accesLocal = new AccesLocal(contexte);
        Log.d("getStock() ", "**************************************************"+toString());
        arrayMyStock = accesLocal.recupAllMyStock();
        //****************************************
    }

    /**
     * Récupérer tous le Stock de l'objet MyStock
     * @return
     */
    public ArrayList<MyStock> getMyFinalStock(){
        return arrayMyStock;
    }

    /**
     * Retourner la valeur de l'idProduit de l'objet MyStock
     * @return
     */
    public int getIdProduitMyStock(){
        return myStock.getIdProduit();
    }

    /**
     * Retourner la valeur de la qteIn de l'objet MyStock
     * @return
     */
    public int getQteIn(){
        return myStock.getQteIn();
    }

    public void getQteFromDataBase(int id){

        //********ACCES LOCAL*********************
        accesLocal = new AccesLocal(contexte);
        Log.d("getStock() ", "**************************************************"+toString());
        qteStock = accesLocal.recupQteStock(id);
        //****************************************

    }

    public int getTheQteFromDataBase(){
        return qteStock;
    }

    //--------------------------------------------------------------------------------------------
    //---------------------------STOCK CLIENT----------------------------------------------------------
    //--------------------------------------------------------------------------------------------

    /**
     * Insérer une nouvelle vente
     * @param vente
     * @param stock
     */
    public void ajouterVente(Vente vente, ArrayList<StockClient> stock)
    {
        //********ACCES LOCAL*******************
        accesLocal = new AccesLocal(contexte);
        accesLocal.ajouterVente(vente, stock);
        //****************************************
    }

    /**
     * Récupérer tout le stock client depuis la BDD
     */
    public void getStockClient(){

        //********ACCES DISTANT*******************
        //Log.d("verifierProfil() ", "**************************************************"+toString());
        //accesDistant = new AccesDistant();
        //accesDistant.envoi("getProduits", new JSONArray());
        //****************************************

        //********ACCES LOCAL*********************
        accesLocal = new AccesLocal(contexte);
        Log.d("getStock() ", "**************************************************"+toString());
        leStockClient = accesLocal.recupAllStocksClients();
        //****************************************
    }

    /**
     * Retourner le stock client depuis la BDD Locale
     * @return
     */
    public ArrayList<StockClient> getLeStockClient(){
        Log.d("LE STOCK CLIENT : ","++++++++++++++++++++++++++++++++++++++++++++++"+leStockClient);
        return leStockClient;
    }

    //--------------------------------------------------------------------------------------------------------------
    //---------------------------CLIENT----------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------

    /**
     * Créer une nouvelle instance de l'objet Client et récupérer l'id de l'objet client
     * @param client
     */

    public void setIdClient(Client client){
        Controle.client = client;
        idClient = client.getId();
        Log.d("idCONTROLE","*************setIdClient****************"+client.getId());
    }

    public void setIdClientInteger(int id){
        this.idClient = id;
    }

    /**
     * Retourner l'id de l'objet Client
     * @return
     */
    public int getIdClient(){
        Log.d("id","*************setIdClient****************"+idClient);
        return idClient;
    }

    public void setIntIdClient(int idClient){
        this.idClient = idClient;
    }

    /**
     * Insérer un nouveau client dans la BDD Locale
     * @param formeSociale
     * @param ets
     * @param categorie
     * @param nom
     * @param prenom
     * @param adresse
     * @param mail
     * @param tel
     * @param profession
     * @param idVendeur
     */
    public void creerClient(String formeSociale, String ets, String categorie, String nom, String prenom, String adresse, String mail, String tel, String profession, int idVendeur, Localisation localisation){
        client = new Client(0, formeSociale, ets, categorie, nom,prenom,adresse,mail,tel,profession, null, idVendeur);

        //********ACCES LOCAL*******************
        accesLocal = new AccesLocal(contexte);
        accesLocal.ajoutClient(client, localisation);
        //****************************************
    }

    /**
     * Récupérer tous les clients depuis la BDD Locale
     */
    public void getClients(){

        //********ACCES DISTANT*******************
        //Log.d("verifierProfil() ", "**************************************************"+toString());
        //accesDistant = new AccesDistant();
        //accesDistant.envoi("getClients", new JSONArray());
        //****************************************

        //********ACCES LOCAL*********************
        Log.d("getClients() ", "**************************************************"+toString());
        accesLocal = new AccesLocal(contexte);
        lesClients = accesLocal.recupClients();
        //****************************************

    }

    /**
     * retourner tous les clients
     * @return
     */
    public ArrayList<Client> getLesClients(){
        return lesClients;
    }

    /**
     * Injecter lesClients dans ListClientsActivity pour les afficher
     * @param lesClients
     */
    public void setLesClients(ArrayList<Client> lesClients) {
        this.lesClients = lesClients;
        Log.d("setLesClients <> ", "**************************************************"+lesClients.toString());
        ((ListClientsActivity)contexte).creerList();
    }

    public void getInfosClient(int idClient){

        //********ACCES LOCAL*********************
        Log.d("getClients() ", "**************************************************"+toString());
        accesLocal = new AccesLocal(contexte);
        client = accesLocal.recupInfosClient(idClient);
        //****************************************

    }

    public Client getLesInfosClient(){
        return client;
    }

    public void modifierClient(Client client, Localisation localisation){
        //********ACCES LOCAL*********************
        Log.d("getClients() ", "**************************************************"+toString());
        accesLocal = new AccesLocal(contexte);
        accesLocal.modifierInfosClient(client, localisation);
        //****************************************
    }

    //--------------------------------------------------------------------------------------------------------------
    //--------------------------------------------VENTE--------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------

    /**
     * Récupérer l'idVente depuis ListSalesActivity
     * @param id
     */
    public void setIdVente(int id){
        this.idVente = id;
    }

    public int getIdVente(){
        return idVente;
    }

    //---------------------------OPERATIONS DE RECUPERATION DES INFORMATIONS D'UN CLIENT AFIN DE POUVOIR MODIFIER LA VENTE----------------------

    /**
     * Récupérer les Informations d'un client d'une vente ainsi que son stock
     */
    public void recupInfosClient(){

            //********ACCES LOCAL*********************
            accesLocal = new AccesLocal(contexte);
            idClient = accesLocal.recupIdClient(idVente);
            etablissementClient = accesLocal.recupEtsClient(idVente);
            accompteClient = accesLocal.recupAccompteClient(idVente);
            //soldeClient = accesLocal.recupSoldeClient(idVente);
            arrayStockClient = accesLocal.recupStockClient(idVente);
            laVenteDuClient = accesLocal.recupVenteDuClient(idVente);
            //****************************************
            Log.d("idClient","****************idClient************"+idClient);
            Log.d("nomClient","****************nomClient************"+ etablissementClient);
            Log.d("leStockClient","****************leStockClient************"+arrayStockClient.toString());
            Log.d("laVenteDuClient","****************laVenteDuClient************"+laVenteDuClient.toString());
            //idVente = 0;

    }

    public Double recupAccompteClient(){
        return accompteClient;
    }

    public Double recupSoldeClient(){
        return soldeClient;
    }

    public int recupIdDuClient() {
        return idClient;
    }

    public String recupEtablissementClient(){
        return etablissementClient;
    }

    public ArrayList<StockClient> recupLeStockClient(){
        return arrayStockClient;
    }

    public Vente recupLaVenteDuClient(){
        return laVenteDuClient;
    }

    public void modifierVente(Vente vente, ArrayList<StockClient> stock, ArrayList<StockClient> temporaryProductsSales){

        //********ACCES LOCAL*********************
        accesLocal = new AccesLocal(contexte);
        accesLocal.modifierVente(vente,stock, temporaryProductsSales);

    }

    public boolean checkSaleMemberShip(int id){
        boolean memeberShip;
        int idVendeur = getIdUser();
        accesLocal = new AccesLocal(contexte);
        memeberShip = accesLocal.checkSaleMemberShip(id, idVendeur);

        return memeberShip;
    }

    //----------------------------------------------------------------------------------------------------------------------------------

    /**
     * Récuperer uniquement les clients qui ont achetés (ou vente.idVente = client.id)
     */
    public void getVentesClients(){

        //********ACCES LOCAL*********************
        accesLocal = new AccesLocal(contexte);
        lesVentesClient = accesLocal.recupVentesClients();
        //****************************************
    }

    public void recupVentesClient(){

        //********ACCES LOCAL*********************
        accesLocal = new AccesLocal(contexte);
        lesVentesClient = accesLocal.recupVentesClient(idClient);
        //****************************************

    }

    /**
     * retourner uniquement les clients qui sont récupérés dessus
     * @return
     */
    public ArrayList<ClientVente> getLesVentesClients(){
        return lesVentesClient;
    }

    //--------------------------------------------------------------------------------------------------------------
    //---------------------------PROFIL VERIFICATION USER PASSWORD--------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------

    private void creerProfil(String username, String password)
    {
        profil = new User(0, username,password, 0, false);
    }

    /**
     * Envoyer le profil recupéré
     * @param username
     * @param password
     */
    public void verifierProfil(String username, String password){
        creerProfil(username,password);
        accesDistant  = new AccesDistant(contexte);
        accesDistant.verifierProfil(profil);
    }

    public void setUser(User profil){
        Controle.profil = profil;
    }


    public void accessGranted(){
        ((MainActivity)contexte).openAccueilActivity();
    }

    /**
     * Récupération du user
     * @return
     */
    public int getIdUser(){
        if (profil == null) {
            return 0;
        }else{
            return profil.getId();
        }
    }

    /**
     * Récupération du user dans MainActivity
     * @return
     */
    public String getUser() {
        if (profil == null) {
            return null;
        }else{
            return profil.getUsername();
        }
    }

    /**
     * Récupération du mot de passe dans MainActivity
     * @return
     */
    public String getPassword() {
        if (profil == null) {
            return null;
        }else{
            return profil.getPassword();
        }
    }

    public void noProfil() {
        Controle.profil = profil;
        ((MainActivity)contexte).profilNull();
    }

    public boolean checkSupervisor(){
        return profil.isSuperviseur();
    }

    public User recupProfil(){
        return profil;
    }

    //---------------------------------------------------------------------------------------------
    //---------------------------LOCATION----------------------------------------------------------
    //---------------------------------------------------------------------------------------------

    public GeoPoint getlocalisationClient(int idClient){
        GeoPoint point;
        accesLocal = new AccesLocal(contexte);
        point = accesLocal.recupLocalisationClient(idClient);

        return point;
    }

    public void setLocation(Localisation location){
        Log.d("setLocation() ", "**************************************************"+toString());
        Controle.location = location;
        //((MapsActivity)contexte).displayMarkers();
    }

    //-------------------------------------------------------------------------------------------------------------
    //---------------------------OPERATIONS AVEC FIRESTORE--------------------------------------------
    //-------------------------------------------------------------------------------------------------------------

    public ArrayList<Client> getNouveauxClients(){
    accesLocal = new AccesLocal(contexte);
    nouveauxClients = new ArrayList<>();
    nouveauxClients = accesLocal.recupNouveauxClients();

    return nouveauxClients;
    }

    public ArrayList<Client> retourneNouveauxClients(){
        return nouveauxClients;
    }

    public void getLocalisations(){
        accesLocal = new AccesLocal(contexte);
        localisations = new ArrayList<>();
        localisations = accesLocal.recupLocalisations();
    }

    public ArrayList<Localisation> retourneLocalisations(){
        return localisations;
    }

    public void envoiClients(ArrayList<Client> clients){
        synchronizeData = new SynchronizeData();
        synchronizeData.envoiNouveauxClients(clients);
    }

    public void envoiLocalisationsClients(ArrayList<Localisation> localisations){
        synchronizeData = new SynchronizeData();
        synchronizeData.envoiLocalisationsClients(localisations);
    }

    public void recupVentes(){
        accesLocal = new AccesLocal(contexte);
        ventes = accesLocal.recupVentes();
    }

    public ArrayList<Vente> retournerVentes(){
        return ventes;
    }

    public void envoiVentes(){
        synchronizeData = new SynchronizeData();
        synchronizeData.envoiVentes();
    }

    public void envoiStockClient(){
        synchronizeData = new SynchronizeData();
        synchronizeData.envoiStockClient();
    }

    public void envoiMyStock(){
        synchronizeData = new SynchronizeData();
        synchronizeData.envoiMyStock();
    }

    public void recupNewProduits(){
        accesDistant = new AccesDistant(contexte);
        accesDistant.downloadProduits(new AsyncResponseProducts() {
            @Override
            public void onCallback(ArrayList<Produit> produits) {
                lesProduits = produits;
                Log.d("******","*********PRODUITS**********"+produits.toString());
                addNewProduits();
                if (!lesProduits.isEmpty()){
                    lesProduitsIsFull = true;
                }
                Log.d("onCallBack :", "************  recupNewProduits  *********************");
            }
        });
    }

    private void addNewProduits(){
        accesLocal = new AccesLocal(contexte);
        accesLocal.refreshProducts(lesProduits);
    }

    public void recupMyStock(){

            accesDistant = new AccesDistant(contexte);
            accesDistant.downloadStock(new AsyncResponseMyStock() {
                @Override
                public void onCallBack(ArrayList<MyStock> myStock) {
                    arrayMyStock = myStock;
                    addMyNewStock();
                    if (!arrayMyStock.isEmpty()){
                        arrayMyStockIsFull = true;
                    }
                    Log.d("onCallBack :", "************  recupMyStock  *********************");
                }
            });
    }

    private void addMyNewStock(){
        accesLocal = new AccesLocal(contexte);
        accesLocal.addMyNewStock(arrayMyStock);
    }

    public void recupClients(){
        if (!profil.isSuperviseur()){

            accesDistant = new AccesDistant(contexte);
            accesDistant.downloadClients(new AsyncResponseClients() {
                @Override
                public void onCallback(ArrayList<Client> clients) {
                    lesClients = clients;
                    addNewClients();
                    recupClientLocation();
                    addSituationClients();
                    if (!lesClients.isEmpty()){
                        lesClientsIsFull = true;
                    }
                    Log.d("onCallBack :", "************  recupClients  *********************");
                }
            });
        }
    }

    private void recupClientsOfSupervisor(ArrayList<Integer> usersId){
        accesDistant = new AccesDistant(contexte);
        accesDistant.downloadClientsOfSupervisor(usersId, new AsyncResponseClients() {
            @Override
            public void onCallback(ArrayList<Client> clients) {
                lesClients = clients;
                addNewClients();
                recupClientLocation();
                addSituationClients();
                if (!lesClients.isEmpty()){
                    lesClientsIsFull = true;
                }
                Log.d("onCallBack :", "************  recupClientsOfSupervisor  *********************");
            }
        });

    }

    public void checkResultsForListClientsActivity(){
        Log.d("2 :", "************  checkResultsForListClientsActivity  *********************");
        Thread t = new Thread(new ThreadCheckVariablesFirestore());
        t.start();

            if (this.check){
                Log.d("4 :", "************  checkResultsForListClientsActivity / if this.check  *********************");
                ((AccueilActivity)contexte).openListClientActivity();
            }else{
                ((AccueilActivity)contexte).checkNotOk();
                Log.d("4 :", "************  checkResultsForListClientsActivity / else this.check  *********************");
            }


    }

    public boolean checkIsOk(boolean check){
        this.check = check;
        return this.check;
    }

    public void setArrayMyStockIsFull(boolean arrayMyStockIsFull){
        this.arrayMyStockIsFull = arrayMyStockIsFull;
    }

    public boolean arrayMyStockIsFull(){
        return arrayMyStockIsFull;
    }

    public void setArraySituationIsFull(boolean arraySituationIsFull){
        this.arraySituationIsFull = arraySituationIsFull;
    }

    public boolean arraySituationIsFull(){
        return arraySituationIsFull;
    }

    public void setArrayStockClientIsFull (boolean arrayStockClientIsFull){
        this.arrayStockClientIsFull = arrayStockClientIsFull;
    }

    public boolean arrayStockClientIsFull(){
        return arrayStockClientIsFull;
    }

    public void setLesClientsIsFull (boolean lesClientsIsFull){
        this.lesClientsIsFull = lesClientsIsFull;
    }

    public boolean lesClientsIsFull(){
        return lesClientsIsFull;
    }

    public void setLesPointsIsFull (boolean lesPointsIsFull){
        this.lesPointsIsFull = lesPointsIsFull;
    }

    public boolean lesPointsIsFull(){
        return lesPointsIsFull;
    }

    public void setLesProduitsIsFull (boolean lesProduitsIsFull){
        this.lesProduitsIsFull = lesProduitsIsFull;
    }

    public boolean lesProduitsIsFull(){
        return lesProduitsIsFull;
    }

    public void setLocationsIsFull (boolean locationsIsFull){
        this.locationsIsFull = locationsIsFull;
    }

    public boolean locationsIsFull(){
        return locationsIsFull;
    }

    public void setUsersIdIsFull (boolean usersIdIsFull){
        this.usersIdIsFull = usersIdIsFull;
    }

    public boolean usersIdIsFull(){
        return usersIdIsFull;
    }

    public void setVentesIsFull (boolean ventesIsFull){
        this.ventesIsFull = ventesIsFull;
    }

    public boolean ventesIsFull(){
        return ventesIsFull;
    }


    private void recupClientLocation(){
        accesDistant = new AccesDistant(contexte);
        accesDistant.downloadLocationClients(lesClients, new AsyncResponseLocations() {
            @Override
            public void onCallback(ArrayList<Localisation> localisations) {
                locations = localisations;
                accesLocal = new AccesLocal(contexte);
                accesLocal.addNewLocations(locations);
                if (!locations.isEmpty()){
                    locationsIsFull = true;
                }
                Log.d("onCallBack :", "************  recupClientLocation  *********************");
            }
        });
    }

    public void recupSecteur(){
        accesDistant = new AccesDistant(contexte);
        accesDistant.downloadMySector(new AsyncResponseSector() {
            @Override
            public void onCallBack(ArrayList<Localisation> points) {
                lesPoints = points;
                accesLocal = new AccesLocal(contexte);
                accesLocal.addSecteur(lesPoints);
                if (!lesPoints.isEmpty()){
                    lesPointsIsFull = true;
                }
                Log.d("onCallBack :", "************  recupSecteur  *********************");
            }
        });
    }

    public ArrayList<Localisation> retourneSecteur(){
        accesLocal = new AccesLocal(contexte);
        secteur = accesLocal.recupSecteur();
        return secteur;
    }

    public ArrayList<Localisation> retourneLocationClients(){
        accesLocal = new AccesLocal(contexte);
        localisations = accesLocal.recupLocalisations();
        return localisations;
    }

    private void addSituationClients(){
        accesDistant = new AccesDistant(contexte);
        accesDistant.downloadSituationClients(new AsyncResponseSituation() {
            @Override
            public void onCallback(ArrayList<Situation> situations) {
                arraySituation = situations;
                accesLocal = new AccesLocal(contexte);
                accesLocal.addSituationClient(arraySituation);
                if (!arraySituation.isEmpty()){
                    arraySituationIsFull = true;
                }
                Log.d("onCallBack :", "************  addSituationClients  *********************");
            }
        });
    }

    public void addNewClients(){
        accesLocal = new AccesLocal(contexte);
        accesLocal.addNewClients(lesClients);
    }

    //=========================================================================================
    // OPERATIONS DE TEST SUR FIRESTORE POUR L'INTEGRATION DES THREADS
    //=========================================================================================

    public boolean isSupervisor(){
        isSupervisor = profil.isSuperviseur();
        return isSupervisor;
    }

    public void executeThreadFirestore(){
        //accesDistant = new AccesDistant(contexte);
        //accesDistant.run();

    }

    public void addVentesClients (ArrayList<Vente> ventes){

        accesLocal = new AccesLocal(contexte);
        accesLocal.addVentesClients(ventes);

    }

    public int getIdProfil(){
        return profil.getId();
    }

    public void setVentes(ArrayList<Vente> ventes){
        this.ventes = ventes;
    }

    //==============================================================================================

    public void recupInfosOfTeam(){
        accesLocal = new AccesLocal(contexte);

        if (profil.isSuperviseur()){

            accesDistant = new AccesDistant(contexte);
            accesDistant.checkUsersOfSupervisor(profil, new AsyncResponseSupervisor() {
                @Override
                public void onCallback(ArrayList<Integer> userIds) {
                    int idSuperviseur = profil.getId();
                    userIds.add(idSuperviseur);
                    usersId = userIds;
                    recupClientsOfSupervisor(userIds);
                    recupTeamSalesForSupervisor(userIds);
                    if (!usersId.isEmpty()){
                        usersIdIsFull = true;
                    }
                    Log.d("onCallBack :", "************  recupInfosOfTeam  *********************");
                }
            });
        }
    }

    private void recupTeamSalesForSupervisor(ArrayList<Integer> userIds){

        accesDistant = new AccesDistant(contexte);
        accesDistant.downloadSales(userIds, new AsyncResponseVentes() {
            @Override
            public void onCallback(ArrayList<Vente> arrayVentes) {
                ventes = arrayVentes;
                accesLocal = new AccesLocal(contexte);
                accesLocal.addVentesClients(ventes);
                recupTeamStockClient(ventes);
                if (!ventes.isEmpty()){
                    ventesIsFull = true;
                }
                Log.d("onCallBack :", "************  recupTeamSalesForSupervisor  *********************");
            }
        });
    }

    private void recupTeamStockClient(ArrayList<Vente> lesVentes){

        accesDistant = new AccesDistant(contexte);
        accesDistant.downloadStockClient(lesVentes, new AsyncResponseStockClient() {
            @Override
            public void onCallback(ArrayList<StockClient> stockClients) {
                arrayStockClient = stockClients;
                accesLocal = new AccesLocal(contexte);
                accesLocal.addStockClient(arrayStockClient);
                if (!arrayStockClient.isEmpty()){
                    arrayStockClientIsFull = true;
                }
                Log.d("onCallBack :", "************  recupTeamStockClient  *********************");
            }
        });
    }

    public void addClientFireStore(Client client, int id){
        synchronizeInstantData = new SynchronizeInstantData(contexte);
        synchronizeInstantData.ajoutClient(client, id);
    }

    public void addLocalisationFireStore(int id, Localisation location, int idClient){
        synchronizeInstantData = new SynchronizeInstantData(contexte);
        synchronizeInstantData.addLocalisationClient(id, location, idClient);
    }

    public void addUpdateVenteFireStore(int id, Vente vente){
        synchronizeInstantData = new SynchronizeInstantData(contexte);
        synchronizeInstantData.addUpdateVente(id, vente);
    }

    public void addUpdateRowMyStockFireStore(int id, int idProduit, int qteFinal, int idVendeur){
        synchronizeInstantData = new SynchronizeInstantData(contexte);
        synchronizeInstantData.addUpdateMyStock(id, idProduit, qteFinal, idVendeur);
    }

    public void addUpdateRowStockClientFireStore(int id, int idVente, int idProduit, int qteFinal, Double prixUnitaire){
        synchronizeInstantData = new SynchronizeInstantData(contexte);
        synchronizeInstantData.addUpdateStockClient(id, idVente, idProduit, qteFinal, prixUnitaire);
    }

    public void deleteRowStockClientFireStore(int id){
        synchronizeInstantData = new SynchronizeInstantData(contexte);
        synchronizeInstantData.deleteRowStockClientFireStore(id);
    }

    public void updateRowLocationFireStore(int id, int idClient, Localisation localisation){
        synchronizeInstantData = new SynchronizeInstantData(contexte);
        synchronizeInstantData.updateRowLocationFireStore(id, idClient, localisation);
    }

    public ArrayList<Situation> recupSituationsClients(){
        accesLocal = new AccesLocal(contexte);
        arraySituation = accesLocal.recupSituationsClients();
        return arraySituation;
    }

    public void addUpdateRowSituationFirestore(Situation situation){
        synchronizeInstantData = new SynchronizeInstantData(contexte);
        synchronizeInstantData.addUpdateSituation(situation);
    }

    public void clearDataBase(){
        accesLocal = new AccesLocal(contexte);
        accesLocal.clearTableProduits();
        accesLocal.clearTableStockClient();
        accesLocal.clearTableVentes();
        accesLocal.clearTableSituation();
        accesLocal.clearTableSecteur();
        accesLocal.clearTableClients();
        accesLocal.clearTableLocations();
    }

    //-------------------------------------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------------------------------------
    //---------------------------VERIFIER LA CONNEXION RESEAU------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------

    private void networkConnectivity()
    {
        networkConnectivity = new NetworkConnectivity(contexte);
    }

    public boolean haveNetwork() {
        networkConnectivity();
        return networkConnectivity.isConnected();
    }

    //-------------------------------------------------------------------------------------------------------------
    //---------------------------MAIN----------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------


    /**
     * Création de l'instance
     * @return Instance
     */
    public static final Controle getInstance(Context contexte){
        if(contexte != null){
            Controle.contexte = contexte;
        }
        if(Controle.instance == null){
            Controle.instance = new Controle();
            //accesDistant.envoi("verifier", profil.convertToJSONArray());
            Log.d("getInstance", "public static final Controle getInstance(Context contexte)");
        }
        return Controle.instance;
    }
}