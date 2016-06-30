package com.domain.nvm.morningfriend.untangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

public class Vertex extends View {

    public static final int RADIUS = 35;

    private PointF position;
    private int mNum;

    private static Paint sPaint;

    static {
        sPaint = new Paint();
        sPaint.setColor(0xffff0000);
    }

    public Vertex(int num, Context context) {
        super(context);
        this.mNum = num;
    }

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        this.position = position;
    }

    public int getNum() {
        return mNum;
    }

    public float getCenterX() {
        return position.x + RADIUS;
    }

    public float getCenterY() {
        return position.y + RADIUS;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, RADIUS, sPaint);
    }
}
