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
    private List<Circle> mCircles;

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
                mField.updateCirclePosition((Circle) v, x, y);
        }
        return true;
    }


    private void generateCircles() {
        mCircles = new ArrayList<>();
        Circle c1 = new Circle(getActivity());
        c1.setPosition(new PointF(100, 100));
        mCircles.add(c1);

        Circle c2 = new Circle(getActivity());
        c2.setPosition(new PointF(200, 200));
        mCircles.add(c2);

        for (Circle c: mCircles) {
            c.setOnTouchListener(this);
            mField.addCircle(c);
        }
        mField.setCircles(mCircles);

    }

}
