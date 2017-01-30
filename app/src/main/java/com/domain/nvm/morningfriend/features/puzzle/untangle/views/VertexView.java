package com.domain.nvm.morningfriend.features.puzzle.untangle.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.view.View;

import com.domain.nvm.morningfriend.features.puzzle.untangle.data.CartesianVertex;

public class VertexView extends View {

    public static final int RADIUS = 35;

    public static final int VERTEX_COLOR = 0xff000000;

    private Paint mPaint;

    private CartesianVertex mVertex;

    public VertexView(CartesianVertex v, Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(VERTEX_COLOR);
        this.mVertex = v;
    }

    public CartesianVertex getVertex() {
        return mVertex;
    }

    public void setVertex(CartesianVertex vertex) {
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
