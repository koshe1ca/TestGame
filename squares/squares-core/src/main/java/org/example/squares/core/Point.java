package org.example.squares.core;

import java.util.Objects;

public final class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) { this.x = x; this.y = y; }

    public Point add(Point o) { return new Point(x + o.x, y + o.y); }
    public Point sub(Point o) { return new Point(x - o.x, y - o.y); }
    public Point perp() { return new Point(-y, x); } // v⊥ для v=(x,y)

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point p)) return false;
        return x == p.x && y == p.y;
    }
    @Override public int hashCode() { return Objects.hash(x, y); }
    @Override public String toString() { return "(" + x + "," + y + ")"; }
}
