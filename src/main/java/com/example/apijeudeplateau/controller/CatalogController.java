package com.example.apijeudeplateau.controller;

import com.example.apijeudeplateau.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/catalog")
    public CatalogDto getCatalog() {
        return new CatalogDto(catalogService.findAll());
    }
}

