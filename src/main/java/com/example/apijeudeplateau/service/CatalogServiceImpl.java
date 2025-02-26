package com.example.apijeudeplateau.service;

import com.example.apijeudeplateau.persistance.CatalogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogDao catalogDao;

    @Override
    public ArrayList<String> findAll() {
         return catalogDao.findAll();
    }
}
