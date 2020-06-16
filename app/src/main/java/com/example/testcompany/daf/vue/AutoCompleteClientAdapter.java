package com.example.testcompany.daf.vue;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.testcompany.daf.R;
import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.modele.Client;

import java.util.ArrayList;
import java.util.List;

class AutoCompleteClientAdapter extends ArrayAdapter<Client> {

    private int idClientSelected;
    private String nomClientSelected;
    private final List<Client> lesClient;

    private final Controle controle;

    public AutoCompleteClientAdapter(Context context, List<Client> lesClients) {
        super(context, 0, lesClients);
        lesClient = new ArrayList<>(lesClients);
        controle = Controle.getInstance(null);
    }

    @Override
    public Filter getFilter() {
        return clientsFilter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.auto_complete_text_view_clients, parent, false);
        }

        TextView txtEts = convertView.findViewById(R.id.txtEtablissement);

        Client client = getItem(position);

        if (client != null){
            txtEts.setText(client.getEtablissement());
            //txtName.setText(client.getNom());
        }
        return convertView;
    }

    private final Filter clientsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            List<Client> suggestions = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                suggestions.addAll(lesClient);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Client client : lesClient){
                    if(client.getEtablissement().toLowerCase().contains(filterPattern)){
                        suggestions.add(client);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((List) filterResults.values);
            int i = filterResults.count;
            notifyDataSetChanged();
            checkCount(i);
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {

            idClientSelected = ((Client) resultValue).getId();
            nomClientSelected = ((Client) resultValue).getNom();
            sendIdClient(idClientSelected, nomClientSelected);
            //Log.d("idClientSeleceted :","**********"+idClientSelected);
            return ((Client) resultValue).getEtablissement();
        }
    };

    private void sendIdClient(int id, String nom){

        Log.d("id =","*************idClient AutoComplete****************"+id);
        Client client = new Client(id, null, null, null, nom, null , null , null , null, null, null, 0);
        controle.setIdClient(client);
    }

    /**
     * Envoyer la taille du résultat
     * si resulat = 0 => ne pas Ajouter de ligne
     * si resultat > 0 => ajouter ligne
     * @param i
     */
    private void checkCount(int i){
        controle.checkSizeOfResultClient(i);
    }
}
