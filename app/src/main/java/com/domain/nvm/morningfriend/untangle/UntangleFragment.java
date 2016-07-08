package com.domain.nvm.morningfriend.untangle;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;


public class UntangleFragment extends Fragment implements View.OnTouchListener {

    private static final String TAG = "DragDemo";
    private UntangleField mField;
    private Graph mGraph;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mField = new UntangleField(getActivity());
        mField.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                generateGraph();
                mField.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
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
        mGraph = GraphReader.getGraph(getActivity());
        mGraph.shufflePositions();
        mGraph.scaleVertexPositions(mField.getWidth()*0.8f, mField.getHeight()*0.8f);
        mGraph.moveVertexPositions(mField.getWidth()*0.05f, mField.getHeight()*0.05f);
        for (Vertex v: mGraph.getVertices()) {
            VertexView vView = new VertexView(v, getActivity());
            vView.setOnTouchListener(this);
            mField.addVertexView(vView);
        }
        mField.setGraph(mGraph);

    }

}
