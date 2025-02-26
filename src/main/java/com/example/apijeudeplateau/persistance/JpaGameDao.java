package com.example.apijeudeplateau.persistance;

import fr.le_campus_numerique.square_games.engine.*;
import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGameFactory;
import fr.le_campus_numerique.square_games.engine.taquin.TaquinGameFactory;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class JpaGameDao implements GameDao {

    @Autowired
    private GameEntityRepository gameEntityRepository;

    @Override
    public Stream<Game> findAll() {
        List<GameEntity> gameEntities = gameEntityRepository.findAll();
        return gameEntities.stream().map(this::convertGameEntityToGame);
    }

    @Override
    public Optional<Game> findById(UUID id) {
        Optional<GameEntity> gameEntity = gameEntityRepository.findById(id);
        return gameEntity.map(this::convertGameEntityToGame);
    }

    @Override
    public Game upsert(Game game) {
        GameEntity gameEntity = new GameEntity(game.getId(), game.getFactoryId(), game.getBoardSize(),
                new ArrayList<>(game.getPlayerIds()), convertTokenToGameTokenEntity(game.getBoard()));
        gameEntityRepository.save(gameEntity);
        return convertGameEntityToGame(gameEntity);
    }

    @Override
    public void delete(UUID id) {
        gameEntityRepository.deleteById(id);
    }

    private Game convertGameEntityToGame(GameEntity gameEntity) {
        GameFactory gameFactory = switch (gameEntity.factoryId) {
            case "tictactoe" -> new TicTacToeGameFactory();
            case "taquin" -> new TaquinGameFactory();
            case "connectfour" -> new ConnectFourGameFactory();
            default -> throw new IllegalArgumentException("Unknown game type: " + gameEntity.factoryId);
        };
        Collection<TokenPosition<UUID>> removedTokens = Collections.emptyList();
        Collection<TokenPosition<UUID>> boardTokens = getPlayedTokens(gameEntity.getTokens());
        try{
            return gameFactory.createGameWithIds(gameEntity.id, gameEntity.boardSize, gameEntity.playerIds, boardTokens, removedTokens);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Fonction pour obtenir les tokens dont la position n'est pas nulle
    public static Collection<TokenPosition<UUID>> getPlayedTokens(Collection<GameTokenEntity> gameTokenEntities) {
        return gameTokenEntities.stream()
                .filter(entity -> entity.x != null && entity.y != null)
                .map(entity -> new TokenPosition<>(
                        entity.ownerId,
                        entity.name,
                        entity.x,
                        entity.y
                ))
                .collect(Collectors.toList());
    }

    private List<GameTokenEntity> convertTokenToGameTokenEntity(Map<CellPosition, Token> tokens) {
        return tokens.entrySet().stream().map(entry -> {
            CellPosition position = entry.getKey();
            Token token = entry.getValue();
            return new GameTokenEntity(UUID.randomUUID(), token.getOwnerId(), token.getName(), false, position.x(), position.y());
        }).collect(Collectors.toList());
    }


}
