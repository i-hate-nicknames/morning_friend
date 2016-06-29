package com.domain.nvm.morningfriend.untangle;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class UntangleFragment extends Fragment {

    private static final String TAG = "DragDemo";

    private static final int SIZE = Circle.RADIUS * 2;
    private static final int INITIAL_OFFSET = 50;


    private FrameLayout.LayoutParams mParams;
    private FrameLayout mFrameLayout;
    private Circle mCircle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFrameLayout = new FrameLayout(getActivity());

        mCircle = new Circle(getActivity());
        mCircle.setPosition(new PointF(INITIAL_OFFSET, INITIAL_OFFSET));
        mParams = new FrameLayout.LayoutParams(SIZE, SIZE, Gravity.LEFT | Gravity.TOP);
        mFrameLayout.addView(mCircle, mParams);

        mCircle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        int y = (int) event.getY();
                        int x = (int) event.getX();
                        Log.i(TAG, "X: " + x + " Y: " + y);
                        updatePosition(x, y);
                }
                return true;
            }
        });
        return mFrameLayout;
    }

    private void updatePosition(int offsetX, int offsetY) {
        PointF pos = mCircle.getPosition();
        int x = (int) pos.x + (offsetX - SIZE/2);
        int y = (int) pos.y + (offsetY - SIZE/2);
        mParams.setMargins(x, y, 0, 0);
        mFrameLayout.updateViewLayout(mCircle, mParams);
        mCircle.setPosition(new PointF(x, y));
    }


}
