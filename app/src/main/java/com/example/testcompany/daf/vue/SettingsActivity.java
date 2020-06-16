package com.example.testcompany.daf.vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.testcompany.daf.BuildConfig;
import com.example.testcompany.daf.R;
import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.modele.Client;
import com.example.testcompany.daf.modele.Localisation;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements Runnable {

    private final Controle controle;
    private AlertDialog progressDialog;

    public SettingsActivity(){
        this.controle = Controle.getInstance(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        FloatingActionButton btnSync = findViewById(R.id.btnSync);
        FloatingActionButton btnRecupData = findViewById(R.id.btnRecupData);
        FloatingActionButton back = findViewById(R.id.backBtn);
        ecouteBtnBack(back);
        ecouteBtns(btnSync, btnRecupData);
        ProgressDialog();
    }

    private void ecouteBtns(FloatingActionButton btnSync, FloatingActionButton btnRecupData){

        btnSync.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                controle.getClients();
                ArrayList<Client> clients = controle.getLesClients();
                controle.getLocalisations();
                ArrayList<Localisation> localisations = controle.retourneLocalisations();
                controle.envoiClients(clients);
                controle.envoiLocalisationsClients(localisations);
                controle.envoiVentes();
                controle.envoiStockClient();
                controle.envoiMyStock();
                Toast.makeText(SettingsActivity.this, "Synchronisation effectuée avec succés !", Toast.LENGTH_SHORT).show();
            }
        });

        btnRecupData.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for (int i=0;i<5;i++){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(SettingsActivity.this, "Récupération effectuée avec succés !", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).start();
                controle.recupNewProduits();
                controle.recupClients();
                controle.recupInfosOfTeam();
                controle.recupMyStock();
                controle.recupSecteur();
            }
        });
    }

    private void ProgressDialog() {
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
        View view = getLayoutInflater().inflate(R.layout.circular_progress_bar, nullParent);
        alertDialog.setView(view);
        progressDialog = alertDialog.create();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void ecouteBtnBack(FloatingActionButton back){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void openAccueilActivity(){
        Intent intent = new Intent(this, AccueilActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        openAccueilActivity();
        this.finish();
        return;
    }

    @Override
    public void run() {

    }
}
