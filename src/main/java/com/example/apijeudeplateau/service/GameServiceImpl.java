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
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    @Qualifier("jpaGameDao")
    private GameDao gameDao;

    @Autowired
    private MessageSource messageSource;

    @Override
    public GameDto createGame(GameCreationRequest request, UUID userId) {
        Set<UUID> playersIDs = new HashSet<>();
        playersIDs.add(userId);
        playersIDs.add(UUID.randomUUID());
        GamePlugin gamePlugin = switch (request.gameType()) {
            case "tictactoe" -> {
                playersIDs.add(UUID.randomUUID());
                yield new TicTacToePlugin(messageSource);
            }
            case "15 puzzle" -> new TaquinPlugin(messageSource);
            case "connectFour" -> {
                playersIDs.add(UUID.randomUUID());
                yield new ConnectFourPlugin(messageSource);
            }
            default -> throw new IllegalArgumentException("Unknown game type: " + request.gameType());
        };
        Game game = gamePlugin.createGame(request.boardSize(), playersIDs);
        gameDao.upsert(Optional.of(convertGameToGameEntity(game)));
        return convertGameToGameDto(game);
    }

    @Override
    public GameDto getGameById(UUID gameId, UUID userId) {
        return gameDao.findById(gameId)
                .map(this::convertGameEntityToGame)
                .filter(game -> game.getPlayerIds().contains(userId))
                .map(this::convertGameToGameDto)
                .orElse(null);
    }

    @Override
    public Game getGameObjectById(UUID gameId, UUID userId) {
        Optional<GameEntity> optionalGameEntity = gameDao.findById(gameId);
        if (optionalGameEntity.isPresent()) {
            Game game = this.convertGameEntityToGame(optionalGameEntity.get());
            assert game != null;
            return game;
        }
        return null;
    }

    @Override
    public void makeMove(UUID gameId, CellPosition request, UUID userId) {
        gameDao.findById(gameId)
                .map(this::convertGameEntityToGame)
                .ifPresent(game -> game.getRemainingTokens()
                        .stream()
                        .filter(token -> Objects.equals(token.getOwnerId().orElse(null), userId))
                        .findFirst()
                        .ifPresent(token -> {
                            try {
                                token.moveTo(request);
                                gameDao.upsert(Optional.of(convertGameToGameEntity(game)));
                            } catch (InvalidPositionException e) {
                                throw new RuntimeException("Invalid position: " + request, e);
                            }
                        }));
    }

    @Override
    public Set<CellPosition> getAllowedMoves(UUID gameId, UUID userId) {
        return gameDao.findById(gameId)
                .filter(gameEntity -> gameEntity.getPlayerIds().contains(userId))
                .map(gameEntity -> {
                    if ("15 puzzle".equals(gameEntity.getFactoryId())) {
                        return this.allowedMovesTaquin(gameEntity);
                    } else {
                        return this.allowedMovesGames(gameEntity);
                    }
                })
                .orElse(Collections.emptySet());
    }

    @Override
    public List<GameDto> findGamesByStatus(GameStatus status, UUID userId) {
        return gameDao.findAll()
                .map(this::convertGameEntityToGame).filter(Objects::nonNull)
                .filter(game -> status.equals(game.getStatus()) && game.getPlayerIds().contains(userId))
                .map(this::convertGameToGameDto)
                .collect(Collectors.toList());
    }

    private Set<CellPosition> allowedMovesGames(GameEntity gameEntity) {
        return Objects.requireNonNull(this.convertGameEntityToGame(gameEntity))
                .getRemainingTokens()
                .stream()
                .map(Token::getAllowedMoves)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<CellPosition> allowedMovesTaquin(GameEntity gameEntity) {
        return Objects.requireNonNull(this.convertGameEntityToGame(gameEntity))
                .getBoard().values()
                .stream()
                .filter(token -> !token.getAllowedMoves().isEmpty())
                .map(Token::getPosition)
                .collect(Collectors.toSet());
    }

    private GameDto convertGameToGameDto(Game game) {
        return new GameDto(game.getId().toString(), game.getFactoryId(),
                game.getPlayerIds(), game.getCurrentPlayerId(), game.getStatus());
    }

    private GameEntity convertGameToGameEntity(Game game) {
        return new GameEntity(game.getId(), game.getFactoryId(), game.getBoardSize(),
                new ArrayList<>(game.getPlayerIds()), convertTokenToGameTokenEntity(game.getBoard()));
    }

    private Game convertGameEntityToGame(GameEntity gameEntity) {
        GameFactory gameFactory = switch (gameEntity.factoryId) {
            case "tictactoe" -> new TicTacToeGameFactory();
            case "15 puzzle" -> new TaquinGameFactory();
            case "connect4" -> new ConnectFourGameFactory();
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
