package com.example.apijeudeplateau.controller;

import com.example.apijeudeplateau.service.GameService;
import fr.le_campus_numerique.square_games.engine.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.*;

@RestController
@Transactional
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/games")
    public GameDto createGame(
            @RequestBody GameCreationRequest request,
            @RequestHeader("X-UserId") UUID userId) {
        return gameService.createGame(request, userId);
    }

    @GetMapping("/games")
    public Collection<GameDto> getAllGames(
            @RequestParam(required = false) String status,
            @RequestHeader("X-UserId") UUID userId) {
        boolean is_status_valid = Arrays.stream(GameStatus.values()).distinct()
                .filter(s->s.name().equalsIgnoreCase(status))
                .count()>0;
        if(is_status_valid) {
            return gameService.findGamesByStatus(GameStatus.valueOf(status.toUpperCase()), userId);
        }
        return null;
    }

    @GetMapping("/games/{gameId}")
    public GameDto getGame(@PathVariable UUID gameId,
                           @RequestHeader("X-UserId") UUID userId) {
        return gameService.getGameById(gameId, userId);
    }

    @GetMapping("/games/{gameId}/moves")
    public Collection<CellPosition> getAllowedMoves(
            @PathVariable UUID gameId,
            @RequestParam(required = false) Boolean avaible,
            @RequestHeader("X-UserId") UUID userId){
        if(avaible != null && avaible) {
            return gameService.getAllowedMoves(gameId, userId);
        }
        return new ArrayList<>();
    }

    @PutMapping("/games/{gameId}/moves")
    public void moveTo(@RequestBody CellPosition request,
                       @PathVariable UUID gameId,
                       @RequestHeader("X-UserId") UUID userId) {
        gameService.moveTo(gameId, request, userId);
    }

    private final RestClient restClient= RestClient.create();

    @GetMapping("/users/{userId}")
    public UserDto getUsers(@RequestHeader("X-UserId") UUID userId) {
        return  restClient.get()
                .uri("http://localhost:8081/users/{userId}", userId)
                .retrieve()
                .body(UserDto.class);
    }

}
