package com.example.apijeudeplateau.service;

import com.example.apijeudeplateau.persistance.CatalogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Locale;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogDao catalogDao;

    @Override
    public ArrayList<String> findAll(Locale language) {
        ArrayList<String> list = new ArrayList<>();
        for(GamePlugin element : catalogDao.findAll()) {
            list.add(element.getName(language));
        }
        return list;
    }
}
