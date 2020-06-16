package com.example.testcompany.daf.vue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.testcompany.daf.R;
import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.modele.Produit;

import java.util.ArrayList;
import java.util.List;

class AutocompleteProductsAdapter extends ArrayAdapter<Produit> {

    private final List<Produit> lesProduit;
    private final Controle controle;
    private final int i;

    public AutocompleteProductsAdapter(Context context, List<Produit> lesProduits, int i) {
        super(context, 0, lesProduits);
        this.i = i;
        lesProduit = new ArrayList<>(lesProduits);
        controle = Controle.getInstance(null);
    }

    @Override
    public Filter getFilter() {
        return productsFilter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.auto_complete_text_view_products, parent, false);
        }

        TextView txtCode = convertView.findViewById(R.id.txtCode);
        TextView txtDesignation = convertView.findViewById(R.id.txtDesignation);

        Produit produit = getItem(position);

        if (produit != null){
            txtCode.setText(produit.getCode());
            txtDesignation.setText(produit.getDesignation());
        }
        //Log.d("convertView ","=================================");
        return convertView;
    }

    private final Filter productsFilter = new Filter() {
        @Override
        public FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            List<Produit> suggestions = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                suggestions.addAll(lesProduit);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Produit produit : lesProduit){
                    if(produit.getCode().toLowerCase().contains(filterPattern) || produit.getDesignation().toLowerCase().contains(filterPattern)){
                        suggestions.add(produit);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            //Log.d("publishResults :","***********************");
            clear();
            addAll((List) filterResults.values);
            int i = filterResults.count;
            notifyDataSetChanged();
            checkCount(i);
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            //Log.d("convertResults :","***********************");
            int id = ((Produit) resultValue).getId();
            String code = ((Produit) resultValue).getCode();
            String designation = ((Produit) resultValue).getDesignation();
            Double prixUnitaire = ((Produit) resultValue).getPrixUnitaire();
            sendIdProduit(id, code, designation, prixUnitaire);
            return ((Produit) resultValue).getDesignation();
        }
    };

    /**
     * Envoyer l'ID du produit selectionné à :
     * si i == 0 : StockActivity.ecouteAjout()
     * si i == 1 : NewSaleActivity.ecouteAjout()
     * @param id
     * @param code
     * @param designation
     */
    private void sendIdProduit(int id, String code, String designation, Double prixUnitaire){
        if (i == 0){
            Produit produit = new Produit(id, code, designation, null, null, null, prixUnitaire);
            controle.setIdProdStockActivity(produit);
        }else {
            if (i == 1){
            Produit produit = new Produit(id, code, designation, null, null, null, prixUnitaire);
            controle.setIdProdNewSaleActivity(produit);
            }
        }
    }

    /**
     * Envoyer la taille du résultat
     * si resulat = 0 => ne pas Ajouter de ligne
     * si resultat > 0 => ajouter ligne
     * @param id
     */
    private void checkCount(int id){
        if (i == 0){
            controle.checkSizeOfResultProductStock(id);
        }else {
            if (i == 1){
                controle.checkSizeOfResultProductSale(id);
            }
        }
    }
}
