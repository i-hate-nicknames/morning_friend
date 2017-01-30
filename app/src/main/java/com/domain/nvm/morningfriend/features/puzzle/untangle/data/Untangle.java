package com.domain.nvm.morningfriend.features.puzzle.untangle.data;

import com.domain.nvm.morningfriend.features.puzzle.data.UndirectedGraph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Untangle extends UndirectedGraph<CartesianVertex, IntersectingEdge> {

    public static final int MAX_ITEMS = 25;

    public Untangle() {
        super(MAX_ITEMS, new IntersectingEdge.IntersectingEdgeFactory());
    }


    public void scaleVertexPositions(float scaleX, float scaleY) {
        for (CartesianVertex v: getVertices()) {
            v.setPosition(v.getX() * scaleX, v.getY() * scaleY);
        }
    }

    public void moveVertexPositions(float moveX, float moveY) {
        for (CartesianVertex v: getVertices()) {
            v.setPosition(v.getX() + moveX, v.getY() + moveY);
        }
    }

    public void shufflePositions() {
        Random rand = new Random();
        int vertexNum = getVertices().size();
        for (int i = 0; i < vertexNum; i++) {
            int uIdx = rand.nextInt(vertexNum), vIdx = rand.nextInt(vertexNum);
            CartesianVertex u = getVertex(uIdx), v = getVertex(vIdx);
            // the more vertices, the higher chance to swap them rather than change coordinate
            if (Math.random() > 1.0 / vertexNum) {
                u.swapX(v);
                u.swapY(v);
            }
            else {
                float x = 1.0f / (rand.nextInt(5)+1);
                float y = 1.0f / (rand.nextInt(5)+1);
                u.setX(x);
                u.setY(y);
            }
        }
        if (isSolved()) {
            shufflePositions();
        }
    }


    public Set<IntersectingEdge> getIntersectingEdges() {
        HashSet<IntersectingEdge> intersecting = new HashSet<>();
        // not optimal but meh the number of edges will never get high
        for (IntersectingEdge e: getEdges()) {
            for (IntersectingEdge r: getEdges()) {
                if (e.intersects(r)) {
                    intersecting.add(e);
                    break;
                }
            }
        }
        return intersecting;
    }

    public Set<IntersectingEdge> getNonIntersectingEdges() {
        HashSet<IntersectingEdge> nonIntersecting = new HashSet<>(getEdges());
        nonIntersecting.removeAll(getIntersectingEdges());
        return nonIntersecting;
    }

    public boolean isSolved() {
        return getIntersectingEdges().size() == 0;
    }

}
