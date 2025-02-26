package com.example.apijeudeplateau.persistance;

import fr.le_campus_numerique.square_games.engine.Game;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface GameDao {
    @NotNull
    Stream<Game> findAll();
    Optional<Game> findById(@NotNull UUID id);
    @NotNull Game upsert(@NotNull Game game);
    void delete(@NotNull UUID id);
}
