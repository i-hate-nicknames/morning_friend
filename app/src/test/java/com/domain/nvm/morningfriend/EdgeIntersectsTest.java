package com.domain.nvm.morningfriend;

import android.graphics.PointF;

import com.domain.nvm.morningfriend.untangle.Graph;
import com.domain.nvm.morningfriend.untangle.Vertex;

import org.junit.Test;

import static org.junit.Assert.*;

public class EdgeIntersectsTest {

    @Test
    public void lineTest() {
        Graph g = new Graph();
        Vertex[] vs = new Vertex[3];
        for (int i = 0; i < 3; i++) {
            Vertex v = new Vertex(0, new PointF(100*i, 200*i));
            g.addVertex(v);
            vs[i] = v;
        }
        g.connect(vs[0], vs[1]);
        g.connect(vs[1], vs[2]);
        g.connect(vs[2], vs[3]);
        for (Graph.Edge e: g.getEdges()) {
            for (Graph.Edge r: g.getEdges()) {
                assertFalse(e.intersects(r));
            }
        }
    }
}
