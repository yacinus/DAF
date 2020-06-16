package com.example.testcompany.daf.outils;

import com.example.testcompany.daf.modele.Produit;

import java.util.ArrayList;

public interface AsyncResponseProducts {
    void onCallback(ArrayList<Produit> produits);
}
