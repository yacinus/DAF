package com.example.testcompany.daf.outils;

import com.example.testcompany.daf.modele.MyStock;

import java.util.ArrayList;

public interface AsyncResponseMyStock {
    void onCallBack(ArrayList<MyStock> myStock);
}
