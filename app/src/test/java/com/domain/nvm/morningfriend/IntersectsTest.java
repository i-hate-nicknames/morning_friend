package com.domain.nvm.morningfriend;

import android.graphics.PointF;

import com.domain.nvm.morningfriend.untangle.Utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class IntersectsTest {

    private static boolean testIntersection(float a1x, float a1y, float a2x, float a2y,
                                            float b1x, float b1y, float b2x, float b2y) {
        PointF a1 = new PointF(a1x, a1y);
        PointF a2 = new PointF(a2x, a2y);
        PointF b1 = new PointF(b1x, b1y);
        PointF b2 = new PointF(b2x, b2y);
        return Utils.intersects(a1, a2, b1, b2);
    }

    @Test
    public void verticalTest() throws Exception {

        // one to the right of another
        assertFalse(testIntersection(0, 0, 0, 5, 2, 2, 2, 5));
        // one above another
        assertFalse(testIntersection(0, 0, 0, 5, 0, 7, 0, 11));
        // one below another
        assertFalse(testIntersection(0, 5, 0, 7, 0, 2, 0, 4));
        // one inside another
        assertTrue(testIntersection(0, 1, 0, 7, 0, 4, 0, 6));
        // one intersecting another
        assertTrue(testIntersection(0, 1, 0, 7, 0, 4, 0, 12));
        // one starting from another
        assertTrue(testIntersection(0, 1, 0, 7, 0, 7, 0, 12));
    }

    @Test
    public void oneVerticalTest() throws Exception {
        // one above the other
        assertFalse(testIntersection(0, 0, 0, 2, -1, 4, 4, 4));
        // one below the other
        assertFalse(testIntersection(0, 5, 0, 7, 0, 0, 5, 5));
        // one to the right of another
        assertFalse(testIntersection(0, 0, 0, 10, 1, 1, 11, 6));

        assertTrue(testIntersection(5, 0, 5, 10, 4, 0, 7, 5));
    }

    @Test
    public void twoParallelTest() throws Exception {
        // one above the other
        assertFalse(testIntersection(0, 0, 5, 5, 1, 0, 6, 5));
        // one inside the other
        assertTrue(testIntersection(0, 0, 5, 5, 1, 1, 3, 3));
        // one intersecting another
        assertTrue(testIntersection(0, 0, 5, 5, 3, 3, 7, 7));

    }

    @Test
    public void twoArbitraryTest() throws Exception {

        assertTrue(testIntersection(0, 0, 5, 5, 3, 0, 0, 3));
        // to the right and below
        assertFalse(testIntersection(0, 0, 5, 5, 6, 0, 12, 3));
        // below
        assertFalse(testIntersection(0, 0, 5, 5, 6, 0, 4, 2));
    }
}