package org.example.squares.core;

public enum GameStatus {
    ONGOING, WIN_W, WIN_B, DRAW;

    public static GameStatus winOf(Color c) { return c == Color.W ? WIN_W : WIN_B; }
}
