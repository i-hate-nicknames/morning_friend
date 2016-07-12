package com.domain.nvm.morningfriend;


import com.domain.nvm.morningfriend.untangle.Utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class LinesIntersectsTest {

    @Test
    public void verticalTest() throws Exception {

        // one to the right of another
        assertFalse(Utils.intersects(0, 0, 0, 5, 2, 2, 2, 5));
        // one above another
        assertFalse(Utils.intersects(0, 0, 0, 5, 0, 7, 0, 11));
        // one below another
        assertFalse(Utils.intersects(0, 5, 0, 7, 0, 2, 0, 4));
        // one inside another
        assertTrue(Utils.intersects(0, 1, 0, 7, 0, 4, 0, 6));
        // one intersecting another
        assertTrue(Utils.intersects(0, 1, 0, 7, 0, 4, 0, 12));
        // one starting from another
        assertTrue(Utils.intersects(0, 1, 0, 7, 0, 7, 0, 12));
    }

    @Test
    public void oneVerticalTest() throws Exception {
        // one above the other
        assertFalse(Utils.intersects(0, 0, 0, 2, -1, 4, 4, 4));
        // one below the other
        assertFalse(Utils.intersects(0, 5, 0, 7, 0, 0, 5, 5));
        // one to the right of another
        assertFalse(Utils.intersects(0, 0, 0, 10, 1, 1, 11, 6));

        assertTrue(Utils.intersects(5, 0, 5, 10, 4, 0, 7, 5));
    }

    @Test
    public void twoParallelTest() throws Exception {
        // one above the other
        assertFalse(Utils.intersects(0, 0, 5, 5, 1, 0, 6, 5));
        // one inside the other
        assertTrue(Utils.intersects(0, 0, 5, 5, 1, 1, 3, 3));
        // one intersecting another
        assertTrue(Utils.intersects(0, 0, 5, 5, 3, 3, 7, 7));

    }

    @Test
    public void twoArbitraryTest() throws Exception {

        assertTrue(Utils.intersects(0, 0, 5, 5, 3, 0, 0, 3));
        // to the right and below
        assertFalse(Utils.intersects(0, 0, 5, 5, 6, 0, 12, 3));
        // below
        assertFalse(Utils.intersects(0, 0, 5, 5, 6, 0, 4, 2));
    }
}