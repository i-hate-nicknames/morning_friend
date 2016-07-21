package com.domain.nvm.morningfriend;


import com.domain.nvm.morningfriend.alert.puzzles.untangle.Utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsTest {

    private static final double EPSILON = .000001;

    @Test
    public void slopeTest() {
        assertEquals(1.0, Utils.slope(1, 1, 2, 2), EPSILON);
        assertEquals(2.0, Utils.slope(0, 0, 2, 4), EPSILON);
        assertEquals(0.5, Utils.slope(0, 0, 4, 2), EPSILON);
        assertEquals(Float.POSITIVE_INFINITY, Utils.slope(0, 0, 0, 1), EPSILON);
        assertEquals(Float.NEGATIVE_INFINITY, Utils.slope(0, 1, 0, 0), EPSILON);
        assertEquals(-1, Utils.slope(3, 0, 0, 3), EPSILON);
        assertEquals(-1, Utils.slope(0, 3, 3, 0), EPSILON);
    }

    @Test
    public void interceptTest() {
        assertEquals(0, Utils.intercept(0, 0, 1), EPSILON);
        assertEquals(1, Utils.intercept(0, 1, 2), EPSILON);
        assertEquals(2, Utils.intercept(2, 3, 0.5f), EPSILON);
    }

    @Test
    public void liesWithinTest() {
        assertTrue(Utils.liesWithin(2, 1, 2));
        assertTrue(Utils.liesWithin(2, 2, 1));
        assertTrue(Utils.liesWithin(1.5f, 2, 1));
        assertFalse(Utils.liesWithin(0, 2, 1));
        assertFalse(Utils.liesWithin(3, 5, 10));
    }

    @Test
    public void segmentsOverlapTest() {
        assertTrue(Utils.segmentsOverlap(1, 3, 2, 4));
        assertTrue(Utils.segmentsOverlap(2, 4, 1, 3));
        assertTrue(Utils.segmentsOverlap(1, 2, 2, 3));
        assertTrue(Utils.segmentsOverlap(3, 2, 2, 1));
        assertTrue(Utils.segmentsOverlap(0, 5, 1, 3));
        assertFalse(Utils.segmentsOverlap(1, 2, 3, 4));
        assertFalse(Utils.segmentsOverlap(3, 4, 2, 1));
    }

}
