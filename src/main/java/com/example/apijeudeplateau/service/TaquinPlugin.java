package com.example.apijeudeplateau.service;

import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.taquin.TaquinGameFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;

@Component
public class TaquinPlugin implements GamePlugin {


    private final MessageSource messageSource;
    @Value("${game.taquin.default-player-count}")
    private int defaultPlayerCount=1;
    @Value("${game.taquin.default-board-size}")
    private int defaultBoardSize=3;

    public TaquinPlugin(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Game createGame(OptionalInt boardSize, Set<UUID> playerIds) {
        return new TaquinGameFactory().createGame(defaultBoardSize, playerIds);
    }

    @Override
    public String getName(Locale locale) {
        return messageSource.getMessage("taquin.name", null, locale);
    }
}
