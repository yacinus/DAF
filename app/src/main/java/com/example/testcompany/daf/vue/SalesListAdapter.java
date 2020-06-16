package com.example.testcompany.daf.vue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.testcompany.daf.R;
import com.example.testcompany.daf.modele.ClientVente;

import java.util.ArrayList;

class SalesListAdapter extends BaseAdapter {

    private final ArrayList<ClientVente> NewTable;
    private final LayoutInflater inflater;

    public SalesListAdapter(Context context, ArrayList<ClientVente> newTable){
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
        return NewTable.get(i).getIdVente();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewGroup nullParent = null;
        //création d'un holder
        ViewHolder holder;
        //si la ligne n'existe pas encore
        if(view == null) {
            holder = new SalesListAdapter.ViewHolder();
            //la ligne est construite avec un formatage (inflater) relié à layout_list_ventes.xml
            view = inflater.inflate(R.layout.layout_list_ventes, nullParent);
            //chaque propriété du holder est relié à une propriété graphique
            holder.txtIdVente = view.findViewById(R.id.txtIdVente);
            holder.txtTotal = view.findViewById(R.id.txtTotal);
            holder.txtSolde = view.findViewById(R.id.txtSolde);
            holder.txtDate = view.findViewById(R.id.txtDate);
            //affecter le holder à la vue
            view.setTag(holder);
        }else {
            //récupération du holder dans la ligne existante
            holder = (SalesListAdapter.ViewHolder) view.getTag();
        }
            //valorisation du contenu du holder(remplissage du holder donc de la ligne)

            holder.txtIdVente.setText(String.valueOf(NewTable.get(i).getIdVente()));
            holder.txtTotal.setText(NewTable.get(i).getTotal().toString());
            holder.txtSolde.setText(NewTable.get(i).getSolde().toString());
            holder.txtDate.setText(NewTable.get(i).getDate());
            //a quelle ligne correspond le bouton, faire un getTag pour recuperer sa position de façon à effacer la ligne correspondante
            //holder.btnListSuppr.setTag(i);
            //return view;
        return view;
    }

    private class ViewHolder{
        TextView txtIdVente;
        TextView txtTotal;
        TextView txtSolde;
        TextView txtDate;
    }
}
