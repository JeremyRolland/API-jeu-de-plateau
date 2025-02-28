package com.example.apijeudeplateau.controller;

import jakarta.validation.constraints.NotNull;

import java.util.OptionalInt;

public record GameCreationRequest(
        @NotNull String gameType,
        @NotNull OptionalInt boardSize
) {}
