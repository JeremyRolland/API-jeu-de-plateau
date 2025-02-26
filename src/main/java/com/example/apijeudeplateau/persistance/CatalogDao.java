package com.example.apijeudeplateau.persistance;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public interface CatalogDao {
    @NotNull
    ArrayList<String> findAll();
}
