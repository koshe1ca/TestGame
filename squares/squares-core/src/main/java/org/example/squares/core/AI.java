package org.example.squares.core;

import java.util.*;

public final class AI {
    private final Random rnd;
    public AI(long seed) { this.rnd = new Random(seed); }

    public record Move(Point p, Color color) {}

    public Optional<Move> chooseMove(GameState s, Color me) {
        if (s.status != GameStatus.ONGOING) return Optional.empty();

        List<Point> empties = new ArrayList<>();
        for (int y = 0; y < s.n; y++)
            for (int x = 0; x < s.n; x++) {
                Point p = new Point(x,y);
                if (s.isEmpty(p)) empties.add(p);
            }
        if (empties.isEmpty()) return Optional.empty();

        for (Point p : empties) if (winsWith(s, p, me)) return Optional.of(new Move(p, me));

        Color opp = me.opponent();
        for (Point p : empties) if (winsWith(s, p, opp)) return Optional.of(new Move(p, me));

        Point best = null; int bestScore = Integer.MIN_VALUE;
        int cx = (s.n - 1) / 2, cy = (s.n - 1) / 2;
        for (Point p : empties) {
            int threats = countThreatsAfter(s, p, me);
            int center = -Math.abs(p.x - cx) - Math.abs(p.y - cy);
            int score = threats * 100 + center;
            if (score > bestScore || (score == bestScore && rnd.nextBoolean())) {
                bestScore = score; best = p;
            }
        }
        return Optional.of(new Move(best, me));
    }

    private boolean winsWith(GameState s, Point p, Color c) {
        if (!s.isEmpty(p)) return false;
        GameState t = s.copy();
        t.turn = c;
        try { Rules.applyMove(t, p, c); } catch (RuntimeException e) { return false; }
        return t.status == GameStatus.winOf(c);
    }

    private int countThreatsAfter(GameState s, Point p, Color c) {
        GameState t = s.copy();
        t.turn = c;
        try { Rules.applyMove(t, p, c); } catch (RuntimeException e) { return -1000; }
        Set<Point> mine = t.of(c);
        int threats = 0;
        for (Point a : mine) for (Point b : mine) {
            if (a.equals(b)) continue;
            Point v  = new Point(b.x - a.x, b.y - a.y);
            Point vp = v.perp();
            Point d1 = new Point(a.x + vp.x, a.y + vp.y);
            Point c1 = new Point(b.x + vp.x, b.y + vp.y);
            if (t.inBounds(d1) && t.inBounds(c1)) {
                int own = (mine.contains(a)?1:0)+(mine.contains(b)?1:0)+(mine.contains(d1)?1:0)+(mine.contains(c1)?1:0);
                if (own == 3) threats++;
            }
            Point d2 = new Point(a.x - vp.x, a.y - vp.y);
            Point c2 = new Point(b.x - vp.x, b.y - vp.y);
            if (t.inBounds(d2) && t.inBounds(c2)) {
                int own2 = (mine.contains(a)?1:0)+(mine.contains(b)?1:0)+(mine.contains(d2)?1:0)+(mine.contains(c2)?1:0);
                if (own2 == 3) threats++;
            }
        }
        return threats;
    }
}
