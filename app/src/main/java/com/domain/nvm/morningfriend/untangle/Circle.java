package com.domain.nvm.morningfriend.untangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

public class Circle extends View {

    public static final int RADIUS = 35;

    private PointF position;
    private Paint mPaint;

    public Circle(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(0xffff0000);
    }

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        this.position = position;
    }

    public float getCenterX() {
        return position.x + RADIUS;
    }

    public float getCenterY() {
        return position.y + RADIUS;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, RADIUS, mPaint);
    }
}
