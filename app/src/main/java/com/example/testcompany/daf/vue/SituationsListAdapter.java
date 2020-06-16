package com.example.testcompany.daf.vue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.testcompany.daf.R;
import com.example.testcompany.daf.modele.Situation;

import java.util.ArrayList;

class SituationsListAdapter extends BaseAdapter {

    private final ArrayList<Situation> NewTable;
    private final LayoutInflater inflater;

    public SituationsListAdapter(Context context, ArrayList<Situation> newTable){
        this.NewTable = newTable;
        //il va servir à formater la liste pour pouvoir recevoir les prochains éléments
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return NewTable.size();
    }

    @Override
    public Object getItem(int i) {
        return NewTable.get(i);
    }

    @Override
    public long getItemId(int i) {
        return NewTable.get(i).getIdClient();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewGroup nullParent = null;
        //création d'un holder
        SituationsListAdapter.ViewHolder holder;
        //si la ligne n'existe pas encore
        if(view == null) {
            holder = new SituationsListAdapter.ViewHolder();
            //la ligne est construite avec un formatage (inflater) relié à layout_list_ventes.xml
            view = inflater.inflate(R.layout.layout_list_situations, nullParent);
            //chaque propriété du holder est relié à une propriété graphique

            holder.txtIdClient = view.findViewById(R.id.txtIdClient);
            holder.txtEtablissement = view.findViewById(R.id.txtEtablissement);
            //holder.txtTotal = (TextView)view.findViewById(R.id.txtTotal);
            holder.txtSolde = view.findViewById(R.id.txtSolde);
            holder.txtDate = view.findViewById(R.id.txtDate);
            //affecter le holder à la vue
            view.setTag(holder);
        }else {
            //récupération du holder dans la ligne existante
            holder = (SituationsListAdapter.ViewHolder) view.getTag();
        }
        //valorisation du contenu du holder(remplissage du holder donc de la ligne)

        holder.txtIdClient.setText(String.valueOf(NewTable.get(i).getIdClient()));
        holder.txtEtablissement.setText(NewTable.get(i).getEts());
        //holder.txtTotal.setText(NewTable.get(i).getTotal().toString());
        holder.txtSolde.setText(String.valueOf(NewTable.get(i).getSolde()));
        holder.txtDate.setText(NewTable.get(i).getDate());
        return view;
    }

    private class ViewHolder{
        TextView txtIdClient;
        TextView txtEtablissement;
        //TextView txtTotal;
        TextView txtSolde;
        TextView txtDate;
    }
}
