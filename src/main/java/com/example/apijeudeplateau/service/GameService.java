package com.example.apijeudeplateau.service;

import com.example.apijeudeplateau.controller.GameCreationRequest;
import com.example.apijeudeplateau.controller.GameDto;
import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.GameStatus;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public interface GameService {
    GameDto createGame(GameCreationRequest request, UUID userId);
    GameDto getGameById(UUID gameId, UUID userId);
    void makeMove(UUID gameId, CellPosition request, UUID userId);
    Set<CellPosition> getAllowedMoves(UUID gameId, UUID userId);
    List<GameDto> findGamesByStatus(GameStatus status, UUID userId);
    // Pour test
    Game getGameObjectById(UUID gameId, UUID userId);
}
