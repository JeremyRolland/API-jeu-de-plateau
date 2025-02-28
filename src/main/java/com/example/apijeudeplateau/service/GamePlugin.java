package com.example.apijeudeplateau.service;

import fr.le_campus_numerique.square_games.engine.Game;
import jakarta.validation.constraints.NotBlank;

import java.util.Locale;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;

public interface GamePlugin {
    @NotBlank Game createGame(OptionalInt boardSize, Set<UUID> playerIds);
    @NotBlank String getName(Locale locale);

}
