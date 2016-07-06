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
                mField.updateCirclePosition((VertexView) v, x, y);
        }
        return true;
    }


    private void generateGraph() {
        mGraph = new Graph();
        List<VertexView> vertexViews = new ArrayList<>();
        Vertex bottom = new Vertex(0, new PointF(300, 500));
        vertexViews.add(new VertexView(bottom, getActivity()));
        mGraph.addVertex(bottom);
        for (int i = 1; i < 6; i++) {
            Vertex v = new Vertex(i, new PointF(100+i*50, 100));
            mGraph.addVertex(v);
            mGraph.connect(v, bottom);
            vertexViews.add(new VertexView(v, getActivity()));
        }
        mGraph.connect(vertexViews.get(1).getVertex(), vertexViews.get(2).getVertex());


        for (VertexView vView: vertexViews) {
            vView.setOnTouchListener(this);
            mField.addVertexView(vView);
        }
        mField.setGraph(mGraph);

    }

}
