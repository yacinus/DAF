package com.example.testcompany.daf.vue;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.testcompany.daf.R;
import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.modele.Client;
import com.example.testcompany.daf.modele.MyStock;
import com.example.testcompany.daf.modele.Produit;
import com.example.testcompany.daf.modele.StockClient;
import com.example.testcompany.daf.modele.Vente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class NewSaleActivity extends AppCompatActivity {

    /**
     * Propriétés
     * @param savedInstanceState
     */
    private ImageView btnModifierVente;
    private ImageView closeAlert;
    private TextView enTete;
    private Button btnAjouter;
    private Button btnValiderVente;
    private ImageView btnSupprimer;
    private TextView txtTotal;
    private TextView txtQteRestanteTable;
    private TextView txtAccompte;
    private TextView txtSolde;
    private TextView txtPrixUnitaire;
    private Double total;
    private Double accompte;
    private Double solde;
    private ImageButton btn1;
    private int id;
    private int temporaryQte;
    private int qte2;
    private int qte = 0;
    private int qteFinal = 0;
    private int txtQ1;
    private TextView txtQteRestante;
    private EditText txtQte;
    private ArrayList<MyStock> myStock = new ArrayList<>();
    private final ArrayList<Integer> idProduit = new ArrayList<>();
    private ArrayList<StockClient> stockClient;
    private ArrayList<StockClient> temporaryStockClient;
    private final ArrayList<Integer> temporaryQteTable = new ArrayList<>();
    private final ArrayList<Integer> temporaryQteOutTable = new ArrayList<>();
    private EditText txtQuantity;
    private EditText txtPrix;
    private Controle controle;
    private ArrayList<Client> lesClients;
    private AutoCompleteTextView txtSearchFilter;
    private AutoCompleteTextView txtSearchFilterProduits;
    private TableLayout tableLayout;
    private LinearLayout layoutBtnAjouter;
    private TableRow row;
    private ArrayList<StockClient> temporaryProductSales = new ArrayList<>();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private AlertDialog progressDialog;

    public NewSaleActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sale);
        init();
    }

    private void init(){
        btnAjouter = findViewById(R.id.btnAjouter);
        btnValiderVente = findViewById(R.id.btnValiderVente);
        layoutBtnAjouter = findViewById(R.id.layoutBtnAjouter);
        enTete = findViewById(R.id.enTete);
        tableLayout = findViewById(R.id.tableLayout);
        txtSearchFilter = findViewById(R.id.txtSearchFilter);
        txtSearchFilterProduits = findViewById(R.id.txtSearchFilterProduit);
        txtQteRestante = findViewById(R.id.txtQteRestante);
        txtQte = findViewById(R.id.txtQte);
        txtPrixUnitaire = findViewById(R.id.txtPrixUnitaire);
        FloatingActionButton back = findViewById(R.id.backBtn);
        ecouteBtnBack(back);
        controleStart();
    }

    private void controleStart(){
        Log.d("controleStart() ", "**************************************************"+toString());
        this.controle = Controle.getInstance(this);
        this.controle.getProduitsMyStock();
        this.controle.getClients();
        this.controle.getMyStock();
        myStock = controle.getMyFinalStock();
        lesClients = controle.getLesClients();
        Log.d("idVente ","*********controle.idVente()************"+controle.getIdVente());
        checkTypeOfVente();
        actionSurTxtSearchFilterProduits();
        actionSurQuantityField();
        ecouteAjout();
        ecouteBtnValiderVente();
    }

    private void ecouteBtnBack(FloatingActionButton back){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void checkTypeOfVente(){
        temporaryStockClient = new ArrayList<>();
        if (controle.getIdVente() == 0){
            setAdapterClients();
            setAdapterProduits();
        }else{
            //si idVente != 0 => NewSaleActivity est en mode modifier Vente => générer le nom du client et le tableau qui contient :
            // codeProduit,DesignationProduit,QteOut,PrixUnitaire
            this.controle.recupInfosClient();
            int idVente = controle.getIdVente();
            enTete.setText("Vente N° " + idVente);
            txtSearchFilter.setText(controle.recupEtablissementClient());
            txtSearchFilter.setEnabled(false);
            btnAjouter.setEnabled(false);
            txtQte.setEnabled(false);
            txtSearchFilterProduits.setEnabled(false);
            btnValiderVente.setEnabled(false);
            Log.d("AVANT ","*********AVANT GENERE TABLEAU************");
            genereBoutonModifier();
            stockClient = controle.recupLeStockClient();
            temporaryStockClient = genererCopieTableau(stockClient);
            genereTableau(stockClient);
            Log.d("APRES ","*********APRES GENERE TABLEAU************");
            ecouteBtnModifier();
        }
    }

    private ArrayList<StockClient> genererCopieTableau (ArrayList<StockClient> stockClient){
        ArrayList<StockClient> copieStockClient = new ArrayList<>();
        for (int i=0;i<stockClient.size();i++){
            copieStockClient.add(stockClient.get(i));
        }
        return copieStockClient;
    }

    /**
     * CREER LA LISTE ADAPTER CLIENTS
     */
    private void setAdapterClients(){

        Log.d("*****","********lesClients******************"+lesClients);
            if (lesClients != null) {
                Log.d("*****","********setAdapterClients******************");
                AutoCompleteClientAdapter adapter = new AutoCompleteClientAdapter(this, lesClients);
                txtSearchFilter.setAdapter(adapter);
                Log.d("sizeOfResultClient","***********sizeOfResultClient**************"+controle.getSizeOfResultClient());
                Log.d("controle.getIdClient()","***********controle.getIdClient()**************"+controle.getIdClient());
            }
    }

    /**
     * CREER LA LISTE DES PRODUITS
     */
    private void setAdapterProduits(){
        ArrayList<Produit> lesProduits = controle.getLesProduitsMyStock();
        if(lesProduits != null) {
            AutocompleteProductsAdapter adapter = new AutocompleteProductsAdapter(this, lesProduits, 1);
            txtSearchFilterProduits.setAdapter(adapter);
        }
    }

    private void genereBoutonModifier(){
        Log.d("AVANT ","*********controle.idVente************"+controle.idVente);
        btnModifierVente = new ImageView(this);
        LinearLayout.LayoutParams btnLayoutParams = new LinearLayout.LayoutParams(80,80, 1f);
        btnLayoutParams.gravity = Gravity.CENTER;
        btnModifierVente.setImageResource(R.drawable.edit);
        btnModifierVente.setLayoutParams(btnLayoutParams);
        layoutBtnAjouter.addView(btnModifierVente);

    }

    private void ecouteBtnModifier(){
        btnModifierVente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtSearchFilter.setEnabled(true);
                txtSearchFilterProduits.setEnabled(true);
                txtQte.setEnabled(true);
                btnAjouter.setEnabled(true);
                btnValiderVente.setEnabled(true);
                txtSearchFilter.getText().clear();
                setAdapterClients();
                txtSearchFilter.setText(controle.recupEtablissementClient());
                setAdapterProduits();
                int i = 1;
                while (i<tableLayout.getChildCount()){
                    row = (TableRow) tableLayout.getChildAt(i);
                    EditText txtQuantityView = (EditText) row.getChildAt(2);
                    txtQuantityView.setEnabled(true);
                    EditText txtPrixView = (EditText) row.getChildAt(4);
                    txtPrixView.setEnabled(true);
                    ImageButton btnSupp = (ImageButton) row.getChildAt(5);
                    btnSupp.setEnabled(true);
                    i++;
                }
            }
        });
    }

    private void genereTableau (ArrayList<StockClient> stock){

        for (int i=0;i<stock.size();i++){
            id = stock.get(i).getIdProduitStockClient();
            controle.getProduit(id);
            controle.getQteFromDataBase(id);
            int qteIn = controle.getTheQteFromDataBase();
            row = new TableRow(this);

            TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
            rowLayoutParams.setMargins(1,2,1,2);
            row.setLayoutParams(rowLayoutParams);

            TextView txtCode = new TextView(this);
            txtCode.setLayoutParams(rowLayoutParams);
            txtCode.setPadding(10,0,0,0);
            txtCode.setTextSize(8);txtCode.setText(controle.getLeProduit().getCode());

            TextView txtDesignation = new TextView(this);
            txtDesignation.setLayoutParams(rowLayoutParams);
            txtDesignation.setPadding(10,0,0,0);
            txtDesignation.setTextSize(10);
            txtDesignation.setText(controle.getLeProduit().getDesignation());

            txtQuantity = new EditText(this);
            txtQuantity.setLayoutParams(rowLayoutParams);
            txtQuantity.setPadding(10,0,0,0);
            txtQuantity.setTextSize(10);
            txtQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            txtQuantity.setText(String.valueOf(stock.get(i).getQteOut()));
            txtQuantity.setEnabled(false);

            txtQteRestanteTable = new TextView(this);
            txtQteRestanteTable.setLayoutParams(rowLayoutParams);
            txtQteRestanteTable.setPadding(10,0,0,0);
            txtQteRestanteTable.setTextSize(10);txtQteRestanteTable.setText(String.valueOf(qteIn));

            txtPrix = new EditText(this);
            txtPrix.setLayoutParams(rowLayoutParams);
            txtPrix.setInputType(InputType.TYPE_CLASS_NUMBER);
            txtPrix.setPadding(10,0,0,0);
            txtPrix.setTextSize(10);
            txtPrix.setText(String.valueOf(stock.get(i).getPrixUnitaire()));
            txtPrix.setEnabled(false);

            btnSupprimer = new ImageButton(this);
            btnSupprimer.setLayoutParams(new TableRow.LayoutParams(50, 50, 1f));
            btnSupprimer.setImageResource(R.drawable.delete);
            btnSupprimer.setBackgroundColor(Color.TRANSPARENT);btnSupprimer.setEnabled(false);

            idProduit.add(id);
            temporaryQteTable.add(qteIn);
            temporaryQteOutTable.add(stock.get(i).getQteOut());
            row.setId(id);
            row.addView(txtCode);
            row.addView(txtDesignation);
            row.addView(txtQuantity);
            row.addView(txtQteRestanteTable);
            row.addView(txtPrix);
            row.addView(btnSupprimer);
            tableLayout.addView(row);
            ecouteBtnSupprimer(id);
            actionSurQuantityFieldTable(id);
        }
    }

    /**
     * Action sur txtSearchFilterProduits
     */
    private void actionSurTxtSearchFilterProduits(){

        txtSearchFilterProduits.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int q = 0;
                Log.d("*****","***********getSizeOfResultProduct**************"+controle.getSizeOfResultProduct());
                Log.d("*****","***********MY STOCK**************"+myStock.toString());
                try{

                    while(q<myStock.size()) {
                        if (controle.getSizeOfResultProduct() > 0) {
                            Log.d("*****","***********controle.getIdProd()**************"+controle.getIdProd());
                            if (controle.getIdProd() == myStock.get(q).getIdProduit()) {
                                qte = myStock.get(q).getQteIn();
                                Log.d("*****","***********QTE**************"+qte);
                                txtQteRestante.setText(String.valueOf(qte).trim());
                                //txtPrixUnitaire.setText(String.format(Locale.getDefault(),"%.2f",controle.getPrixProduit()));
                                txtPrixUnitaire.setText(String.valueOf(controle.getPrixProduit()));
                                break;
                            }else{
                                txtQteRestante.setText(null);
                                txtPrixUnitaire.setText(null);
                            }
                        }
                        q++;
                    }
                }
                catch(Exception e){
                    Log.d("", e.getMessage());

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnAjouter.setEnabled(!txtSearchFilterProduits.getText().toString().isEmpty());
                if (txtSearchFilterProduits.getText().toString().isEmpty()){
                    txtQteRestante.setText(null);
                    txtPrixUnitaire.setText(null);
                }
            }
        });
    }

    /**
     * Action sur l'edit Text de txtQte à coté de produit
     */
    private void actionSurQuantityField(){
        txtQte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txtQte.getText().toString().isEmpty() && !txtQteRestante.getText().toString().isEmpty()){

                    txtQ1 = Integer.valueOf(txtQte.getText().toString());
                    if (txtQ1 > qte){
                        Toast.makeText(NewSaleActivity.this, "La quantité est supérieure au stock !", Toast.LENGTH_SHORT).show();
                        txtQteRestante.setText(String.valueOf(qte));
                    }else{

                        qteFinal = qte - txtQ1;
                        txtQteRestante.setText(String.valueOf(qteFinal));
                    }
                }else{
                    if (txtQte.getText().toString().isEmpty()){
                        txtQteRestante.setText(String.valueOf(qte));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Action sur l'edit Text de txtQuantity (Tableau)
     * @param id
     */
    private void actionSurQuantityFieldTable(final int id){
        txtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                for (int s=1;s<tableLayout.getChildCount();s++){
                    row = (TableRow) tableLayout.getChildAt(s);
                    Log.d("*******","******row.getId()*********"+row.getId());
                    Log.d("*******","******id*********"+id);
                    if (row.getId() == id) {
                        TextView txtQuantity = (TextView) row.getChildAt(2);
                        TextView txtQuantityR = (TextView) row.getChildAt(3);
                        temporaryQte = temporaryQteTable.get(s-1);   //qteIn
                        int newQte = temporaryQte;
                        if (temporaryStockClient.isEmpty()){

                            txtQuantityR.setText(String.valueOf(newQte));

                            if (!txtQuantity.getText().toString().isEmpty()){
                                qte2 = Integer.parseInt(txtQuantity.getText().toString());
                                txtQuantityR.setText(String.valueOf(newQte - qte2));
                            }
                        }else{

                            for (int m=0;m<temporaryStockClient.size();m++){
                                if (temporaryStockClient.get(m).getIdProduitStockClient() == id){
                                    int qteOut = temporaryStockClient.get(m).getQteOut();
                                    newQte = temporaryQte+qteOut;
                                    txtQuantityR.setText(String.valueOf(newQte));

                                    if (!txtQuantity.getText().toString().isEmpty()){
                                        qte2 = Integer.parseInt(txtQuantity.getText().toString());
                                        txtQuantityR.setText(String.valueOf(newQte - qte2));
                                    }
                                    break;
                                }else{
                                    if (m == temporaryStockClient.size()-1){

                                        txtQuantityR.setText(String.valueOf(newQte));

                                        if (!txtQuantity.getText().toString().isEmpty()){
                                            qte2 = Integer.parseInt(txtQuantity.getText().toString());
                                            txtQuantityR.setText(String.valueOf(newQte - qte2));
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        });
    }

    public void ecouteAjout(){
        findViewById(R.id.btnAjouter).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                    if (controle.getSizeOfResultProduct() == 0){
                        Toast.makeText(NewSaleActivity.this, "Entrée erronée !", Toast.LENGTH_SHORT).show();
                    }else {
                        if (txtQ1 > qte){
                            Toast.makeText(NewSaleActivity.this, "Veuillez modifier la quantité > Qte Restante !", Toast.LENGTH_SHORT).show();
                        }else {
                            checkProduct();
                            txtQteRestante.setText(null);
                            btnAjouter.setEnabled(false);
                            txtQte.getText().clear();
                            if (tableLayout.getChildCount() > 1) {
                                btnValiderVente.setEnabled(true);
                            }
                        }
                    }
            }
        });
        ecouteBtnValiderVente();
    }

    /**
     * Vérifier dans le tableau utilisateur si le produit a déjà été ajouté ou pas
     * idProduit est un tableau de type Integer qui contient les id des produits
     */
    private void checkProduct() {
        int j = tableLayout.getChildCount();
        Log.d("checkProduct()","**********idProduit.size()*****************"+idProduit.size());
        if (j == 1) {
            ajouterLigne();
            Toast.makeText(NewSaleActivity.this, "Produit Ajouté !", Toast.LENGTH_SHORT).show();
            txtSearchFilterProduits.getText().clear();
        } else {
            if (j > 1) {
                for (int i = 0; i < idProduit.size(); i++) {
                    if (idProduit.get(i) == controle.getIdProd()) {
                        Toast.makeText(NewSaleActivity.this, "Le Produit a déjà été ajouté, modifiez la quantité !", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        if (i == idProduit.size() - 1) {
                            ajouterLigne();
                            Toast.makeText(NewSaleActivity.this, "Produit Ajouté !", Toast.LENGTH_SHORT).show();
                            txtSearchFilterProduits.getText().clear();
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

        TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
        rowLayoutParams.setMargins(1,2,1,2);
        row.setLayoutParams(rowLayoutParams);

        TextView txtCode = new TextView(this);
        txtCode.setLayoutParams(rowLayoutParams);
        txtCode.setPadding(10,0,0,0);
        txtCode.setTextSize(8);
        txtCode.setText(controle.getCodeProd());

        TextView txtDesignation = new TextView(this);
        txtDesignation.setLayoutParams(rowLayoutParams);
        txtDesignation.setPadding(10,0,0,0);
        txtDesignation.setTextSize(10);
        txtDesignation.setText(controle.getDesignation());

        txtQuantity = new EditText(this);
        txtQuantity.setLayoutParams(rowLayoutParams);
        txtQuantity.setPadding(10,0,0,0);
        txtQuantity.setTextSize(10);
        txtQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        txtQuantity.setText(txtQte.getText().toString());

        txtQteRestanteTable = new TextView(this);
        txtQteRestanteTable.setLayoutParams(rowLayoutParams);
        txtQteRestanteTable.setPadding(10,0,0,0);
        txtQteRestanteTable.setTextSize(10);
        txtQteRestanteTable.setText(String.valueOf(qteFinal));

        try{
            txtPrix = new EditText(this);
            txtPrix.setLayoutParams(rowLayoutParams);
            txtPrix.setPadding(10,0,0,0);
            txtPrix.setTextSize(10);
            txtPrix.setText(txtPrixUnitaire.getText().toString());
            txtPrix.setInputType(InputType.TYPE_CLASS_NUMBER);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }


        btnSupprimer = new ImageButton(this);
        btnSupprimer.setLayoutParams(new TableRow.LayoutParams(50, 50, 1f));
        btnSupprimer.setImageResource(R.drawable.delete);
        btnSupprimer.setBackgroundColor(Color.TRANSPARENT);

        idProduit.add(id);
        temporaryQteTable.add(qte);
        row.setId(id);
        row.addView(txtCode);
        row.addView(txtDesignation);
        row.addView(txtQuantity);
        row.addView(txtQteRestanteTable);
        row.addView(txtPrix);
        row.addView(btnSupprimer);
        tableLayout.addView(row);
        ecouteBtnSupprimer(id);
        actionSurQuantityFieldTable(id);
        txtQte.getText().clear();
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

                btn1 = (ImageButton) row.getChildAt(5);
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("*******","***ecouteBtnSupprimer***OnClick****************");
                        if (tableLayout.getChildCount() == 2) {
                            btnValiderVente.setEnabled(false);
                        }
                        removeFromTableidProduit(id);
                        addOldQteOutToMyStock(id);
                        removeFromTable(id);
                    }
                });
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
     * Suppression de la ligne du tableau utiisateur
     * @param id
     */
    private void removeFromTable(int id){
        for (int i = 1; i<tableLayout.getChildCount(); i++) {

            row = (TableRow) tableLayout.getChildAt(i);
            if (row.getId() == id) {
                temporaryQteTable.remove(i-1);
                tableLayout.removeView(row);
            }
        }
    }

    private void addOldQteOutToMyStock (int id){
        int qteIn;
        int qteOut = 0;
        for (int j=0;j<temporaryStockClient.size();j++){

            if (temporaryStockClient.get(j).getIdProduitStockClient() == id){
                qteOut = temporaryStockClient.get(j).getQteOut();
                temporaryQteOutTable.remove(j);
                temporaryStockClient.remove(j);
                break;
            }

        }

        for (int i = 0;i<myStock.size();i++){

                if (myStock.get(i).getIdProduit() == id){
                    qteIn = myStock.get(i).getQteIn();
                    int qteFinal = qteIn + qteOut;
                    myStock.get(i).setQteIn(qteFinal);

                }
        }
    }

    private void ecouteBtnValiderVente(){
        findViewById(R.id.btnValiderVente).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                boolean t = checkQuantity();
                int sizeOfResult = controle.getSizeOfResultClient();
                    if (sizeOfResult == 0 || txtSearchFilter.getText().toString().isEmpty()) {
                            Toast.makeText(NewSaleActivity.this, "Client Inexistant !", Toast.LENGTH_SHORT).show();
                        }else{
                        if (!t){
                            Toast.makeText(NewSaleActivity.this, "Veuillez saisir une quantité > 0 ou supprimez la ligne !", Toast.LENGTH_SHORT).show();
                        }else{
                            alertDialog();
                            ProgressDialog();
                        }
                }

            }
        });
    }

    private boolean checkQuantity (){
        boolean t = false;
        //This will iterate through the table layout and get the total amount of cells.
        for(int i = 1; i < tableLayout.getChildCount(); i++)
        {
            //Remember that .getChildAt() method returns a View, so you would have to cast a specific control.
            row = (TableRow) tableLayout.getChildAt(i);
            TextView txtQuantityView = (TextView) row.getChildAt(2);
            String quantity = txtQuantityView.getText().toString();

            if (quantity.trim().isEmpty() || quantity.trim().equals("0")) {
               t = false;
               return t;
            } else {
                t = true;
            }
        }
        return t;
    }

    private ArrayList<StockClient> remplirProductSales (){
        ArrayList<StockClient> temporaryProductSales = new ArrayList<>();

        Log.d("tableLayout :", "**********tableLayout.getChildCount()***********" +tableLayout.getChildCount());
        int i = 1;
        int count = tableLayout.getChildCount();
        while (i < count)
        {
            //Remember that .getChildAt() method returns a View, so you would have to cast a specific control.
            row = (TableRow) tableLayout.getChildAt(i);
            TextView txtQuantityView = (TextView) row.getChildAt(2);
            TextView txtPrixUnitaireView = (TextView) row.getChildAt(4);

            int quantity = 0;
            Double prixUnitaire = 0.00;

            if (!txtQuantityView.getText().toString().trim().isEmpty()) {
                Log.d("*****", "***************txtQuantityView.getText().toString()*********************" + txtQuantityView.getText().toString());
                quantity = Integer.valueOf(txtQuantityView.getText().toString());
            }
            if (!txtPrixUnitaireView.getText().toString().trim().isEmpty()) {
                    prixUnitaire = Double.valueOf(txtPrixUnitaireView.getText().toString());
            }

                StockClient stock = new StockClient(0,idProduit.get(i-1),0, quantity, prixUnitaire);
                temporaryProductSales.add(stock);
                Log.d("temporaryProductSales :", "*********************" + temporaryProductSales.toString());

            i++;
        }
        return temporaryProductSales;
    }

    private void alertDialog(){
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewSaleActivity.this);
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_sale, nullParent);
        alertDialog.setView(view);
        AlertDialog dialog = alertDialog.create();
        dialog.show();

        closeAlert = view.findViewById(R.id.closeIcon);
        txtTotal = view.findViewById(R.id.txtTotal);
        txtAccompte = (EditText)view.findViewById(R.id.txtAccompte);
        txtSolde = view.findViewById(R.id.txtSolde);
        final Button btnFinalier = view.findViewById(R.id.btnFinaliser);

        txtTotal.setText(decimalFormat.format(somme()).replace(",","."));

        try{
            if (controle.idVente != 0) {
                txtAccompte.setText(decimalFormat.format(controle.recupAccompteClient()).replace(",","."));
                total = Double.valueOf(String.valueOf(txtTotal.getText()).replace(",","."));
                accompte = Double.valueOf(String.valueOf(txtAccompte.getText()).replace(",","."));
                txtSolde.setText(decimalFormat.format(total - accompte).replace(",","."));
                btnFinalier.setEnabled(true);
            }
        }catch(NumberFormatException e){
            e.printStackTrace();
        }


        txtAccompte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    Log.d("txtTotal","******************"+txtTotal.toString());
                    Log.d("txtAccompte","***************"+txtAccompte.toString());
                    Log.d("txtSoldeBefore","******************"+txtSolde.toString());
                    total = Double.valueOf(txtTotal.getText().toString());
                    if (txtAccompte.getText().toString().isEmpty()){
                        accompte = null;
                        solde = total;
                    }else{
                        accompte = Double.valueOf(txtAccompte.getText().toString());
                        solde = total - accompte;
                    }
                    txtSolde.setText(decimalFormat.format(solde).replace(",","."));
                    Log.d("txtSoldeAfter","******************"+txtSolde);
                    btnFinalier.setEnabled(true);
                }catch(NumberFormatException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (txtAccompte.getText().toString().isEmpty()) {
                        total = Double.valueOf(txtTotal.getText().toString());
                        txtSolde.setText(decimalFormat.format(total));
                        Toast.makeText(NewSaleActivity.this, "Saisissez 0.0 si Accompte null !", Toast.LENGTH_SHORT).show();
                        btnFinalier.setEnabled(false);
                    } else {
                        try {
                            total = Double.valueOf(txtTotal.getText().toString());
                            accompte = Double.valueOf(txtAccompte.getText().toString());
                            solde = total - accompte;
                            txtSolde.setText(decimalFormat.format(solde).replace(",", "."));
                            btnFinalier.setEnabled(true);
                        } catch (NumberFormatException e) {
                            e.getMessage();
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        btnFinalier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFinalier.setEnabled(false);
                if (!txtAccompte.getText().toString().trim().isEmpty()){
                    temporaryProductSales = remplirProductSales();
                    Double total = Double.valueOf(decimalFormat.format(Double.valueOf(String.valueOf(txtTotal.getText()))).replace(",","."));
                    Double accompte = Double.valueOf(decimalFormat.format(Double.valueOf(String.valueOf(txtAccompte.getText()))).replace(",","."));
                    Double solde = Double.valueOf(decimalFormat.format(Double.valueOf(String.valueOf(txtSolde.getText()))).replace(",","."));
                    if (controle.idVente == 0) {
                        Log.d("getIdUser : ", "*********************" + controle.getIdUser());
                        Vente vente = new Vente(0, controle.getIdUser(), controle.getIdClient(), Float.parseFloat(total.toString()), Float.parseFloat(accompte.toString()), Float.parseFloat(solde.toString()), null);
                        //=====================================================================
                        //======l'id de la vente est unique, donc on modifie la vente selon son id, valable aussi pour
                        //stock.idVente   !!!!! :)
                        //=====================================================================
                        Log.d("temporaryProductSales :", "*********************" + temporaryProductSales.toString());
                        controle.ajouterVente(vente, temporaryProductSales);
                        Toast.makeText(NewSaleActivity.this, "Opération effectuée avec succés !", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        progressDialog.show();
                        openEtatStockActivity();
                    } else {
                        Log.d("ùùùùùù", "ùùùùùùùùùùùùùùùcontrole.recupIdDuClient()ùùùùùùùùùùùùùùù" + controle.recupIdDuClient());
                        Vente vente = new Vente(controle.idVente, controle.getIdUser(), controle.getIdClient(), Float.parseFloat(total.toString()), Float.parseFloat(accompte.toString()), Float.parseFloat(solde.toString()), null);
                        //stockClient = controle.recupLeStockClient();
                        controle.modifierVente(vente, stockClient, temporaryProductSales);
                        //Reinitialiser les id vente et client
                        controle.setIdVente(0);
                        Toast.makeText(NewSaleActivity.this, "Modification effectuée avec succés !", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        progressDialog.show();
                        openEtatStockActivity();
                    }
                }
            }
        });

        closeAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onStop() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
        super.onStop();
    }

    private Double somme() {
        Double total = 0.00;
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            int quantity = 0;
            //Remember that .getChildAt() method returns a View, so you would have to cast a specific control.
            row = (TableRow) tableLayout.getChildAt(i);
            TextView txtQte = (TextView) row.getChildAt(2);
            TextView txtPrix = (TextView) row.getChildAt(4);
            if (!txtQte.getText().toString().isEmpty()) {
                quantity = Integer.valueOf(txtQte.getText().toString());
            }

            if (!txtPrix.getText().toString().trim().isEmpty()) {
                Double prix = Double.valueOf(txtPrix.getText().toString());
                total += (prix * quantity);
                Log.d("total =", "================================" + total);

            }
        }
            return total;
        }

    private void ProgressDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewSaleActivity.this);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.circular_progress_bar, null);
        alertDialog.setView(view);
        progressDialog = alertDialog.create();
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void openEtatStockActivity(){
        Intent intent = new Intent(this, EtatStockActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        controle.setIdVente(0);
        this.finish();
        return;
    }
}
