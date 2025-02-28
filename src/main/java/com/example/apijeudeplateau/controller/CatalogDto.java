package com.example.apijeudeplateau.controller;

import com.example.apijeudeplateau.service.GamePlugin;

import java.util.ArrayList;

public record CatalogDto(ArrayList<GamePlugin> catalog) {
}

