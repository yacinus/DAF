package com.example.testcompany.daf.vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.testcompany.daf.R;
import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.modele.Client;
import com.example.testcompany.daf.modele.StockClient;
import com.example.testcompany.daf.modele.Vente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DisplaySaleSupervisorActivity extends AppCompatActivity {

    private TextView txtIdVente;
    private TextView txtIdVendeur;
    private TextView txtTotal;
    private TextView txtAccompte;
    private TextView txtSolde;
    private TextView txtPrix;
    private TextView txtId;
    private TextView txtEtablissement;
    private TextView txtAdress;
    private TextView txtDate;
    private TextView txtCode;
    private TextView txtDesignation;
    private TextView txtQuantity;
    private Controle controle;
    private int idClient;
    private Client client;
    private Vente vente;
    private ArrayList<StockClient> stockClients = new ArrayList<>();
    private TableLayout tableLayout;
    private TableRow row;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sale_supervisor);
        txtIdVente = findViewById(R.id.txtidVente);
        txtIdVendeur = findViewById(R.id.txtIdVendeur);
        txtId = findViewById(R.id.txtIdClient);
        txtEtablissement = findViewById(R.id.txtEtablissement);
        txtAdress = findViewById(R.id.txtAdress);
        txtTotal = findViewById(R.id.txtTotal);
        txtAccompte = findViewById(R.id.txtAccompte);
        txtSolde = findViewById(R.id.txtSolde);
        txtDate = findViewById(R.id.txtDate);
        tableLayout = findViewById(R.id.tableLayoutSuper);
        FloatingActionButton back = findViewById(R.id.backBtn);
        ecouteBtnBack(back);
        init();
    }

    private void init(){

        this.controle = Controle.getInstance(this);
        controle.recupInfosClient();
        vente = controle.recupLaVenteDuClient();
        stockClients = controle.recupLeStockClient();
        idClient = vente.getIdClient();
        controle.getInfosClient(idClient);
        client = controle.getLesInfosClient();
        remplirTextViews();
        genereTableau(stockClients);
    }

    private void ecouteBtnBack(FloatingActionButton back){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccueilActivity();
            }
        });
    }

    private void remplirTextViews(){
    txtIdVente.setText("Vente N° ".concat(String.valueOf(vente.getId())));
    txtIdVendeur.setText(String.valueOf(vente.getIdVendeur()));
    txtId.setText(String.valueOf(idClient));
    txtEtablissement.setText(client.getEtablissement());
    txtAdress.setText(client.getAdresse());
    txtTotal.setText(vente.getTotal().toString().concat(" DA"));
    txtAccompte.setText(vente.getAccompte().toString().concat(" DA"));
    txtSolde.setText(vente.getSolde().toString().concat(" DA"));
    txtDate.setText(vente.getDate());

    }

    private void genereTableau (ArrayList<StockClient> stockClients){
        int id;
        for (int i=0;i<stockClients.size();i++){
            id = stockClients.get(i).getIdProduitStockClient();
            controle.getProduit(id);

            row = new TableRow(this);

            TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(rowLayoutParams);

            txtDesignation = new TextView(this);
            txtDesignation.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            txtDesignation.setPadding(10,0,0,0);
            txtDesignation.setText(controle.getLeProduit().getDesignation());

            txtCode = new TextView(this);
            txtCode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            txtCode.setPadding(10,0,0,0);
            txtCode.setText(controle.getLeProduit().getCode());

            txtQuantity = new TextView(this);
            txtQuantity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            txtQuantity.setPadding(10,0,0,0);
            txtQuantity.setText(String.valueOf(stockClients.get(i).getQteOut()));

            txtPrix = new TextView(this);
            txtPrix.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            txtPrix.setPadding(10,0,0,0);
            txtPrix.setText(String.valueOf(stockClients.get(i).getPrixUnitaire()));

            row.addView(txtDesignation);
            row.addView(txtCode);
            row.addView(txtQuantity);
            row.addView(txtPrix);
            tableLayout.addView(row);
        }
    }

    private void openAccueilActivity(){
        Intent intent = new Intent(this, AccueilActivity.class);
        startActivity(intent);
    }

}
