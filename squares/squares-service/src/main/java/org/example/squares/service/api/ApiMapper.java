package org.example.squares.service.api;

import org.example.squares.core.*;

public final class ApiMapper {
    private ApiMapper() {}

    public static GameState fromDto(Dto.GameStateDto d) {
        if (d == null) throw new IllegalArgumentException("Пустое тело запроса.");
        if (d.n < 2) throw new IllegalArgumentException("Размер поля N должен быть >= 2.");

        GameState s = new GameState(d.n, d.turn);

        if (d.white != null) for (var c : d.white) s.white.add(new Point(c.x, c.y));
        if (d.black != null) for (var c : d.black) s.black.add(new Point(c.x, c.y));

        for (var p : s.white)
            if (Rules.isSquareCompletedBy(s, p, Color.W)) { s.status = GameStatus.WIN_W; return s; }
        for (var p : s.black)
            if (Rules.isSquareCompletedBy(s, p, Color.B)) { s.status = GameStatus.WIN_B; return s; }
        if (s.full()) s.status = GameStatus.DRAW;
        return s;
    }
}
