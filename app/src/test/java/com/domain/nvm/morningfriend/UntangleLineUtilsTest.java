package com.domain.nvm.morningfriend;


import com.domain.nvm.morningfriend.puzzle.untangle.utils.LineUtils;

import org.junit.Test;

import static org.junit.Assert.*;

public class UntangleLineUtilsTest {

    private static final double EPSILON = .000001;

    @Test
    public void slopeTest() {
        assertEquals(1.0, LineUtils.slope(1, 1, 2, 2), EPSILON);
        assertEquals(2.0, LineUtils.slope(0, 0, 2, 4), EPSILON);
        assertEquals(0.5, LineUtils.slope(0, 0, 4, 2), EPSILON);
        assertEquals(Float.POSITIVE_INFINITY, LineUtils.slope(0, 0, 0, 1), EPSILON);
        assertEquals(Float.NEGATIVE_INFINITY, LineUtils.slope(0, 1, 0, 0), EPSILON);
        assertEquals(-1, LineUtils.slope(3, 0, 0, 3), EPSILON);
        assertEquals(-1, LineUtils.slope(0, 3, 3, 0), EPSILON);
    }

    @Test
    public void interceptTest() {
        assertEquals(0, LineUtils.intercept(0, 0, 1), EPSILON);
        assertEquals(1, LineUtils.intercept(0, 1, 2), EPSILON);
        assertEquals(2, LineUtils.intercept(2, 3, 0.5f), EPSILON);
    }

    @Test
    public void liesWithinTest() {
        assertTrue(LineUtils.liesWithin(2, 1, 2));
        assertTrue(LineUtils.liesWithin(2, 2, 1));
        assertTrue(LineUtils.liesWithin(1.5f, 2, 1));
        assertFalse(LineUtils.liesWithin(0, 2, 1));
        assertFalse(LineUtils.liesWithin(3, 5, 10));
    }

    @Test
    public void segmentsOverlapTest() {
        assertTrue(LineUtils.segmentsOverlap(1, 3, 2, 4));
        assertTrue(LineUtils.segmentsOverlap(2, 4, 1, 3));
        assertTrue(LineUtils.segmentsOverlap(1, 2, 2, 3));
        assertTrue(LineUtils.segmentsOverlap(3, 2, 2, 1));
        assertTrue(LineUtils.segmentsOverlap(0, 5, 1, 3));
        assertFalse(LineUtils.segmentsOverlap(1, 2, 3, 4));
        assertFalse(LineUtils.segmentsOverlap(3, 4, 2, 1));
    }

}
