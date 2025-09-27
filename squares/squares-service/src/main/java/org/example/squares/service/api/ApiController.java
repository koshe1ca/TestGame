package org.example.squares.service.api;

import org.example.squares.core.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @PostMapping("/state")
    public Dto.StateResponse state(@RequestBody Dto.GameStateDto dto) {
        Dto.StateResponse r = new Dto.StateResponse();
        try {
            GameState s = ApiMapper.fromDto(dto);
            r.status = s.status.name();
            if (s.status == GameStatus.WIN_W) r.winner = "W";
            if (s.status == GameStatus.WIN_B) r.winner = "B";
            if (s.winningSquare != null) r.winningSquare = toCells(s.winningSquare);
        } catch (Exception e) {
            r.valid = false; r.message = "Ошибка: " + e.getMessage();
        }
        return r;
    }

    @PostMapping("/move")
    public Dto.MoveResponse move(@RequestBody Dto.GameStateDto dto) {
        Dto.MoveResponse r = new Dto.MoveResponse();
        try {
            GameState s = ApiMapper.fromDto(dto);
            if (s.status != GameStatus.ONGOING) {
                r.status = s.status.name(); return r;
            }
            AI ai = new AI(1234);
            var mv = ai.chooseMove(s, s.turn);
            if (mv.isEmpty()) { r.status = "DRAW"; return r; }
            var m = mv.get();
            Rules.applyMove(s, m.p(), m.color());
            r.move = new Dto.Cell(); r.move.x = m.p().x; r.move.y = m.p().y;
            r.color = m.color().name();
            r.status = s.status.name();
            if (s.status == GameStatus.WIN_W) r.winner = "W";
            if (s.status == GameStatus.WIN_B) r.winner = "B";
            if (s.winningSquare != null) r.winningSquare = toCells(s.winningSquare);
        } catch (Exception e) {
            r.valid = false; r.message = "Ошибка: " + e.getMessage();
        }
        return r;
    }

    private static List<Dto.Cell> toCells(List<Point> pts) {
        List<Dto.Cell> res = new ArrayList<>();
        for (Point p : pts) {
            Dto.Cell c = new Dto.Cell(); c.x = p.x; c.y = p.y; res.add(c);
        }
        return res;
    }
}
