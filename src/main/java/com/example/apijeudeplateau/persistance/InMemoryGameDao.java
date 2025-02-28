package com.example.apijeudeplateau.persistance;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Stream;

@Repository
public class InMemoryGameDao implements GameDao {

    private final List<GameEntity> games = new ArrayList<>();

    @Override
    public Stream<GameEntity> findAll() {
        return games.stream();
    }

    @Override
    public Optional<GameEntity> findById(UUID id) {
        return games.stream().filter(g -> g.getId().equals(id)).findFirst();
    }

    public void upsert(Optional<GameEntity> gameEntityOptional) {
        gameEntityOptional.ifPresent(gameEntity -> {
            games.removeIf(existingGame -> existingGame.getId().equals(gameEntity.id));
            games.add(gameEntity);
        });
    }

    @Override
    public void delete(UUID id) {
        games.removeIf(g -> g.getId().equals(id));
    }
}
