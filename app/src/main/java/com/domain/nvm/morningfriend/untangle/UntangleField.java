package com.domain.nvm.morningfriend.untangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.Gravity;
import android.widget.FrameLayout;

import java.util.List;

public class UntangleField extends FrameLayout {

    private static final int SIZE = Vertex.RADIUS * 2;
    private List<Vertex> mVertices;
    private Paint mLinePaint;

    public UntangleField(Context context) {
        super(context);
        mLinePaint = new Paint();
        mLinePaint.setColor(0xff222222);
        mLinePaint.setStrokeWidth(4);
        this.setWillNotDraw(false);
    }

    public void setVertices(List<Vertex> vertices) {
        this.mVertices = vertices;
    }

    public void addVertex(Vertex v) {
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(SIZE, SIZE, Gravity.LEFT | Gravity.TOP);
        params.setMargins((int) v.getPosition().x, (int) v.getPosition().y, 0, 0);
        addView(v, params);
    }

    public void updateCirclePosition(Vertex v, int offsetX, int offsetY) {
        PointF pos = v.getPosition();
        // center the circle at current touch point
        // when clicked in the center of circle, offsetX and offsetY are equal to radius
        int x = (int) pos.x + (offsetX - Vertex.RADIUS);
        int y = (int) pos.y + (offsetY - Vertex.RADIUS);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
        params.setMargins(x, y, 0, 0);
        updateViewLayout(v, params);
        v.setPosition(new PointF(x, y));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Vertex c1 = mVertices.get(0);
        Vertex c2 = mVertices.get(1);
        canvas.drawLine(c1.getCenterX(), c1.getCenterY(),
                c2.getCenterX(), c2.getCenterY(), mLinePaint);
    }
}
