package com.example.testcompany.daf.vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testcompany.daf.R;
import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.modele.MyStock;
import com.example.testcompany.daf.modele.Produit;

import java.util.ArrayList;

public class StockActivity extends AppCompatActivity {

    //propriétés
    private TextView txtQteRestante;
    private TextView txtQteRestanteTable;
    private int id;
    private int i = 0;
    private ImageButton btn1;
    private ImageButton btnSupprimer;
    private int j;
    private Controle controle;
    private AutocompleteProductsAdapter adapter;
    private final ArrayList<MyStock> temporaryProductSales = new ArrayList<>();
    private ArrayList<MyStock> stock = new ArrayList<>();
    private final ArrayList<Integer> idProduit = new ArrayList<>();
    private final ArrayList<Integer> qte = new ArrayList<>();
    private EditText txtQuantity;
    private EditText txtQte;
    private int qte2;
    private Button btnEnreg;
    private int temporaryQteTable;
    private AutoCompleteTextView txtSearchFilterProduits;
    private TableLayout tableLayout;
    private TableRow row;


    public StockActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        init();
    }

    private void init(){
        this.controle = Controle.getInstance(this);
        this.controle.getProduits();
        this.controle.getMyStock();
        stock = controle.getMyFinalStock();
        txtSearchFilterProduits = findViewById(R.id.txtSearchFilterProduits);
        btnEnreg = findViewById(R.id.btnEnreg);
        tableLayout = findViewById(R.id.tableLayout);
        txtQte = findViewById(R.id.txtQte);
        Log.d("******","*****STOCK == ************"+stock.toString());
        genereTableau(stock);
        creerListProduits();
        actionSurQuantityField();
        ecouteAjout();

        //creerListSpinner();
    }

    private void genereTableau (ArrayList<MyStock> stock){

        for (int i=0;i<stock.size();i++){
            id = stock.get(i).getIdProduit();
            controle.getProduit(id);
            row = new TableRow(this);

            TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(rowLayoutParams);

            TextView txtCode = new TextView(this);
            txtCode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            txtCode.setText(controle.getLeProduit().getCode());
            Log.d("CODE : ","**********controle.getLeProduit().getCode()************"+controle.getLeProduit().getCode());

            TextView txtDesignation = new TextView(this);
            txtDesignation.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            txtDesignation.setText(controle.getLeProduit().getDesignation());
            Log.d("DESIGNATION : ","**********controle.getLeProduit().getDesignation()************"+controle.getLeProduit().getDesignation());

            txtQuantity = new EditText(this);
            txtQuantity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            txtQuantity.setText(String.valueOf(0));

            txtQteRestanteTable = new TextView(this);
            txtQteRestanteTable.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            txtQteRestanteTable.setText(String.valueOf(stock.get(i).getQteIn()));

            btnSupprimer = new ImageButton(this);
            //btnSupprimer.setId(id);
            btnSupprimer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            btnSupprimer.setEnabled(false);

            idProduit.add(id);
            qte.add(stock.get(i).getQteIn());
            row.setId(id);
            row.addView(txtCode);
            row.addView(txtDesignation);
            row.addView(txtQuantity);
            row.addView(txtQteRestanteTable);
            row.addView(btnSupprimer);
            tableLayout.addView(row);
            ecouteBtnSupprimer(id);
            actionSurQuantityFieldTable(id);
        }
    }

    /**
     * Bouton de suppression de ligne
     * @param id
     */
    private void ecouteBtnSupprimer(final int id) {
        for (int i = 1;i<tableLayout.getChildCount();i++) {
            row = (TableRow) tableLayout.getChildAt(i);
            Log.d("*******","***ecouteBtnSupprimer************row.getId()*********"+row.getId());
            Log.d("*******","***ecouteBtnSupprimer***id****************"+id);
            if (row.getId() == id) {

                btn1 = (ImageButton) row.getChildAt(4);
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("*******","***ecouteBtnSupprimer***OnClick****************");
                        removeFromTableidProduit(id);
                        removeFromTable(id);
                    }
                });
            }
        }
    }

    /**
     * CREER LA LISTE ADAPTER PRODUITS
     */
    private void creerListProduits(){
        ArrayList<Produit> lesProduits = controle.getLesProduits();
        Log.d("creerList() ", "**************************************************"+lesProduits.toString());
        if(lesProduits.isEmpty()) {
            Log.d("LISTE VIDE ","*********");
        }else{
            //Collections.sort(lesClients, Collections.<Clients>reverseOrder());  ---> avec compareTo dans Client.java pour trier du plus récent au plus ancien
            adapter = new AutocompleteProductsAdapter(this, lesProduits, 0);
            Log.d("ProductListAdapter ", "**************************************************"+adapter.toString());
            txtSearchFilterProduits.setAdapter(adapter);
        }
    }

    private void actionSurQuantityField(){
        txtQte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtQte.getText().length() > 0) {
                    btnEnreg.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void actionSurQuantityFieldTable(final int id){
        txtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        Log.d("*******", "*********QTE TABLE**************"+qte);

/*
                        for (int s=1;s<tableLayout.getChildCount();s++){
                            row = (TableRow) tableLayout.getChildAt(s);
                            Log.d("*******","******row.getId()*********"+row.getId());
                            Log.d("*******","******id*********"+id);
                        if (row.getId() == id) {
                            TextView txtQuantity = (TextView) row.getChildAt(2);
                            TextView txtQuantityR = (TextView) row.getChildAt(3);
                            temporaryQteTable = qte.get(s-1);
                            try {
                                qte2 = Integer.parseInt(txtQuantity.getText().toString());
                                Log.d("*******", "*********temporaryQteTable**************" +temporaryQteTable);
                                Log.d("*******", "*********qte2**************" +qte2);
                                txtQuantityR.setText(String.valueOf(temporaryQteTable + qte2));

                            } catch (NumberFormatException e) {
                                txtQuantityR.setText(String.valueOf(temporaryQteTable));
                                e.getMessage();
                            }
                        }
                        } */
                }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Ecoute du bouton AJOUTER
     * Ajouter si et seulement si le tableau de résultats dans AutoCompleteProductsAdapter en'est pas null (valueOfProducts > 0)
     */
    public void ecouteAjout(){
        btnEnreg.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                    if (controle.getSizeOfResultProduct() == 0){
                        Toast.makeText(StockActivity.this, "Entrée erronée !", Toast.LENGTH_SHORT).show();
                        }else {
                            checkProduct();
                            txtSearchFilterProduits.getText().clear();
                            btnEnreg.setEnabled(false);
                            txtQte.getText().clear();
                        }
            }
        });
        ecouteBtnValiderStock();
    }

    /**
     * Vérifier dans le tableau utilisateur si le produit a déjà été ajouté ou pas
     * idProduit est un tableau de type Integer qui contient les id des produits
     */
    private void checkProduct() {
        j = tableLayout.getChildCount();
            if (j == 1) {
            ajouterLigne();
            Toast.makeText(StockActivity.this, "Produit Ajouté !", Toast.LENGTH_SHORT).show();
        } else {
            if (j > 1) {
                for (i = 0; i < idProduit.size(); i++) {
                    if (idProduit.get(i) == controle.getIdProd()) {
                        Toast.makeText(StockActivity.this, "Le Produit a déjà été ajouté, modifiez la quantité !", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        if (i == idProduit.size() - 1) {
                            ajouterLigne();
                            Toast.makeText(StockActivity.this, "Produit Ajouté !", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            }
        }

    }

    /**
     * Ajouter la ligne au tableau utilisateur et ajouter l'id du produit dans le tableau idProduit
     */
    private void ajouterLigne(){

            id = controle.getIdProd();
            row = new TableRow(this);

            TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(rowLayoutParams);

            TextView txtCode = new TextView(this);
            txtCode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            txtCode.setText(controle.getCodeProd());

            TextView txtDesignation = new TextView(this);
            txtDesignation.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            txtDesignation.setText(controle.getDesignation());

            txtQuantity = new EditText(this);
            txtQuantity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            txtQuantity.setText(String.valueOf(txtQte.getText()));

            txtQteRestanteTable = new TextView(this);
            txtQteRestanteTable.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            txtQteRestanteTable.setText(String.valueOf(txtQte.getText()));

            btnSupprimer = new ImageButton(this);
            //btnSupprimer.setId(id);
            btnSupprimer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1f));
            btnSupprimer.setEnabled(true);

            idProduit.add(id);
            qte.add(Integer.valueOf(txtQte.getText().toString()));
            row.setId(id);
            row.addView(txtCode);
            row.addView(txtDesignation);
            row.addView(txtQuantity);
            row.addView(txtQteRestanteTable);
            row.addView(btnSupprimer);
            tableLayout.addView(row);
            ecouteBtnSupprimer(id);
            actionSurQuantityFieldTable(id);
            txtQte.getText().clear();
    }

    /**
     * Suppression de la ligne du tableau utiisateur
     * @param id
     */
    private void removeFromTable(int id){
        for (int i = 1; i<tableLayout.getChildCount(); i++) {

            row = (TableRow) tableLayout.getChildAt(i);
            if (row.getId() == id) {
                qte.remove(i-1);
                tableLayout.removeView(row);
            }
        }
    }

    /**
     * Suppression de la ligne du tableau idProduit qui contient l'id du produit à supprimer
     * @param id
     */
    private void removeFromTableidProduit(int id){
        Log.d("id = ","++++++++++++++++++++++++"+id);
        for (int i = 0; i<tableLayout.getChildCount(); i++) {
            if (idProduit.get(i) == id){
                idProduit.remove(i);
                break;
            }
        }
    }

    /**
     * Valider la vente Après avoir ajouté les produits et leurs quantités
     */
    private void ecouteBtnValiderStock(){
        findViewById(R.id.btnValiderStock).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (tableLayout.getChildCount() == 1) {
                    Toast.makeText(StockActivity.this, "Tableau vide !", Toast.LENGTH_SHORT).show();
                }else{
                    int t = checkQuantity();
                    if (t == 0){
                        Toast.makeText(StockActivity.this, "Vous n'avez pas saisi toutes les quantités", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    Log.d("temporaryProductSales :", "*********************" + temporaryProductSales.toString());
                    for (int i=0;i<temporaryProductSales.size();i++){
                        int idProduit = temporaryProductSales.get(i).getIdProduit();
                        int qteIn = temporaryProductSales.get(i).getQteIn();
                        controle.addUpdateMyStock(idProduit,qteIn);
                        Log.d("*****","********BOUCLE*********");
                    }
                    Toast.makeText(StockActivity.this, "Opération effectuée avec succés !", Toast.LENGTH_SHORT).show();
                        openEtatAccueilActivity();
                    }
                }
            }
        });
    }

    /**
     * Vérifier si toutes les quantités ont été saisies
     */
    private int checkQuantity (){

        int t = 2;
        //This will iterate through the table layout and get the total amount of cells.
        for(int i = 1; i < tableLayout.getChildCount(); i++)
        {
                //Remember that .getChildAt() method returns a View, so you would have to cast a specific control.
                row = (TableRow) tableLayout.getChildAt(i);
                TextView txtQuantityTest = (TextView) row.getChildAt(2);
                int quantity = Integer.parseInt(txtQuantityTest.getText().toString());
                        if (quantity != 0) {
                            MyStock stock = new MyStock(0, idProduit.get(i - 1), quantity, controle.getIdUser());
                            Log.d("temporaryProductSales :", "*********************" + temporaryProductSales.toString());
                            temporaryProductSales.add(stock);
                        }
                        t = 1;
        }
        return t;
    }

    private void openEtatStockActivity(){
        Intent intent = new Intent(this, EtatStockActivity.class);
        startActivity(intent);
    }

    private void openEtatAccueilActivity(){
        Intent intent = new Intent(this, AccueilActivity.class);
        startActivity(intent);
    }

}




