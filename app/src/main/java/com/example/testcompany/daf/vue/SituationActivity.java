package com.example.testcompany.daf.vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.testcompany.daf.R;
import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.modele.Situation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SituationActivity extends AppCompatActivity {

    private Controle controle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situation);
        FloatingActionButton back = findViewById(R.id.backBtn);
        ecouteBtnBack(back);
        controleStart();
    }

    private void controleStart(){
        Log.d("controleStart() ", "**************************************************"+toString());
        this.controle = Controle.getInstance(this);
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
    private void creerList(){
        ArrayList<Situation> situations = controle.recupSituationsClients();

        if(situations != null){
            ListView lstSituations = findViewById(R.id.lstSituations);
            final SituationsListAdapter adapter = new SituationsListAdapter(this, situations);
            lstSituations.setAdapter(adapter);
            lstSituations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                    int id = Integer.parseInt(Long.toString(adapter.getItemId(i)));
                    Log.d("id","***************id******************"+id);
                    controle.setIdClientInteger(id);
                    openListSalesActivity();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openListSalesActivity(){
        Intent intent = new Intent(this, ListSalesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        controle.setIdVente(0);
        this.finish();
        return;
    }
}
