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
import com.example.testcompany.daf.modele.MyStock;
import com.example.testcompany.daf.modele.Produit;
import com.example.testcompany.daf.modele.Stock;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class EtatStockActivity extends AppCompatActivity {

    private Controle controle;
    private TableLayout tableLayout;
    private TableLayout tableLayoutStockEpuise;
    private TableRow row;
    private ArrayList<Stock> stockEpuise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etat_stock);
        FloatingActionButton back = findViewById(R.id.back);
        ecouteBtnBack(back);
        init();
    }

    private void init() {
        this.controle = Controle.getInstance(this);
        this.controle.getMyStock();
        tableLayout = findViewById(R.id.tableLayout);
        //tableLayoutStockEpuise = (TableLayout) findViewById(R.id.tableLayoutStockEpuise);
        creerTableau();
        //creerTableauStockEpuise();
    }

    private void ecouteBtnBack(FloatingActionButton back){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccueilActivity();
            }
        });
    }

    private void creerTableau() {
        ArrayList<MyStock> myStock = controle.getMyFinalStock();
        ArrayList<Stock> newStock = triTableau(myStock);

        for (int i = 0; i < newStock.size(); i++) {

            String designation = newStock.get(i).getDesignation();
            String code = newStock.get(i).getCode();
            int qteIn = newStock.get(i).getQteRestante();

            row = new TableRow(this);
            TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
            rowLayoutParams.setMargins(1,2,1,2);
            row.setBackgroundColor(getResources().getColor(R.color.textNotificationStock));
            row.setLayoutParams(rowLayoutParams);

            TextView txtDesignation = new TextView(this);
            txtDesignation.setLayoutParams(rowLayoutParams);
            txtDesignation.setPadding(10,0,0,0);
            txtDesignation.setTextSize(18);
            txtDesignation.setText(designation);
            row.addView(txtDesignation);

            TextView txtCode = new TextView(this);
            txtCode.setLayoutParams(rowLayoutParams);
            txtCode.setPadding(20,0,0,0);
            txtCode.setTextSize(18);
            txtCode.setText(code);
            row.addView(txtCode);

            TextView txtQteRestant = new TextView(this);
            txtQteRestant.setLayoutParams(rowLayoutParams);
            txtQteRestant.setPadding(15,0,0,0);
            txtQteRestant.setTextSize(18);
            txtQteRestant.setText(String.valueOf(qteIn));
            row.addView(txtQteRestant);


            if (qteIn <= 10){
                txtDesignation.setBackgroundColor(getResources().getColor(R.color.notificiationStockRouge));
                txtCode.setBackgroundColor(getResources().getColor(R.color.notificiationStockRouge));
                txtQteRestant.setBackgroundColor(getResources().getColor(R.color.notificiationStockRouge));
                txtDesignation.setTextColor(getResources().getColor(R.color.textNotificationStock));
                txtCode.setTextColor(getResources().getColor(R.color.textNotificationStock));
                txtQteRestant.setTextColor(getResources().getColor(R.color.textNotificationStock));
            }else{
                if (qteIn >10 && qteIn <= 20){
                    txtDesignation.setBackgroundColor(getResources().getColor(R.color.notificationStockOrange));
                    txtCode.setBackgroundColor(getResources().getColor(R.color.notificationStockOrange));
                    txtQteRestant.setBackgroundColor(getResources().getColor(R.color.notificationStockOrange));
                }else{
                    if (qteIn > 20){
                        txtDesignation.setBackgroundColor(getResources().getColor(R.color.notificiationStockVert));
                        txtCode.setBackgroundColor(getResources().getColor(R.color.notificiationStockVert));
                        txtQteRestant.setBackgroundColor(getResources().getColor(R.color.notificiationStockVert));
                    }
                }
            }
            /*
            else{
                row.setBackgroundColor(getResources().getColor(R.color.notificiationStockVert));
                txtDesignation.setTextColor(getResources().getColor(R.color.textNotificationStock));
                txtCode.setTextColor(getResources().getColor(R.color.textNotificationStock));
                txtQteRestant.setTextColor(getResources().getColor(R.color.textNotificationStock));
            } */
            tableLayout.addView(row);
        }
    }

    private ArrayList<Stock> triTableau(ArrayList<MyStock> myStock) {

        ArrayList<Stock> newStock = new ArrayList<>();

        for (int i=0;i<myStock.size();i++){

            int idProduit = myStock.get(i).getIdProduit();
            controle.getProduit(idProduit);
            Produit produit = controle.getLeProduit();
            String designation = produit.getDesignation();
            String code = produit.getCode();
            int qteIn = myStock.get(i).getQteIn();

            Stock stockObject = new Stock(idProduit, designation, code, qteIn);
            newStock.add(stockObject);

        }

        for (int i = 0;i<newStock.size();i++){

            for (int j=i+1;j<newStock.size();j++){

                int idProduit1 = myStock.get(i).getIdProduit();
                controle.getProduit(idProduit1);
                Produit produit1 = controle.getLeProduit();

                int idProduit2 = myStock.get(j).getIdProduit();
                controle.getProduit(idProduit2);
                Produit produit2 = controle.getLeProduit();
                String designation2 = produit1.getDesignation();
                String code2 = produit2.getCode();
                int qteIn2 = myStock.get(j).getQteIn();

                if (produit1.getDesignation().equals(produit2.getDesignation())){

                    if (j>i+1){
                        Stock stockObject = new Stock(idProduit2,designation2,code2,qteIn2);
                        newStock.get(j).setIdProduit(newStock.get(i+1).getIdProduit());
                        newStock.get(j).setDesignation(newStock.get(i+1).getDesignation());
                        newStock.get(j).setCode(newStock.get(i+1).getCode());
                        newStock.get(j).setQteRestante(newStock.get(i+1).getQteRestante());
                        newStock.set(i+1, stockObject);
                    }

                }
            }
        }

        int s=0;
        int r=0;
        while(s<=r){
            while(r<newStock.size()){
                if (newStock.get(s).getDesignation().equals(newStock.get(r).getDesignation())){
                    if (r != s){
                        newStock.get(r).setDesignation(null);
                    }
                }else{
                    s = r;
                }
                r++;
            }
            s++;
        }

        return newStock;
    }

    private void openAccueilActivity(){
        Intent intent = new Intent(this, AccueilActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        openAccueilActivity();
        return;
    }
}