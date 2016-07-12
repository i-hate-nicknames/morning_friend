package com.domain.nvm.morningfriend.untangle;

import android.graphics.PointF;
import android.util.Log;

public class Utils {

    private static final double ALMOST_EQUAL_EPSILON = .0000001;

    private Utils() {

    }

    public static boolean almostEqual(float x, float y) {
        return Math.abs(x-y) < ALMOST_EQUAL_EPSILON;
    }

    public static float slope(float ax, float ay, float bx, float by) {
        float slope;
        if (bx - ax == 0) {
            if (by >= ay) {
                slope = Float.POSITIVE_INFINITY;
            }
            else {
                slope = Float.NEGATIVE_INFINITY;
            }
        }
        else {
            slope = (by - ay) / (bx - ax);
        }
        return slope;
    }

    public static float intercept(float px, float py, float slope) {
        if (slope == Float.POSITIVE_INFINITY || slope == Float.NEGATIVE_INFINITY) {
            return px;
        }
        return py - px * slope;
    }

    /**
     * True if given value v lies within range of two values a and b
     */
    public static boolean liesWithin(float v, float a, float b) {
        return v >= Math.min(a, b) && v <= Math.max(a, b);
    }

    public static boolean segmentsOverlap(float a1, float a2, float b1, float b2) {
        if (Math.abs(a1-a2) > Math.abs(b1-b2)) {
            return liesWithin(b1, a1, a2) || liesWithin(b2, a1, a2);
        }
        else {
            return liesWithin(a1, b1, b2) || liesWithin(a2, b1, b2);
        }

    }

    public static boolean intersects(PointF a1, PointF a2, PointF b1, PointF b2) {
        return intersects(a1.x, a1.y, a2.x, a2.y, b1.x, b1.y, b2.x, b2.y);
    }

    public static boolean linesTouch(PointF a1, PointF a2, PointF b1, PointF b2) {
        return a1 == b1 || a1 == b2 || a2 == b1 || a2 == b2;
    }

    public static boolean intersects(float a1x, float a1y, float a2x, float a2y,
                                     float b1x, float b1y, float b2x, float b2y) {
        float slopeA = slope(a1x, a1y, a2x, a2y);
        float slopeB = slope(b1x, b1y, b2x, b2y);
        float interA = intercept(a1x, a1y, slopeA);
        float interB = intercept(b1x, b1y, slopeB);
        boolean isAVertical =
                slopeA == Double.POSITIVE_INFINITY || slopeA == Double.NEGATIVE_INFINITY;
        boolean isBVertical =
                slopeB == Double.POSITIVE_INFINITY || slopeB == Double.NEGATIVE_INFINITY;
        if (isAVertical && isBVertical) {
            if (a1x != b1x) return false;
            return liesWithin(b1y, a1y, a2y) || liesWithin(b2y, a1y, a2y);
        }
        if (isAVertical) {
            float sectY = interB + slopeB * interA;
            // belongs to both A and B
            return liesWithin(sectY, a1y, a2y) && liesWithin(interA, b1x, b2x);
        }
        if (isBVertical) {
            float sectY = interA + slopeA * interB;
            return liesWithin(sectY, b1y, b2y) && liesWithin(interB, a1x, a2x);
        }
        // two lines are parallel
        if (almostEqual(slopeA, slopeB)) {
            // lines overlap and have same intercept
            return almostEqual(interA, interB) && segmentsOverlap(a1x, a2x, b1x, b2x);
        }
        float sectX = -(interA-interB)/(slopeA-slopeB);
        return liesWithin(sectX, a1x, a2x) && liesWithin(sectX, b1x, b2x);

    }
}
