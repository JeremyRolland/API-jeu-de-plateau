package com.example.apijeudeplateau.service;

import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;

@Component
public class TicTacToePlugin implements GamePlugin {


    private final MessageSource messageSource;
    @Value("${game.tictactoe.default-board-size}")
    private int defaultBoardSize;

    public TicTacToePlugin(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Game createGame(OptionalInt boardSize, Set<UUID> playerIds) {
        return new TicTacToeGameFactory().createGame(defaultBoardSize, playerIds);
    }

    @Override
    public String getName(Locale locale) {
        return messageSource.getMessage("tictactoe.name", null, locale);
    }
}
