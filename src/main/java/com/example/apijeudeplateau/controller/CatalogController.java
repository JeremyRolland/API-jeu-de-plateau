package com.example.apijeudeplateau.controller;

import com.example.apijeudeplateau.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/catalog")
    public List<String> getGames(@RequestHeader("Accept-Language") Locale language) {
        return catalogService.findAll(language);
    }
}

