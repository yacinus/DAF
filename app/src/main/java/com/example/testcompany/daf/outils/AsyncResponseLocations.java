package com.example.testcompany.daf.outils;

import com.example.testcompany.daf.modele.Localisation;

import java.util.ArrayList;

public interface AsyncResponseLocations {
    void onCallback(ArrayList<Localisation> localisations);
}