/**
 * CREER LA LISTE ADAPTER POUR SPINNER

 public void creerListSpinner(){
 ArrayList<Produit> lesProduits = controle.getLesProduits();
 Log.d("creerList() ", "**************************************************"+lesProduits.toString());
 //Collections.sort(lesClients, Collections.<Clients>reverseOrder());  ---> avec compareTo dans Client.java pour trier du plus récent au plus ancien
 spinner = (Spinner) findViewById(R.id.lstProduits);
 ProductListAdapter adapter = new ProductListAdapter(this, controle, lesProduits);
 Log.d("ProductListAdapter ", "**************************************************"+adapter.toString());
 spinner.setAdapter(adapter);
 spinner.setOnItemSelectedListener(adapter);
 Log.d("OnItemSelected ", "**************************************************"+adapter.toString());
 ecouteAjout();
 }
 */

    /*
    public void removeFromList(int id){
        for (Iterator<ImageButton> iter = list.listIterator(); iter.hasNext();) {
            ImageButton imageButton = iter.next();
            if (imageButton.getId() == id) {

                Log.d("LIST :","************************************"+list.toString());
                Log.d("id :","************************************"+id);
                Log.d("i ====","======================="+i);
                Log.d("imageButton.getId() :","************************************"+imageButton.getId());
                Log.d("Iterator : ","********************");
                list.remove(imageButton);
                break;
            }
        }
    }
    */