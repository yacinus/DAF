package com.example.testcompany.daf.modele;

import android.content.Context;
import android.util.Log;

import com.example.testcompany.daf.controleur.Controle;

public class ThreadCheckVariablesFirestore implements Runnable {

    private final Controle controle;
    private boolean check = false;

    public ThreadCheckVariablesFirestore(){
        controle = Controle.getInstance(null);
    }

    @Override
    public void run() {
        for (int i = 0; i<5; i++){
            try {
                Thread.sleep(1000);
                if (controle.arrayMyStockIsFull() && controle.arraySituationIsFull() && controle.arrayStockClientIsFull() && controle.lesClientsIsFull()
                        && controle.lesPointsIsFull() && controle.lesProduitsIsFull() && controle.locationsIsFull()
                        && controle.usersIdIsFull() && controle.ventesIsFull()){
                    check = true;
                    controle.checkIsOk(check);
                    Thread.interrupted();

                }
                Log.d("3 : i = "+i, "********************************* ");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
