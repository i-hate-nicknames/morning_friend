package com.domain.nvm.morningfriend;


import com.domain.nvm.morningfriend.puzzle.untangle.utils.LineUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class LinesIntersectsTest {

    @Test
    public void verticalTest() throws Exception {

        // one to the right of another
        assertFalse(LineUtils.intersects(0, 0, 0, 5, 2, 2, 2, 5));
        // one above another
        assertFalse(LineUtils.intersects(0, 0, 0, 5, 0, 7, 0, 11));
        // one below another
        assertFalse(LineUtils.intersects(0, 5, 0, 7, 0, 2, 0, 4));
        // one inside another
        assertTrue(LineUtils.intersects(0, 1, 0, 7, 0, 4, 0, 6));
        // one intersecting another
        assertTrue(LineUtils.intersects(0, 1, 0, 7, 0, 4, 0, 12));
        // one starting from another
        assertTrue(LineUtils.intersects(0, 1, 0, 7, 0, 7, 0, 12));
    }

    @Test
    public void oneVerticalTest() throws Exception {
        // one above the other
        assertFalse(LineUtils.intersects(0, 0, 0, 2, -1, 4, 4, 4));
        // one below the other
        assertFalse(LineUtils.intersects(0, 5, 0, 7, 0, 0, 5, 5));
        // one to the right of another
        assertFalse(LineUtils.intersects(0, 0, 0, 10, 1, 1, 11, 6));

        assertTrue(LineUtils.intersects(5, 0, 5, 10, 4, 0, 7, 5));
    }

    @Test
    public void twoParallelTest() throws Exception {
        // one above the other
        assertFalse(LineUtils.intersects(0, 0, 5, 5, 1, 0, 6, 5));
        // one inside the other
        assertTrue(LineUtils.intersects(0, 0, 5, 5, 1, 1, 3, 3));
        // one intersecting another
        assertTrue(LineUtils.intersects(0, 0, 5, 5, 3, 3, 7, 7));

    }

    @Test
    public void twoArbitraryTest() throws Exception {

        assertTrue(LineUtils.intersects(0, 0, 5, 5, 3, 0, 0, 3));
        // to the right and below
        assertFalse(LineUtils.intersects(0, 0, 5, 5, 6, 0, 12, 3));
        // below
        assertFalse(LineUtils.intersects(0, 0, 5, 5, 6, 0, 4, 2));
    }
}