package com.domain.nvm.morningfriend.untangle;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class UntangleFragment extends Fragment implements View.OnTouchListener {

    private static final String TAG = "DragDemo";
    private UntangleField mField;
    private List<Vertex> mVertices;
    private Graph mGraph;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mField = new UntangleField(getActivity());
        generateGraph();
        return mField;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int y = (int) event.getY();
                int x = (int) event.getX();
                Log.i(TAG, "X: " + x + " Y: " + y);
                mField.updateCirclePosition((Vertex) v, x, y);
        }
        return true;
    }


    private void generateGraph() {
        mGraph = new Graph();
        mVertices = new ArrayList<>();
        Vertex bottom = new Vertex(0, getActivity());
        bottom.setPosition(new PointF(300, 500));
        mVertices.add(bottom);
        mGraph.addVertex(bottom);
        for (int i = 1; i < 6; i++) {
            Vertex v = new Vertex(i, getActivity());
            v.setPosition(new PointF(100+i*50, 100));
            mVertices.add(v);
            mGraph.addVertex(v);
            mGraph.connect(v, bottom);
        }


        for (Vertex v: mVertices) {
            v.setOnTouchListener(this);
            mField.addVertex(v);
        }
        mField.setGraph(mGraph);

    }

}
