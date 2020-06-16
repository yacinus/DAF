package com.example.testcompany.daf.outils;

import com.example.testcompany.daf.modele.Vente;

import java.util.ArrayList;

public interface AsyncResponseVentes {
    void onCallback(ArrayList<Vente> ventes);
}
