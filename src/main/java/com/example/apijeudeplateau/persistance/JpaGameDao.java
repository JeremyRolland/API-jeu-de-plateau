package com.example.apijeudeplateau.persistance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Stream;

@Repository
public class JpaGameDao implements GameDao {

    @Autowired
    private GameEntityRepository gameEntityRepository;

    @Override
    public Stream<GameEntity> findAll() {
        return gameEntityRepository.findAll().stream();
    }

    @Override
    public Optional<GameEntity> findById(UUID id) {
        return gameEntityRepository.findById(id);
    }

    @Override
    public void upsert(Optional<GameEntity> gameEntity) {
        gameEntity.ifPresent(gameEntityRepository::save);
    }

    @Override
    public void delete(UUID id) {
        gameEntityRepository.deleteById(id);
    }




}
