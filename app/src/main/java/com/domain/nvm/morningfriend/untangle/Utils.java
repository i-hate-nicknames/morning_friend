package com.domain.nvm.morningfriend.untangle;

import android.graphics.PointF;

public class Utils {

    private static final double ALMOST_EQUAL_EPSILON = .0000001;

    private Utils() {

    }

    public static boolean almostEqual(float x, float y) {
        return Math.abs(x-y) < ALMOST_EQUAL_EPSILON;
    }

    public static float slope(PointF a1, PointF a2) {
        float slope;
        if (a2.x - a1.x == 0) {
            if (a2.y >= a1.y) {
                slope = Float.POSITIVE_INFINITY;
            }
            else {
                slope = Float.NEGATIVE_INFINITY;
            }
        }
        else {
            slope = (a2.y - a1.y) / (a2.x - a1.x);
        }
        return slope;
    }

    public static float intercept(PointF a, float slope) {
        if (slope == Float.POSITIVE_INFINITY || slope == Float.NEGATIVE_INFINITY) {
            return a.x;
        }
        return a.y - a.x * slope;
    }

    /**
     * True if given value v lies within range of two values a and b
     */
    public static boolean liesWithin(float v, float a, float b) {
        return v >= Math.min(a, b) && v <= Math.max(a, b);
    }

    public static boolean segmentsOverlap(float a1, float a2, float b1, float b2) {
        return liesWithin(a1, b1, b2) || liesWithin(a2, b1, b2);
    }

    public static boolean intersects(PointF a1, PointF a2, PointF b1, PointF b2) {
        float slopeA = slope(a1, a2), slopeB = slope(b1, b2);
        float interA = intercept(a1, slopeA);
        float interB = intercept(b1, slopeB);
        boolean isAVertical =
                slopeA == Double.POSITIVE_INFINITY || slopeA == Double.NEGATIVE_INFINITY;
        boolean isBVertical =
                slopeB == Double.POSITIVE_INFINITY || slopeB == Double.NEGATIVE_INFINITY;
        if (isAVertical && isBVertical) {
            if (a1.x != b1.x) return false;
            return !(liesWithin(b1.y, a1.y, a2.y) || liesWithin(b2.y, a1.y, a2.y));
        }
        if (isAVertical) {
            float sectY = interB + slopeB * interA;
            // belongs to both A and B
            return liesWithin(sectY, a1.y, a2.y) && liesWithin(interA, b1.x, b2.x);
        }
        if (isBVertical) {
            float sectY = interA + slopeA * interB;
            return liesWithin(sectY, b1.y, b2.y) && liesWithin(interB, a1.x, a2.x);
        }
        // two lines are parallel
        if (almostEqual(slopeA, slopeB)) {
            // lines overlap and have same intercept
            return almostEqual(interA, interB) && segmentsOverlap(a1.x, a2.x, b1.x, b2.x);
        }
        float sectX = -(interA-interB)/slopeA-slopeB;
        return liesWithin(sectX, a1.x, a2.x) && liesWithin(sectX, b1.x, b2.x);

    }
}
