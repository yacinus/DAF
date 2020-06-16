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
import com.example.testcompany.daf.modele.Client;

import java.util.ArrayList;

class ClientSpinnerAdapter extends BaseAdapter implements AdapterView.OnItemSelectedListener {

    private final Context context;
    private final ArrayList<Client> lesClients;
    private LayoutInflater inflater;
    private Client client;
    private int id1;
    private final Controle controle;

    public ClientSpinnerAdapter(Context context, Controle controle, ArrayList<Client> lesClients) {
        this.lesClients = lesClients;
        this.context = context;
        //il va servir à formater la liste pour pouvoir recevoir les prochains éléments
        this.inflater = LayoutInflater.from(context);
        this.controle = controle;
    }

    @Override
    public int getCount() {
        return lesClients.size();
    }

    @Override
    public Object getItem(int position) {
        return lesClients.get(position);
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
            convertView = inflater.inflate(R.layout.layout_client_spinner_activity, parent, false);
        }
        TextView txtEts = convertView.findViewById(R.id.txtEtablissement);
        TextView txtNom = convertView.findViewById(R.id.txtNom);
        client = (Client) getItem(position);
        if (client != null) {

            txtEts.setText(client.getEtablissement());


            Log.d("ID :","******************************"+id1);
        }
        return convertView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
