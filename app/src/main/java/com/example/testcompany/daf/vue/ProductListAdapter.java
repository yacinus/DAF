package com.example.testcompany.daf.vue;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.testcompany.daf.R;
import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.modele.Produit;

import java.util.ArrayList;

class ProductListAdapter extends BaseAdapter implements AdapterView.OnItemSelectedListener {

    private final Context context;
    private final ArrayList<Produit> lesProduits;
    private LayoutInflater inflater;
    private Produit produit;
    private int id1;
    private final Controle controle;

    public ProductListAdapter(Context context, Controle controle, ArrayList<Produit> lesProduits) {
        this.lesProduits = lesProduits;
        this.context = context;
        //il va servir à formater la liste pour pouvoir recevoir les prochains éléments
        this.inflater = LayoutInflater.from(context);
        this.controle = controle;
    }

    @Override
    public int getCount() {
        return lesProduits.size();
    }

    @Override
    public Produit getItem(int position) {
        return lesProduits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Position ", " : " + position);
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_product_spinner_activity, parent, false);
        }
        TextView txtCode = convertView.findViewById(R.id.txtCode);
        TextView txtDesignation = convertView.findViewById(R.id.txtDesignation);
        produit = getItem(position);
        if (produit != null) {
            String code = produit.getCode();
            String designation = produit.getDesignation();
            id1 = produit.getId();
            txtCode.setText(produit.getCode());
            txtDesignation.setText(produit.getDesignation());
            Produit produit = new Produit(id1, code, designation, null, null, null, null);
            controle.setIdProdStockActivity(produit);
            Log.d("ID :","******************************"+id1);
        }
        return convertView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
