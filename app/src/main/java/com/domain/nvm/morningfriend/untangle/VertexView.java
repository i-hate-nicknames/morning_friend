package com.domain.nvm.morningfriend.untangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class VertexView extends View {

    public static final int RADIUS = 35;
    private static Paint sPaint;

    static {
        sPaint = new Paint();
        sPaint.setColor(0xffff0000);
    }

    private Vertex mVertex;

    public VertexView(Vertex v, Context context) {
        super(context);
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
        return (int) mVertex.getPosition().x;
    }

    public int getVertexY() {
        return (int) mVertex.getPosition().y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, RADIUS, sPaint);
    }


}
