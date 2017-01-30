package com.domain.nvm.morningfriend.features.puzzle.untangle.data;

import android.graphics.PointF;

import com.domain.nvm.morningfriend.features.puzzle.data.Edge;
import com.domain.nvm.morningfriend.features.puzzle.untangle.utils.LineUtils;

public class IntersectingEdge implements Edge {

    private CartesianVertex u, v;

    public IntersectingEdge(CartesianVertex v1, CartesianVertex v2) {
        if (v1.getNum() < v2.getNum()) {
            u = v1;
            v = v2;
        }
        else {
            u = v2;
            v = v1;
        }
    }

    public boolean intersects(IntersectingEdge other) {
        if (this == other) {
            return false;
        }
        PointF a, b, c, d;
        a = u.getPosition();
        b = v.getPosition();
        c = other.getFirst().getPosition();
        d = other.getSecond().getPosition();
        return !LineUtils.linesTouch(a, b, c, d) && LineUtils.intersects(a, b, c, d);
    }

    public CartesianVertex getFirst() {
        return u;
    }

    public CartesianVertex getSecond() {
        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntersectingEdge that = (IntersectingEdge) o;

        if (!u.equals(that.u)) return false;
        return v.equals(that.v);

    }

    @Override
    public int hashCode() {
        int result = u.hashCode();
        result = 31 * result + v.hashCode();
        return result;
    }

    public static class IntersectingEdgeFactory
            implements EdgeFactory<IntersectingEdge, CartesianVertex> {

        @Override
        public IntersectingEdge makeEdge(CartesianVertex u, CartesianVertex v) {
            return new IntersectingEdge(u, v);
        }
    }

}