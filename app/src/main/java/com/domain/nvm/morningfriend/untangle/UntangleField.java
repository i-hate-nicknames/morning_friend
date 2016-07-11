package com.domain.nvm.morningfriend.untangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.Gravity;
import android.widget.FrameLayout;

public class UntangleField extends FrameLayout {

    private static final int SIZE = VertexView.RADIUS * 2;
    private Graph mGraph;
    private Paint mLinePaint;
    private Paint mIntersectingLinePaint;
    private VertexView[] mVertexViews;

    public UntangleField(Context context) {
        super(context);
        mVertexViews = new VertexView[Graph.MAX_ITEMS];
        for (int i = 0; i < mVertexViews.length; i++) {
            mVertexViews[i] = null;
        }
        mLinePaint = new Paint();
        mLinePaint.setColor(0xff222222);
        mLinePaint.setStrokeWidth(4);
        mIntersectingLinePaint = new Paint();
        mIntersectingLinePaint.setColor(0xffff0000);
        mIntersectingLinePaint.setStrokeWidth(4);
        this.setWillNotDraw(false);
    }

    public void setGraph(Graph graph) {
        this.mGraph = graph;
    }

    public void addVertexView(VertexView vView) {
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(SIZE, SIZE, Gravity.LEFT | Gravity.TOP);
        params.setMargins(vView.getVertexX(), vView.getVertexY(), 0, 0);
        addView(vView, params);
        mVertexViews[vView.getVertex().getNum()] = vView;
    }

    public void updateCirclePosition(VertexView vView, int offsetX, int offsetY) {
        PointF pos = vView.getVertex().getPosition();
        // center the circle at current touch point
        // when clicked in the center of circle, offsetX and offsetY are equal to radius
        int x = (int) pos.x + (offsetX - VertexView.RADIUS);
        int y = (int) pos.y + (offsetY - VertexView.RADIUS);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) vView.getLayoutParams();
        params.setMargins(x, y, 0, 0);
        updateViewLayout(vView, params);
        vView.getVertex().setPosition(x, y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Graph.Edge e: mGraph.getIntersectingEdges()) {
            drawEdge(canvas, e, true);
        }
        for (Graph.Edge e: mGraph.getNonIntersectingEdges()) {
            drawEdge(canvas, e, false);
        }

    }

    private void drawEdge(Canvas c, Graph.Edge e, boolean intersect) {
        Vertex u = e.getFirst();
        Vertex v = e.getSecond();
        VertexView uView = mVertexViews[u.getNum()];
        VertexView vView = mVertexViews[v.getNum()];
        Paint paint;
        if (intersect) {
            paint = mIntersectingLinePaint;
        }
        else {
            paint = mLinePaint;
        }
        c.drawLine(uView.getCenterX(), uView.getCenterY(),
                vView.getCenterX(), vView.getCenterY(), paint);
    }
}
