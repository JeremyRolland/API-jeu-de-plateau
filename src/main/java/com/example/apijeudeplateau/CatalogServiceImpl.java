package com.example.apijeudeplateau;

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
