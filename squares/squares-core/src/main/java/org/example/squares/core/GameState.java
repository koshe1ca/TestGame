package org.example.squares.core;

import java.util.*;

public class GameState {
    public final int n;
    public Color turn;
    public final Set<Point> white = new HashSet<>();
    public final Set<Point> black = new HashSet<>();
    public GameStatus status = GameStatus.ONGOING;
    public List<Point> winningSquare = null;

    public GameState(int n, Color turn) {
        if (n < 2) throw new IllegalArgumentException("Размер поля N должен быть >= 2.");
        this.n = n;
        this.turn = turn;
    }

    public boolean inBounds(Point p) { return p.x >= 0 && p.y >= 0 && p.x < n && p.y < n; }
    public boolean isEmpty(Point p) { return !white.contains(p) && !black.contains(p); }
    public Set<Point> of(Color c) { return c == Color.W ? white : black; }
    public boolean full() { return white.size() + black.size() == n * n; }

    public GameState copy() {
        GameState g = new GameState(n, turn);
        g.white.addAll(white);
        g.black.addAll(black);
        g.status = status;
        if (winningSquare != null) g.winningSquare = new ArrayList<>(winningSquare);
        return g;
    }
}
