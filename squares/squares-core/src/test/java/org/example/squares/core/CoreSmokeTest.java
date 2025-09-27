package org.example.squares.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CoreSmokeTest {

    @Test
    void rotatedSquareWin() {
        GameState s = new GameState(10, Color.W);
        s.white.add(new Point(1,1));
        s.white.add(new Point(2,3));
        s.white.add(new Point(4,2));
        s.turn = Color.W;

        Rules.applyMove(s, new Point(3,0), Color.W);

        assertEquals(GameStatus.WIN_W, s.status);
        assertNotNull(s.winningSquare);
        assertEquals(4, s.winningSquare.size());
    }
}
