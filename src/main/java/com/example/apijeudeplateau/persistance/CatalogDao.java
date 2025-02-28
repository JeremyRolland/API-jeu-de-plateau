package com.example.apijeudeplateau.persistance;

import com.example.apijeudeplateau.service.GamePlugin;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public interface CatalogDao {
    @NotNull ArrayList<GamePlugin> findAll();
}
