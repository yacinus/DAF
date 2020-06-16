package com.example.testcompany.daf.vue;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.testcompany.daf.R;
import com.example.testcompany.daf.controleur.Controle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AccueilActivity extends AppCompatActivity {

    private Controle controle;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        FloatingActionButton settings = findViewById(R.id.settings);
        controleStart();
        ecouteLogin();
        ecouteBtnSettings(settings);
    }

    private void controleStart(){
        //Remettre idVente = 0 dans controle
        Log.d("controleStart() ", "**************************************************"+toString());
        this.controle = Controle.getInstance(this);
        controle.setIdVente(0);
        controle.setIntIdClient(0);
        ProgressDialog();
    }

    private void ecouteBtnSettings(FloatingActionButton settings){
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsActivity();
            }
        });
    }

    private void ecouteLogin(){

        findViewById(R.id.btnNewSale).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                openNewSaleActivity();

            }
        });

        findViewById(R.id.btnListClients).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                progressDialog.show();
                checkListClientActivity();

            }
        });

        findViewById(R.id.btnNewClient).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                openFormulaireNewClientActivity();
            }
        });

        findViewById(R.id.btnEtatStock).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                openEtatStockActivity();

            }
        });

        findViewById(R.id.btnSituation).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                progressDialog.show();
                openSituationActivity();
            }
        });

        findViewById(R.id.btnMap).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                openMapsActivity();
            }
        });
    }

    private void ProgressDialog() {
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AccueilActivity.this);
        View view = getLayoutInflater().inflate(R.layout.circular_progress_bar, nullParent);
        alertDialog.setView(view);
        progressDialog = alertDialog.create();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void openListClientActivity(){
        Intent intent = new Intent(this, ListClientsActivity.class);
        startActivity(intent);
        //progressDialog.dismiss();
        this.finish();
    }

    private void checkListClientActivity(){
        if(this.controle.haveNetwork()) {
            controle.recupInfosOfTeam();
            Log.d("1 :", "************  checkListClientActivity  *********************");
            controle.checkResultsForListClientsActivity();
        }else{
            openListClientActivity();
        }
    }

    public void checkNotOk(){
        Toast.makeText(AccueilActivity.this, "Nous n'avons pas pus récupérer toutes les informations, vérifiez votre connexion" +
                "internet !", Toast.LENGTH_SHORT).show();
        Log.d("5 :", "************  checkNotOk()  *********************");
        openListClientActivity();
    }

    private void openFormulaireNewClientActivity(){
        Intent intent = new Intent(this, FormulaireNewClientActivity.class);
        startActivity(intent);
        //finish();
    }

    private void openEtatStockActivity(){
        Intent intent = new Intent(this, EtatStockActivity.class);
        startActivity(intent);
        finish();
    }
    
    private void openNewSaleActivity(){
        Intent intent = new Intent(this, NewSaleActivity.class);
        startActivity(intent);
        //finish();
    }

    private void openSituationActivity(){
        Intent intent = new Intent(this, SituationActivity.class);
        try{
            if(this.controle.haveNetwork()) {
                controle.recupInfosOfTeam();
            }
            progressDialog.dismiss();
        }catch(Exception e){
            e.printStackTrace();
        }
        startActivity(intent);
        //finish();
    }

    private void openMapsActivity(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        //finish();
    }

    private void openSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        //finish();
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Êtes vous sûr(e) de vouloir quitter ?\nIl est préferable de ne pas quitter l'application si vous êtes en service pour cause de ne pas pouvoir y accéder dans une zone sans internet !")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })

                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
