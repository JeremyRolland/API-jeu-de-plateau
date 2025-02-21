package com.example.apijeudeplateau;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class InMemoryCatalogDao implements CatalogDao {

    @Override
    public ArrayList<String> findAll() {
        ArrayList<String> catalog = new ArrayList<>();
        catalog.add("TicTacToe");
        catalog.add("Taquin");
        catalog.add("ConnectFour");
        return catalog;
    }
}
