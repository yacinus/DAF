package com.example.testcompany.daf.vue;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.testcompany.daf.R;
import com.example.testcompany.daf.modele.Client;

import java.util.ArrayList;

class ClientListAdapter extends BaseAdapter {

    private final ArrayList<Client> lesClients;
    private final LayoutInflater inflater;

    public ClientListAdapter(Context context, ArrayList<Client> lesClients){
        this.lesClients = lesClients;
        //il va servir à formater la liste pour pouvoir recevoir les prochains éléments
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return lesClients.size();
    }

    @Override
    public Object getItem(int i) {
        return lesClients.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lesClients.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewGroup nullParent = null;
        //création d'un holder
        ViewHolder holder;
        //si la ligne n'existe pas encore
        if(view == null) {
        holder = new ViewHolder();
        //la ligne est construite avec un formatage (inflater) relié à layout_list_cliens.xml
            view = inflater.inflate(R.layout.layout_list_clients, nullParent);
        //chaque propriété du holder est relié à une propriété graphique
            holder.txtEts = view.findViewById(R.id.txtEtablissement);
            holder.txtNomPrenom = view.findViewById(R.id.txtNomPrenom);
            //affecter le holder à la vue
            view.setTag(holder);
        }else {
            //récupération du holder dans la ligne existante
            holder = (ViewHolder) view.getTag();
        }
        //valorisation du contenu du holder(remplissage du holder donc de la ligne)
        Log.d("Remplissage ligne ", "**************************************************"+lesClients.get(i).getEtablissement());
        Log.d("Remplissage ligne ", "**************************************************"+lesClients.get(i).getNom());
        holder.txtEts.setText(lesClients.get(i).getEtablissement());
        holder.txtNomPrenom.setText(lesClients.get(i).getNom().concat(" ").concat(lesClients.get(i).getPrenom()));
        //holder.txtNomPrenom.setText(" ");
        //holder.txtNomPrenom.setText(lesClients.get(i).getPrenom());
        //a quelle ligne correspond le bouton, faire un getTag pour recuperer sa position de façon à effacer la ligne correspondante
        //holder.btnListSuppr.setTag(i);
        return view;
    }

    private class ViewHolder{
        TextView txtEts;
        TextView txtNomPrenom;
    }
}
