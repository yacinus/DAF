package com.example.testcompany.daf.vue;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import com.example.testcompany.daf.R;
import com.example.testcompany.daf.controleur.Controle;

public class MainActivity extends AppCompatActivity {

    //private ProgressBar progressBar;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //propriétés
    private EditText etUsername;
    private EditText etPassword;
    private Controle controle; //besoin de créer un objet depuis controle avec getInstance()
    private String username = null;
    private String password = null;

    /**
     * Initialisation des liens avec les objets graphiques
     */
    private void init(){
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        ecouteLogin();
        ProgressDialog();
    }

    /**
     * Ecoute utilisateur sur bouton Valider
     */
    private void ecouteLogin(){
        findViewById(R.id.btnLogin).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if(username.isEmpty() && password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Veuillez saisir un identifiant", Toast.LENGTH_SHORT).show();
                }else{
                    if(username.length() > 0 && password.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Veuillez saisir le mot de passe", Toast.LENGTH_SHORT).show();
                    }else{
                        dialog.show();
                        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Log.d("ecouteLogin() ", "**************************************************"+toString());
                        controleStart();
                    }
                }
            }
        });
    }

    private void controleStart(){
        Log.d("controleStart() ", "**************************************************"+toString());
        this.controle = Controle.getInstance(this);
        Log.d("haveNetwork() :", "**************************************************"+this.controle.haveNetwork());
        if(this.controle.haveNetwork()) {
            this.controle.verifierProfil(username, password);
        }
        else{
            Toast.makeText(MainActivity.this, "Veuillez verifier votre connexion Internet !", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    public void profilNull(){
        dialog.dismiss();
        Toast.makeText(MainActivity.this, "Nom d'utilisateur ou mot de passe incorrect ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
    }

    public void openAccueilActivity(){
        controle.clearDataBase();
        controle.recupNewProduits();
        controle.recupClients();
        controle.recupInfosOfTeam();
        controle.recupMyStock();
        controle.recupSecteur();
        Intent intent = new Intent(this, AccueilActivity.class);
        startActivity(intent);
        dialog.dismiss();
    }

    private void ProgressDialog() {
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.circular_progress_bar, nullParent);
        alertDialog.setView(view);
        dialog = alertDialog.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        return;
    }
}
