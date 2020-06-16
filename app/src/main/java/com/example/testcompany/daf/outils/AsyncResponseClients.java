package com.example.testcompany.daf.outils;

import com.example.testcompany.daf.modele.Client;

import java.util.ArrayList;

public interface AsyncResponseClients {
    void onCallback(ArrayList<Client> clients);
}
