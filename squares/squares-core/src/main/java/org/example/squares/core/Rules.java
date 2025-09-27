package org.example.squares.core;

import java.util.*;

public final class Rules {
    private Rules() {}

    public static void validateMove(GameState s, Point p, Color c) {
        if (s.status != GameStatus.ONGOING) throw new IllegalStateException("Игра заверешена");
        if (s.turn != c) throw new IllegalStateException("Не товая очередь.");
        if (!s.inBounds(p)) throw new IllegalArgumentException("Координаты вне поля.");
        if (!s.isEmpty(p)) throw new IllegalArgumentException("Клетка занята");
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
        Set<Point> mine = s.of(c);
        for (Point b : mine) {
            if (b.equals(p)) continue;
            Point v = new Point(b.x - p.x, b.y - p.y); // вектор стороны
            Point vp = v.perp();                       // перпендикуляр

            // Вариант 1: +v⊥
            Point d1 = new Point(p.x + vp.x, p.y + vp.y);
            Point c1 = new Point(b.x + vp.x, b.y + vp.y);
            if (mine.contains(d1) && mine.contains(c1)) {
                s.winningSquare = Arrays.asList(p, b, c1, d1);
                return true;
            }

            // Вариант 2: -v⊥
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
