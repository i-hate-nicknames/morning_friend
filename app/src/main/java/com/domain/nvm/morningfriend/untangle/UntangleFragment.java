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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mField = new UntangleField(getActivity());
        generateCircles();
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


    private void generateCircles() {
        mVertices = new ArrayList<>();
        Vertex v1 = new Vertex(getActivity());
        v1.setPosition(new PointF(100, 100));
        mVertices.add(v1);

        Vertex v2 = new Vertex(getActivity());
        v2.setPosition(new PointF(200, 200));
        mVertices.add(v2);

        for (Vertex v: mVertices) {
            v.setOnTouchListener(this);
            mField.addVertex(v);
        }
        mField.setVertices(mVertices);

    }

}
