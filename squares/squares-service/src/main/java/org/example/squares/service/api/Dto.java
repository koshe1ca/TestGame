package org.example.squares.service.api;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.squares.core.Color;

import java.util.List;

public final class Dto {
    private Dto() {}

    @Schema(description = "Клетка на поле")
    public static class Cell {
        @Schema(description = "X-координата (0..N-1)", example = "3")
        public int x;
        @Schema(description = "Y-координата (0..N-1)", example = "5")
        public int y;
    }

    @Schema(description = "Снимок состояния игры")
    public static class GameStateDto {
        @Schema(description = "Размер поля (N ≥ 3)", example = "8")
        public int n;

        @Schema(description = "Чей ход сейчас", allowableValues = {"W","B"}, example = "W")
        public Color turn;

        @Schema(description = "Координаты белых фишек")
        public List<Cell> white;

        @Schema(description = "Координаты чёрных фишек")
        public List<Cell> black;
    }

    @Schema(description = "Ответ со статусом игры")
    public static class StateResponse {
        @Schema(description = "Статус", allowableValues = {"ONGOING","WIN_W","WIN_B","DRAW"})
        public String status;

        @Schema(description = "Победитель (когда есть)", allowableValues = {"W","B"})
        public String winner;

        @Schema(description = "Вершины выигрышного квадрата (если есть)")
        public List<Cell> winningSquare;

        @Schema(description = "Флаг валидности запроса")
        public boolean valid = true;

        @Schema(description = "Сообщение об ошибке, если valid=false")
        public String message;
    }

    @Schema(description = "Ответ на запрос хода ИИ")
    public static class MoveResponse extends StateResponse {
        @Schema(description = "Сделанный ход (если был)")
        public Cell move;

        @Schema(description = "Цвет, который сходил", allowableValues = {"W","B"})
        public String color;
    }
}
