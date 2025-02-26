package com.example.apijeudeplateau.service;

import com.example.apijeudeplateau.controller.GameCreationRequest;
import com.example.apijeudeplateau.persistance.GameDao;
import com.example.apijeudeplateau.controller.GameDto;
import com.example.apijeudeplateau.persistance.GameEntity;
import com.example.apijeudeplateau.persistance.GameTokenEntity;
import fr.le_campus_numerique.square_games.engine.*;
import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGameFactory;
import fr.le_campus_numerique.square_games.engine.taquin.TaquinGameFactory;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    @Qualifier("jpaGameDao")
    @Autowired
    private GameDao gameDao;

    @Override
    public GameDto createGame(GameCreationRequest request, UUID userId) {
        Set<UUID> playersIDs = new HashSet<>();
        playersIDs.add(userId);
        GameFactory gameFactory = switch (request.gameType()) {
            case "tictactoe" -> {
                playersIDs.add(UUID.randomUUID());
                yield new TicTacToeGameFactory();
            }
            case "taquin" -> new TaquinGameFactory();
            case "connectfour" -> {
                playersIDs.add(UUID.randomUUID());
                yield new ConnectFourGameFactory();
            }
            default -> throw new IllegalArgumentException("Unknown game type: " + request.gameType());
        };
        Game game = gameDao.upsert(gameFactory.createGame(request.boardSize(), playersIDs));
        return new GameDto(game.getId().toString(), game.getFactoryId(), game.getPlayerIds(), game.getCurrentPlayerId());
    }

    @Override
    public GameDto getGameById(UUID gameId, UUID userId) {
        Optional<Game> gameOptional = gameDao.findById(gameId);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            if (game.getPlayerIds().contains(userId)) {
                return new GameDto(gameId.toString(), game.getFactoryId(), game.getPlayerIds(), game.getCurrentPlayerId());
            }
        }
        return null;
    }

    @Override
    public void moveTo(UUID gameId, CellPosition request, UUID userId) {
        // Trouver le jeu par ID
        Optional<Game> optionalGame = gameDao.findById(gameId);
        if (optionalGame.isEmpty()) {
            return;
        }
        Game game = optionalGame.get();
        Optional<Token> optionalTokenToMove = game.getRemainingTokens()
                .stream()
                .filter(token -> Objects.equals(token.getOwnerId().orElse(null), userId))
                .findFirst();
        if (optionalTokenToMove.isEmpty()) {
            return;
        }
        Token tokenToMove = optionalTokenToMove.get();
        try {
            tokenToMove.moveTo(request);
            gameDao.upsert(game);
        } catch (InvalidPositionException e) {
            throw new RuntimeException("Invalid position: " + request, e);
        }
    }

    @Override
    public Set<CellPosition> getAllowedMoves(UUID gameId, UUID userId) {
        return gameDao.findById(gameId)
                .filter(game -> game.getPlayerIds().contains(userId))
                .map(game -> game.getRemainingTokens().stream()
                        .map(Token::getAllowedMoves)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet()))
                .orElse(null);
    }

    @Override
    public List<GameDto> findGamesByStatus(GameStatus status, UUID userId) {
        return gameDao.findAll()
                .filter(game -> status.equals(game.getStatus()) && game.getPlayerIds().contains(userId))
                .map(game -> new GameDto(game.getId().toString(), game.getFactoryId(), game.getPlayerIds(), game.getCurrentPlayerId()))
                .collect(Collectors.toList());
    }

}
