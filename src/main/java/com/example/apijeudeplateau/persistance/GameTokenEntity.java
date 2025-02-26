package com.example.apijeudeplateau.persistance;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

@Entity
public class GameTokenEntity {
    @Id
    public UUID id;
    public @NotNull UUID ownerId;
    public @NotNull String name;
    public boolean removed;
    public @Nullable Integer x;
    public @Nullable Integer y;

    public GameTokenEntity() {

    }

    public GameTokenEntity(UUID id, @NotNull Optional<UUID> ownerId, @NotBlank String name, boolean removed) {
        this.id = id;
        this.ownerId = ownerId.orElse(null);
        this.name = name;
        this.removed = removed;
    }

    public GameTokenEntity(UUID id, @NotNull Optional<UUID> ownerId, @NotBlank String name, boolean removed, int x, int y) {
        this.id = id;
        this.ownerId = ownerId.orElse(null);
        this.name = name;
        this.removed = removed;
        this.x = x;
        this.y = y;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    @Nullable
    public Integer getX() {
        return x;
    }

    public void setX(@Nullable Integer x) {
        this.x = x;
    }

    @Nullable
    public Integer getY() {
        return y;
    }

    public void setY(@Nullable Integer y) {
        this.y = y;
    }
}
