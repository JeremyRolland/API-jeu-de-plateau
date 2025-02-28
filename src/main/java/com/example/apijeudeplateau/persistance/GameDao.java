package com.example.apijeudeplateau.persistance;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface GameDao {
    @NotNull
    Stream<GameEntity> findAll();
    Optional<GameEntity> findById(@NotNull UUID id);
    void upsert(Optional<GameEntity> gameEntity);
    void delete(@NotNull UUID id);
}
