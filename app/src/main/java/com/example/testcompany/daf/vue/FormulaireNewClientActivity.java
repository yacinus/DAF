package com.example.testcompany.daf.vue;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcompany.daf.R;
import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.modele.Client;
import com.example.testcompany.daf.modele.Localisation;
import com.example.testcompany.daf.modele.MyLocation;
import com.example.testcompany.daf.modele.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.GeoPoint;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import java.util.List;

public class FormulaireNewClientActivity extends AppCompatActivity implements PermissionsListener {


    //propriétés
    private Client client;
    private User profil;
    private TextView txtNclient;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private RadioButton radioButtonSarl;
    private RadioButton radioButtonEurl;
    private EditText txtEts;
    private EditText txtCategorie;
    private EditText txtNom;
    private EditText txtPrenom;
    private EditText txtAdresse;
    private EditText txtMail;
    private EditText txtTel;
    private EditText txtProfession;
    private Controle controle; //besoin de créer un objet depuis controle avec getInstance()
    private PermissionsManager permissionsManager;
    private int idClient = 0;
    private int idVendeur;
    private String formeSociale = null;
    private String ets = null;
    private String categorie = null;
    private String nom = null;
    private String prenom = null;
    private String adresse = null;
    private String mail = null;
    private String tel = null;
    private String profession = null;
    private String dateAjout = null;
    private Button btnLocation;
    private Button btnStore;
    private Double Longitude;
    private Double Latitude;
    private GeoPoint point;

