package com.domain.nvm.morningfriend.untangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.Gravity;
import android.widget.FrameLayout;

import java.util.List;

public class UntangleField extends FrameLayout {

    private static final int SIZE = Circle.RADIUS * 2;
    private List<Circle> mCircles;
    private Paint mLinePaint;

    public UntangleField(Context context) {
        super(context);
        mLinePaint = new Paint();
        mLinePaint.setColor(0xff222222);
        mLinePaint.setStrokeWidth(4);
        this.setWillNotDraw(false);
    }

    public void setCircles(List<Circle> circles) {
        this.mCircles = circles;
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Circle c1 = mCircles.get(0);
        Circle c2 = mCircles.get(1);
        float x1 = c1.getPosition().x + Circle.RADIUS, y1 = c1.getPosition().y + Circle.RADIUS;
        float x2 = c2.getPosition().x + Circle.RADIUS, y2 = c2.getPosition().y + Circle.RADIUS;

        canvas.drawLine(x1, y1, x2, y2, mLinePaint);
    }
}
