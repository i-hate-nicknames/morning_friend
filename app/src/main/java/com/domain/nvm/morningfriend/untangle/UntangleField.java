package com.domain.nvm.morningfriend.untangle;

import android.content.Context;
import android.graphics.PointF;
import android.view.Gravity;
import android.widget.FrameLayout;

public class UntangleField extends FrameLayout {

    private static final int SIZE = Circle.RADIUS * 2;

    public UntangleField(Context context) {
        super(context);

    }

    public void addCircle(Circle c) {
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(SIZE, SIZE, Gravity.LEFT | Gravity.TOP);
        params.setMargins((int) c.getPosition().x, (int) c.getPosition().y, 0, 0);
        addView(c, params);
    }

    public void updateCirclePosition(Circle c, int offsetX, int offsetY) {
        PointF pos = c.getPosition();
        int x = (int) pos.x + (offsetX - SIZE/2);
        int y = (int) pos.y + (offsetY - SIZE/2);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) c.getLayoutParams();
        params.setMargins(x, y, 0, 0);
        updateViewLayout(c, params);
        c.setPosition(new PointF(x, y));
    }
}
