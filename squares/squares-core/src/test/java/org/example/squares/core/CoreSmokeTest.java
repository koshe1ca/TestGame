package org.example.squares.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CoreSmokeTest {

    @Test
    void rotatedSquareIsNotWin() {
        GameState s = new GameState(8, Color.W);
        Rules.applyMove(s, new Point(4,4), Color.W);
        Rules.applyMove(s, new Point(0,0), Color.B);
        Rules.applyMove(s, new Point(5,5), Color.W);
        Rules.applyMove(s, new Point(0,1), Color.B);
        Rules.applyMove(s, new Point(4,6), Color.W);
        Rules.applyMove(s, new Point(0,2), Color.B);
        Rules.applyMove(s, new Point(3,5), Color.W);
        assertEquals(GameStatus.ONGOING, s.status);
    }

    @Test
    void axisAlignedSquare2x2IsWin() {
        GameState s = new GameState(8, Color.W);
        Rules.applyMove(s, new Point(2,2), Color.W);
        Rules.applyMove(s, new Point(0,0), Color.B);
        Rules.applyMove(s, new Point(3,2), Color.W);
        Rules.applyMove(s, new Point(0,1), Color.B);
        Rules.applyMove(s, new Point(2,3), Color.W);
        Rules.applyMove(s, new Point(1,0), Color.B);
        Rules.applyMove(s, new Point(3,3), Color.W);
        assertEquals(GameStatus.WIN_W, s.status);
    }

    @Test
    void rectangle2x3IsNotWin() {
        GameState s = new GameState(10, Color.W);
        Rules.applyMove(s, new Point(2,2), Color.W);
        Rules.applyMove(s, new Point(0,0), Color.B);
        Rules.applyMove(s, new Point(4,2), Color.W);
        Rules.applyMove(s, new Point(0,1), Color.B);
        Rules.applyMove(s, new Point(2,5), Color.W);
        Rules.applyMove(s, new Point(0,2), Color.B);
        Rules.applyMove(s, new Point(4,5), Color.W);
        assertEquals(GameStatus.ONGOING, s.status);
    }
}
