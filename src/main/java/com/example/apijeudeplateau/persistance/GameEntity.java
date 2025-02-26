package com.example.apijeudeplateau.persistance;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class GameEntity {
    @Id
    public UUID id;
    public @NotNull String factoryId;
    public @Positive int boardSize;
    public @NotNull @ElementCollection List<UUID> playerIds;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    public List<GameTokenEntity> tokens;

    public GameEntity(@NotNull UUID id, @NotBlank String factoryId, @Min(2L) int boardSize,
                      @NotEmpty List<UUID> playerIds, List<GameTokenEntity> tokens) {
        this.id = id;
        this.factoryId = factoryId;
        this.boardSize = boardSize;
        this.playerIds = playerIds;
        this.tokens = tokens;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public List<UUID> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<UUID> playerIds) {
        this.playerIds = playerIds;
    }

    public List<GameTokenEntity> getTokens() {
        return tokens;
    }

    public void setTokens(List<GameTokenEntity> tokens) {
        this.tokens = tokens;
    }

    public GameEntity() {

    }
}
