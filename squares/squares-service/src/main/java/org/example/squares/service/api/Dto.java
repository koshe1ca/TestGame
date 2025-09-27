package org.example.squares.service.api;

import org.example.squares.core.Color;
import java.util.List;

public final class Dto {
    private Dto() {}

    public static class Cell {
        public int x;
        public int y;
    }

    public static class GameStateDto {
        public int n;
        public Color turn;
        public List<Cell> white;
        public List<Cell> black;
    }

    public static class StateResponse {
        public String status;
        public String winner;
        public List<Cell> winningSquare;
        public boolean valid = true;
        public String message;
    }

    public static class MoveResponse extends StateResponse {
        public Cell move;
        public String color;
    }
}
