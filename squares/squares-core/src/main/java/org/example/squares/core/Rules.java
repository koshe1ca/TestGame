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

            // Сторона v = b - p
            final Point v = new Point(b.x - p.x, b.y - p.y);
            if (v.x == 0 && v.y == 0) continue;

            // Перпендикуляр к v (той же длины)
            final Point vp = new Point(-v.y, v.x);

            // Проверка перпендикулярности и равенства длин (защита от прямоугольников)
            final int len2v = v.x * v.x + v.y * v.y;
            final int len2vp = vp.x * vp.x + vp.y * vp.y;
            final int dot = v.x * vp.x + v.y * vp.y;
            if (dot != 0 || len2v != len2vp) continue;

            // Вариант 1: смещение +vp (p -> d1, b -> c1)
            Point d1 = new Point(p.x + vp.x, p.y + vp.y);
            Point c1 = new Point(b.x + vp.x, b.y + vp.y);
            if (mine.contains(d1) && mine.contains(c1)) {
                s.winningSquare = Arrays.asList(p, b, c1, d1);
                return true;
            }

            // Вариант 2: смещение -vp (p -> d2, b -> c2)
            Point d2 = new Point(p.x - vp.x, p.y - vp.y);
            Point c2 = new Point(b.x - vp.x, b.y - vp.y);
            if (mine.contains(d2) && mine.contains(c2)) {
                s.winningSquare = Arrays.asList(p, b, c2, d2);
                return true;
            }
        }
        return false;
    }
}
