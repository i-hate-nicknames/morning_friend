package com.domain.nvm.morningfriend.labyrinth;

import com.domain.nvm.morningfriend.features.puzzle.labyrinth.data.Passages;

import org.junit.Test;

import static org.junit.Assert.*;

public class PassagesTest {

    @Test
    public void noPassageNotPassableTest() {
        Passages ps = new Passages(2);
        assertFalse(ps.isPassable(0, 1));
    }

    @Test
    public void connectTest() {
        Passages ps = new Passages(2);
        ps.connect(0, 1);
        assertTrue(ps.isPassable(0, 1));
        assertFalse(ps.isPassable(0, 2));
    }

}
