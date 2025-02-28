package com.example.apijeudeplateau.service;

import java.util.ArrayList;
import java.util.Locale;

public interface CatalogService {

    ArrayList<String> findAll(Locale language);
}
