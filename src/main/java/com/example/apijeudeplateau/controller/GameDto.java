package com.example.apijeudeplateau.controller;

import java.util.Set;
import java.util.UUID;

public record GameDto(String gameId, String gameType, Set<UUID> playersIds, UUID currentPlayerId) {
}
