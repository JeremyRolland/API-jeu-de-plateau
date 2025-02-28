package com.example.apijeudeplateau.controller;

import fr.le_campus_numerique.square_games.engine.GameStatus;

import java.util.Set;
import java.util.UUID;

public record GameDto(String gameId, String gameType, Set<UUID> playersIds, UUID currentPlayerId, GameStatus status) {
}
