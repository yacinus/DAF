package com.example.testcompany.daf.vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.testcompany.daf.R;
import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.modele.Client;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListClientsActivity extends AppCompatActivity {

    private Controle controle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_clients);
        FloatingActionButton back = findViewById(R.id.backBtn);
        ecouteBtnBack(back);
        controleStart();
    }

    private void controleStart(){
        Log.d("controleStart() ", "**************************************************"+toString());
        this.controle = Controle.getInstance(this);
        this.controle.getClients();
        creerList();
    }

    private void ecouteBtnBack(FloatingActionButton back){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * CREER LA LISTE ADAPTER
     */
    public void creerList(){
        ArrayList<Client> lesClients = controle.getLesClients();
        Log.d("creerList() ", "**************************************************"+lesClients.toString());
        //Collections.sort(lesClients, Collections.<Clients>reverseOrder());  ---> avec compareTo dans Client.java pour trier du plus récent au plus ancien
        if(lesClients != null){
            ListView lstClients = findViewById(R.id.lstClients);
            Log.d("ClientListAdapter ", "**************************************************"+lstClients);
            final ClientListAdapter adapter = new ClientListAdapter(this, lesClients);
            Log.d("ClientListAdapterAfter ", "**************************************************"+adapter.toString());
            lstClients.setAdapter(adapter);
            lstClients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                    int id = Integer.parseInt(Long.toString(adapter.getItemId(i)));
                    Log.d("id","***************id******************"+id);
                    controle.setIntIdClient(id);
                    openClientInfosActivity();
                }
            });
        }
    }

    private void openClientInfosActivity(){
        Intent intent = new Intent(this, FormulaireNewClientActivity.class);
        startActivity(intent);
        //finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        controle.setArrayMyStockIsFull(false);
        controle.setArraySituationIsFull(false);
        controle.setArrayStockClientIsFull(false);
        controle.setLesClientsIsFull(false);
        controle.setUsersIdIsFull(false);
        controle.setArrayMyStockIsFull(false);
        controle.setLesPointsIsFull(false);
        controle.setLesProduitsIsFull(false);
        controle.setLocationsIsFull(false);
        controle.setVentesIsFull(false);
        this.finish();
        Intent intent = new Intent(this, AccueilActivity.class);
        startActivity(intent);
        return;
    }

}
