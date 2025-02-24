package com.example.apijeudeplateau;

import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.GameStatus;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface GameService {
    GameDto createGame(GameCreationRequest request, UUID userId);
    GameDto getGameById(UUID gameId, UUID userId);
    void moveTo(UUID gameId, CellPosition request, UUID userId);
    Set<CellPosition> getAllowedMoves(UUID gameId, UUID userId);
    List<GameDto> findGamesByStatus(GameStatus status, UUID userId);
}
