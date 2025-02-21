package com.example.apijeudeplateau;

import fr.le_campus_numerique.square_games.engine.*;
import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGameFactory;
import fr.le_campus_numerique.square_games.engine.taquin.TaquinGameFactory;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameDao gameDao;

    @Override
    public GameDto createGame(GameCreationRequest request) {
        GameFactory gameFactory = switch (request.gameType()) {
            case "TicTacToe" -> new TicTacToeGameFactory();
            case "Taquin" -> new TaquinGameFactory();
            case "ConnectFour" -> new ConnectFourGameFactory();
            default -> throw new IllegalArgumentException("Unknown game type: " + request.gameType());
        };
        Game game = gameDao.upsert(gameFactory.createGame(request.playerCount(), request.boardSize()));
        gameDao.upsert(game);
        return new GameDto(game.getId().toString(), game.getFactoryId());
    }

    @Override
    public GameDto getGameById(UUID id) {
        Optional<Game> game = gameDao.findById(id);
        return new GameDto(game.get().getId().toString(), game.get().getFactoryId());
    }

    @Override
    public void moveTo(UUID id, CellPosition request) {
        // Trouver le jeu par ID
        Game game = gameDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Game not found"));


        // Récupérer les tokens restants pour le joueur courant
        Token tokenToMove = game.getRemainingTokens()
                .stream()
                .filter(token -> token.getOwnerId().orElse(null).equals(game.getCurrentPlayerId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Token not found for the current player"));

        // Tenter de déplacer le token
        try {
            tokenToMove.moveTo(request);
            gameDao.upsert(game);
        } catch (InvalidPositionException e) {
            throw new RuntimeException("Invalid position: " + request, e);
        }
    }

    @Override
    public Set<CellPosition> getAllowedMoves(UUID id) {
       return gameDao.findById(id).get()
                .getRemainingTokens()
                .stream()
                .map(Token::getAllowedMoves)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public List<GameDto> findGamesByStatus(GameStatus status) {
        return gameDao.findAll()
                .stream()
                .filter(game -> status.equals(game.getStatus()))
                .map(game -> new GameDto(game.getId().toString(), game.getFactoryId()))
                .collect(Collectors.toList());
    }

}
