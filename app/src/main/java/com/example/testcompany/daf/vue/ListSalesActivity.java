package com.example.testcompany.daf.vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testcompany.daf.R;
import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.modele.ClientVente;
import com.example.testcompany.daf.outils.MyTools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class ListSalesActivity extends AppCompatActivity {

    private Controle controle;
    private TextView txtEtablissement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sales);
        txtEtablissement = findViewById(R.id.txtEtablissement);
        FloatingActionButton back = findViewById(R.id.backBtn);
        ecouteBtnBack(back);
        controleStart();
    }

    private void controleStart(){
        Log.d("controleStart() ", "**************************************************"+toString());
        this.controle = Controle.getInstance(this);
        this.controle.recupVentesClient();
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
        //ArrayList<ClientVente> newTable = controle.getLesVentesClients();
        ArrayList<ClientVente> lesVentesClients = controle.getLesVentesClients();

        if(lesVentesClients.isEmpty()){

            showAlert();

        }else{

            ArrayList<ClientVente> newTable = newTable(lesVentesClients);
            Log.d("newTbale = =","======================"+newTable.toString());

            //if(lesVentesClients.size() > 0){
            if(newTable.size() > 0){
                txtEtablissement.setText(newTable.get(0).getEtablissement());
                ListView lstVentes = findViewById(R.id.lstVentes);
                final SalesListAdapter adapter = new SalesListAdapter(this, newTable);
                lstVentes.setAdapter(adapter);
                lstVentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                        int id = Integer.parseInt(Long.toString(adapter.getItemId(i)));
                        Log.d("id","***************id******************"+id);
                        controle.setIdVente(id);
                        Boolean membership = controle.checkSaleMemberShip(id);
                        boolean supervisor = controle.checkSupervisor();
                        if (supervisor){

                            if (membership){
                                openNewSaleActivity();
                            }else{
                                openDisplaySaleSupervisorActivity();
                            }

                        }else{
                            openNewSaleActivity();
                        }
                    }
                });
            }

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

    private ArrayList<ClientVente> newTable(ArrayList<ClientVente> lesVentesClients) {
        int i = 0;
        int j = 0;
        while (i<=j) {

                while (j<lesVentesClients.size()) {
                    Date date1 = MyTools.convertStringToDateLocal(lesVentesClients.get(i).getDate());
                    Date date2 = MyTools.convertStringToDateLocal(lesVentesClients.get(j).getDate());

                            if (date2.getTime() > date1.getTime()){

                                int id = lesVentesClients.get(i).getId();
                                int idVente = lesVentesClients.get(i).getIdVente();
                                int idVendeur = lesVentesClients.get(i).getIdVendeur();
                                int idClient = lesVentesClients.get(i).getIdClient();
                                String ets = lesVentesClients.get(i).getEtablissement();
                                Double total = lesVentesClients.get(i).getTotal();
                                Double accompte = lesVentesClients.get(i).getAccompte();
                                Double solde = lesVentesClients.get(i).getSolde();
                                String date = lesVentesClients.get(i).getDate();

                                ClientVente laVenteClient = new ClientVente(lesVentesClients.get(j).getId(), lesVentesClients.get(j).getIdVente(), lesVentesClients.get(j).getIdVendeur(), lesVentesClients.get(j).getIdClient(), lesVentesClients.get(j).getEtablissement(), lesVentesClients.get(j).getTotal(), lesVentesClients.get(j).getAccompte(), lesVentesClients.get(j).getSolde(), lesVentesClients.get(j).getDate());

                                lesVentesClients.get(j).setId(id);
                                lesVentesClients.get(j).setIdVente(idVente);
                                lesVentesClients.get(j).setIdVendeur(idVendeur);
                                lesVentesClients.get(j).setIdClient(idClient);
                                lesVentesClients.get(j).setEtablissementClient(ets);
                                lesVentesClients.get(j).setTotal(total);
                                lesVentesClients.get(j).setAccompte(accompte);
                                lesVentesClients.get(j).setSolde(solde);
                                lesVentesClients.get(j).setDate(date);

                                lesVentesClients.get(i).setId(laVenteClient.getId());
                                lesVentesClients.get(i).setIdVente(laVenteClient.getIdVente());
                                lesVentesClients.get(i).setIdVendeur(laVenteClient.getIdVendeur());
                                lesVentesClients.get(i).setIdClient(laVenteClient.getIdClient());
                                lesVentesClients.get(i).setEtablissementClient(laVenteClient.getEtablissement());
                                lesVentesClients.get(i).setTotal(laVenteClient.getTotal());
                                lesVentesClients.get(i).setAccompte(laVenteClient.getAccompte());
                                lesVentesClients.get(i).setSolde(laVenteClient.getSolde());
                                lesVentesClients.get(i).setDate(laVenteClient.getDate());

                            }
                    j++;
                    }
            i++;
        }
        return lesVentesClients;
    }

    private void openNewSaleActivity(){
        Intent intent = new Intent(this, NewSaleActivity.class);
        startActivity(intent);
    }

    private void openDisplaySaleSupervisorActivity(){
        Intent intent = new Intent(this, DisplaySaleSupervisorActivity.class);
        startActivity(intent);
    }

    private void showAlert() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        //View view = getLayoutInflater().inflate(R.layout.alert_dialog_sale, null);
        //alertDialog.setView(view);
        alertDialog.setMessage("Il n'y a aucune vente à afficher pour ce client")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        return;
    }

}
