package com.example.testcompany.daf.outils;

import com.example.testcompany.daf.modele.Situation;

import java.util.ArrayList;

public interface AsyncResponseSituation {
    void onCallback(ArrayList<Situation> situations);
}
