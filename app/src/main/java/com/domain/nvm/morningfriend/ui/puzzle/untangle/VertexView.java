package com.domain.nvm.morningfriend.ui.puzzle.untangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.view.View;

import com.domain.nvm.morningfriend.ui.puzzle.untangle.data.Vertex;

public class VertexView extends View {

    public static final int RADIUS = 35;

    public static final int VERTEX_COLOR = 0xffff0000;

    private Paint mPaint;

    private Vertex mVertex;

    public VertexView(Vertex v, Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(VERTEX_COLOR);
        this.mVertex = v;
    }

    public Vertex getVertex() {
        return mVertex;
    }

    public void setVertex(Vertex vertex) {
        mVertex = vertex;
    }

    public float getCenterX() {
        return mVertex.getPosition().x + RADIUS;
    }

    public float getCenterY() {
        return mVertex.getPosition().y + RADIUS;
    }

    public int getVertexX() {
        return (int) mVertex.getX();
    }

    public int getVertexY() {
        return (int) mVertex.getY();
    }

    public void setColor(@ColorInt int color) {
        mPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, RADIUS, mPaint);
    }


}
