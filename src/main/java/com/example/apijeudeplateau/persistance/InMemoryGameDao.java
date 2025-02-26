package com.example.apijeudeplateau.persistance;

import fr.le_campus_numerique.square_games.engine.Game;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Stream;

@Repository
public class InMemoryGameDao implements GameDao {

    private final List<Game> games = new ArrayList<>();

    @Override
    public Stream<Game> findAll() {
        return games.stream();
    }

    @Override
    public Optional<Game> findById(UUID id) {
        return games.stream().filter(g -> g.getId().equals(id)).findFirst();
    }

    public Game upsert(@NotNull Game game) {
        games.removeIf(existingGame -> existingGame.getId().equals(game.getId()));
        games.add(game);
        return game;
    }

    @Override
    public void delete(UUID id) {
        games.removeIf(g -> g.getId().equals(id));
    }
}
