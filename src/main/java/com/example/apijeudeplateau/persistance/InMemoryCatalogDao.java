package com.example.apijeudeplateau.persistance;

import com.example.apijeudeplateau.service.ConnectFourPlugin;
import com.example.apijeudeplateau.service.GamePlugin;
import com.example.apijeudeplateau.service.TaquinPlugin;
import com.example.apijeudeplateau.service.TicTacToePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class InMemoryCatalogDao implements CatalogDao {

    @Autowired
    private MessageSource messageSource;

    @Override
    public ArrayList<GamePlugin> findAll() {
        ArrayList<GamePlugin> catalog = new ArrayList<>();
        catalog.add(new TicTacToePlugin(messageSource));
        catalog.add(new TaquinPlugin(messageSource));
        catalog.add(new ConnectFourPlugin(messageSource));
        return catalog;
    }
}
