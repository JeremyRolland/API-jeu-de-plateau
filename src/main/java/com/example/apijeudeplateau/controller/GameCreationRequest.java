package com.example.apijeudeplateau.controller;

import jakarta.validation.constraints.NotNull;

public record GameCreationRequest(
        @NotNull String gameType,
        @NotNull Integer playerCount,
        @NotNull Integer boardSize
) {}
