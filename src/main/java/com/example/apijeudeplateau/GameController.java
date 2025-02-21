package com.example.apijeudeplateau;

import fr.le_campus_numerique.square_games.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/games")
    public GameDto createGame(
            @RequestBody GameCreationRequest request,
            @RequestHeader("X-UserId") UUID userId) {
        return gameService.createGame(request);
    }

    @GetMapping("games")
    public Collection<GameDto> getAllGames(@RequestParam(required = false) String status) {
        boolean is_status_valid = Arrays.stream(GameStatus.values()).distinct()
                .filter(s->s.name().equalsIgnoreCase(status))
                .count()>0;
        if(is_status_valid) {
            return gameService.findGamesByStatus(GameStatus.valueOf(status.toUpperCase()));
        }
        return gameService.findGamesByStatus(GameStatus.ONGOING);
    }

    @GetMapping("/games/{gameId}")
    public GameDto getGame(@PathVariable UUID gameId) {
        return gameService.getGameById(gameId);
    }

    @GetMapping("/games/{gameId}/moves")
    public Collection<CellPosition> getAllowedMoves(
            @PathVariable UUID gameId,
            @RequestParam(required = false) Boolean avaible){
        if(avaible != null && avaible) {
            return gameService.getAllowedMoves(gameId);
        }
        return new ArrayList<>();
    }

    @PostMapping("/games/{gameId}/moves")
    public void moveTo(@RequestBody CellPosition request, @PathVariable UUID gameId) {
        gameService.moveTo(gameId, request);
    }
}
