package org.example.squares.service.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.squares.core.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Squares", description = "Операции игры «Квадраты»")
public class ApiController {

    @Operation(
            summary = "Проверить состояние игры",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Dto.GameStateDto.class),
                            examples = @ExampleObject(value = "{\"n\":8,\"turn\":\"W\",\"white\":[],\"black\":[]}")
                    )
            )
    )
    @PostMapping("/state")
    public Dto.StateResponse state(@org.springframework.web.bind.annotation.RequestBody Dto.GameStateDto dto) {
        Dto.StateResponse r = new Dto.StateResponse();
        try {
            GameState s = ApiMapper.fromDto(dto);
            r.status = s.status.name();
            if (s.status == GameStatus.WIN_W) r.winner = "W";
            if (s.status == GameStatus.WIN_B) r.winner = "B";
            if (s.winningSquare != null) r.winningSquare = toCells(s.winningSquare);
        } catch (Exception e) {
            r.valid = false;
            r.message = "Ошибка: " + e.getMessage();
        }
        return r;
    }

    @Operation(
            summary = "Сделать ход компьютером",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Dto.GameStateDto.class),
                            examples = @ExampleObject(value = "{\"n\":8,\"turn\":\"B\",\"white\":[{\"x\":3,\"y\":3}],\"black\":[]}")
                    )
            )
    )
    @PostMapping("/move")
    public Dto.MoveResponse move(@org.springframework.web.bind.annotation.RequestBody Dto.GameStateDto dto) {
        Dto.MoveResponse r = new Dto.MoveResponse();
        try {
            GameState s = ApiMapper.fromDto(dto);

            // если игра уже завершена — вернём полный статус (winner/квадрат)
            if (s.status != GameStatus.ONGOING) {
                r.status = s.status.name();
                if (s.status == GameStatus.WIN_W) r.winner = "W";
                if (s.status == GameStatus.WIN_B) r.winner = "B";
                if (s.winningSquare != null) r.winningSquare = toCells(s.winningSquare);
                return r;
            }

            AI ai = new AI(1234);
            var mv = ai.chooseMove(s, s.turn);
            if (mv.isEmpty()) {
                r.status = s.full() ? GameStatus.DRAW.name() : GameStatus.ONGOING.name();
                return r;
            }
            var m = mv.get();
            Rules.applyMove(s, m.p(), m.color());

            r.move = new Dto.Cell(); r.move.x = m.p().x; r.move.y = m.p().y;
            r.color = m.color().name();
            r.status = s.status.name();
            if (s.status == GameStatus.WIN_W) r.winner = "W";
            if (s.status == GameStatus.WIN_B) r.winner = "B";
            if (s.winningSquare != null) r.winningSquare = toCells(s.winningSquare);
        } catch (Exception e) {
            r.valid = false;
            r.message = "Ошибка: " + e.getMessage();
        }
        return r;
    }

    private static List<Dto.Cell> toCells(List<Point> pts) {
        List<Dto.Cell> res = new ArrayList<>();
        for (Point p : pts) {
            Dto.Cell c = new Dto.Cell();
            c.x = p.x; c.y = p.y;
            res.add(c);
        }
        return res;
    }
}
