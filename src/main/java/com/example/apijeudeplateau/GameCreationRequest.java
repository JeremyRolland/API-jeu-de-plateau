package com.example.apijeudeplateau;

import jakarta.validation.constraints.NotNull;

public record GameCreationRequest(
        @NotNull String gameType,
        @NotNull Integer playerCount,
        @NotNull Integer boardSize
) {}
