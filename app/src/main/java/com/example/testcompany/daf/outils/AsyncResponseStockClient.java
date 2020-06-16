package com.example.testcompany.daf.outils;

import com.example.testcompany.daf.modele.StockClient;

import java.util.ArrayList;

public interface AsyncResponseStockClient {
    void onCallback(ArrayList<StockClient> stockClients);
}
