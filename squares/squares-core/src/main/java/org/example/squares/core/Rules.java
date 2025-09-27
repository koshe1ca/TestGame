package org.example.squares.core;

import java.util.*;

public final class Rules {
    private Rules() {}

    public static void validateMove(GameState s, Point p, Color c) {
        if (s.status != GameStatus.ONGOING) throw new IllegalStateException("Игра уже завершена.");
        if (s.turn != c) throw new IllegalStateException("Сейчас не ваш ход.");
        if (!s.inBounds(p)) throw new IllegalArgumentException("Координаты вне поля.");
        if (!s.isEmpty(p)) throw new IllegalArgumentException("Клетка уже занята.");
    }

    public static void applyMove(GameState s, Point p, Color c) {
        validateMove(s, p, c);
        s.of(c).add(p);

        if (isSquareCompletedBy(s, p, c)) {
            s.status = GameStatus.winOf(c);
            return;
        }
        if (s.full()) {
            s.status = GameStatus.DRAW;
            return;
        }
        s.turn = s.turn.opponent();
    }

    public static boolean isSquareCompletedBy(GameState s, Point p, Color c) {
        final Set<Point> mine = s.of(c);

        for (Point b : mine) {
            if (b.equals(p)) continue;

            // 1) вертикальный отрезок (x равны) -> сдвиг по X на |dy|
            if (b.x == p.x && b.y != p.y) {
                int dy = b.y - p.y;
                int L = Math.abs(dy);

                // вправо
                Point d1 = new Point(p.x + L, p.y);
                Point c1 = new Point(b.x + L, b.y);
                if (mine.contains(d1) && mine.contains(c1)) {
                    s.winningSquare = Arrays.asList(p, b, c1, d1);
                    return true;
                }
                // влево
                Point d2 = new Point(p.x - L, p.y);
                Point c2 = new Point(b.x - L, b.y);
                if (mine.contains(d2) && mine.contains(c2)) {
                    s.winningSquare = Arrays.asList(p, b, c2, d2);
                    return true;
                }
            }

            // 2) горизонтальный отрезок (y равны) -> сдвиг по Y на |dx|
            if (b.y == p.y && b.x != p.x) {
                int dx = b.x - p.x;
                int L = Math.abs(dx);

                // вниз
                Point d1 = new Point(p.x, p.y + L);
                Point c1 = new Point(b.x, b.y + L);
                if (mine.contains(d1) && mine.contains(c1)) {
                    s.winningSquare = Arrays.asList(p, b, c1, d1);
                    return true;
                }
                // вверх
                Point d2 = new Point(p.x, p.y - L);
                Point c2 = new Point(b.x, b.y - L);
                if (mine.contains(d2) && mine.contains(c2)) {
                    s.winningSquare = Arrays.asList(p, b, c2, d2);
                    return true;
                }
            }
        }
        return false;
    }
}
