package com.example.testcompany.daf.modele;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AppCompatActivity;

public class NetworkConnectivity extends AppCompatActivity {

    private final Context context;

    public NetworkConnectivity(Context contexte){
        this.context = contexte;
    }

    public boolean isConnected(){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if(connectivity != null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info != null){
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }
}