    private final MyLocation myLocation = new MyLocation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_new_client);
        FloatingActionButton back = findViewById(R.id.backBtn);
        ecouteBtnBack(back);
        init();
    }

    private void ecouteBtnBack(FloatingActionButton back){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccueilActivity();
            }
        });
    }

    /**
     * Initialisation des liens avec les objets graphiques
     */
    private void init(){

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

                this.controle = Controle.getInstance(this);
                idClient = controle.getIdClient();
                txtNclient = findViewById(R.id.txtNClient);
                radioGroup = findViewById(R.id.btnRadioGroup);
                radioButtonSarl = findViewById(R.id.btnSarl);
                radioButtonEurl = findViewById(R.id.btnEurl);
                txtEts = findViewById(R.id.txtEtablissement);
                txtCategorie = findViewById(R.id.txtCategorie);
                txtNom = findViewById(R.id.txtNom);
                txtPrenom = findViewById(R.id.txtPrenom);
                txtAdresse = findViewById(R.id.txtAdresse);
                txtMail = findViewById(R.id.txtMail);
                txtTel = findViewById(R.id.txtTel);
                txtProfession = findViewById(R.id.txtProfession);
                btnLocation = findViewById(R.id.btnLocation);
                btnStore = findViewById(R.id.btnStore);

                Log.d("***********","***********idClient*************"+idClient);

                if (idClient != 0){
                    profil = controle.recupProfil();
                    controle.getInfosClient(idClient);
                    client = controle.getLesInfosClient();
                    dateAjout = client.getDateAjout();
                    idVendeur = client.getIdVendeur();
                    point = controle.getlocalisationClient(idClient);
                    txtNclient.setText("Client N° ".concat(String.valueOf(idClient)));
                    setInfosClient();
                }
                ecouteBtnLocation(this);
                ecouteLogin();

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlert() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        //View view = getLayoutInflater().inflate(R.layout.alert_dialog_sale, null);
        //alertDialog.setView(view);
        alertDialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private void setInfosClient(){
        Log.d("***********","***********client.getFormeSociale()*************"+client.getFormeSociale());
        formeSociale = client.getFormeSociale();
        if (formeSociale.contains("Eurl")){
            Log.d("********","*****************EURL****************");
            radioButtonEurl.setChecked(true);
            radioButtonSarl.setChecked(false);
        }else{
            if (formeSociale.contains("Sarl")) {
                Log.d("********","*****************SARL****************");
                radioButtonEurl.setChecked(false);
                radioButtonSarl.setChecked(true);
            }
        }
        txtEts.setText(client.getEtablissement());
        txtCategorie.setText(client.getCategorie());
        txtNom.setText(client.getNom());
        txtPrenom.setText(client.getPrenom());
        txtAdresse.setText(client.getAdresse());
        txtMail.setText(client.getMail());
        txtTel.setText(client.getTel());
        txtProfession.setText(client.getProfession());
        Latitude = point.getLatitude();
        Longitude = point.getLongitude();
        btnStore.setEnabled(true);
    }

    @SuppressWarnings("MissingPermission")
    private void ecouteBtnLocation(Context context){
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isLocationEnabled()){

                    myLocation.getLocation(context, locationResult);

                }else{

                    showAlert();

                }
            }
        });
    }

    private final MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {

        @Override
        public void gotLocation(Location location) {
            // TODO Auto-generated method stub
            Longitude = location.getLongitude();
            Latitude = location.getLatitude();

                try{
                    Toast.makeText(FormulaireNewClientActivity.this, "Coordonnées géographiques sauvegardés !", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Longitude : "+Longitude+" \n Latitude : "+Latitude, Toast.LENGTH_LONG).show();
                    btnStore.setEnabled(true);
                }catch (Exception e){
                    e.getStackTrace();
                    Toast.makeText(FormulaireNewClientActivity.this, "Echec de sauvegarde, Veuillez réessayer !", Toast.LENGTH_SHORT).show();
                }
            //Toast.makeText(getApplicationContext(), "Got Location", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * Ecoute utilisateur sur bouton Enregistrer
     */
    private void ecouteLogin(){
        btnStore.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                    int radioBtn = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioBtn);
                    try{
                        formeSociale = radioButton.getText().toString();
                    }catch(Exception e){
                        //Toast.makeText(FormulaireNewClientActivity.this, "Veuillez renseigner la raison sociale !", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    Log.d("formeSociale : ","+++++++++++++++++++++++++++++++"+formeSociale);
                    ets = txtEts.getText().toString();
                    categorie = txtCategorie.getText().toString();
                    nom = txtNom.getText().toString();
                    prenom = txtPrenom.getText().toString();
                    adresse = txtAdresse.getText().toString();
                    mail = txtMail.getText().toString().trim();
                    tel = txtTel.getText().toString().trim();
                    profession = txtProfession.getText().toString();
                    if(!ets.isEmpty() && !nom.isEmpty() && !prenom.isEmpty() && !adresse.isEmpty()){
                        if (idClient == 0) {
                            Toast.makeText(FormulaireNewClientActivity.this, "Client enregistré dans la base locale !", Toast.LENGTH_SHORT).show();
                            Localisation localisation = new Localisation(0, Latitude, Longitude, 0);
                            controle.creerClient(formeSociale, ets, categorie, nom, prenom, adresse, mail, tel, profession, controle.getIdUser(), localisation);
                            openAccueilActivity();
                        }else{
                            Client client = new Client(idClient,formeSociale,ets, categorie, nom, prenom, adresse, mail, tel, profession, dateAjout, idVendeur);
                            Localisation localisation = new Localisation(0, Latitude, Longitude, idClient);
                            controle.modifierClient(client, localisation);
                            Toast.makeText(FormulaireNewClientActivity.this, "Client modifié !", Toast.LENGTH_SHORT).show();
                            controle.setIntIdClient(0);
                            openAccueilActivity();
                        }
                    }else{

                        Toast.makeText(FormulaireNewClientActivity.this, "Raison Sociale, Ets, Nom, Prenom, Adresse requis !", Toast.LENGTH_SHORT).show();

                    }
            }
        });
    }

    private void openAccueilActivity(){
        Intent intent = new Intent(this, AccueilActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.finish();
        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        controle.setIntIdClient(0);
        super.onBackPressed();
        this.finish();
        return;
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (!granted) {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
