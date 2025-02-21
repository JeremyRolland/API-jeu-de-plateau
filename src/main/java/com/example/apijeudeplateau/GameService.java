package com.example.apijeudeplateau;

import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.GameStatus;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface GameService {
    GameDto createGame(GameCreationRequest request);
    GameDto getGameById(UUID id);
    void moveTo(UUID id, CellPosition request);
    Set<CellPosition> getAllowedMoves(UUID id);
    List<GameDto> findGamesByStatus(GameStatus status);
}
