package com.example.apijeudeplateau;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public interface CatalogDao {
    @NotNull
    ArrayList<String> findAll();
}
