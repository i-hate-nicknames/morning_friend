package com.domain.nvm.morningfriend.untangle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.domain.nvm.morningfriend.R;

public class UntangleField extends FrameLayout implements View.OnTouchListener {

    private static final int VERTEX_VIEW_SIZE = VertexView.RADIUS * 2;
    private static final int EDGE_WIDTH = 4;
    private static final int EDGE_COLOR = 0xff222222;
    private static final int CROSS_EDGE_COLOR = 0xffff0000;
    private static final float MARGIN_PERCENTAGE = .2f;
    private static final float VERTEX_SCALE_PERCENTAGE = 1 - MARGIN_PERCENTAGE;
    private static final float VERTEX_OFFSET = MARGIN_PERCENTAGE / 4;

    private Graph mGraph;
    private Paint mLinePaint;
    private Paint mIntersectingLinePaint;
    private VertexView[] mVertexViews;
    private int mVertexColor;

    public UntangleField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        mVertexViews = new VertexView[Graph.MAX_ITEMS];
        for (int i = 0; i < mVertexViews.length; i++) {
            mVertexViews[i] = null;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UntangleField);
        mVertexColor = a.getColor(R.styleable.UntangleField_vertexColor, VertexView.VERTEX_COLOR);
        int edgeColor = a.getColor(R.styleable.UntangleField_edgeColor, EDGE_COLOR);
        int crossEdgeColor = a.getColor(R.styleable.UntangleField_crossEdgeColor, CROSS_EDGE_COLOR);
        mLinePaint = new Paint();
        mLinePaint.setColor(edgeColor);
        mLinePaint.setStrokeWidth(EDGE_WIDTH);
        mIntersectingLinePaint = new Paint();
        mIntersectingLinePaint.setColor(crossEdgeColor);
        mIntersectingLinePaint.setStrokeWidth(EDGE_WIDTH);
        this.setWillNotDraw(false);
        a.recycle();
    }

    public void generateGraph() {
        mGraph = GraphReader.getGraph(getContext());
        mGraph.shufflePositions();
        mGraph.scaleVertexPositions(getWidth()*VERTEX_SCALE_PERCENTAGE,
                getHeight()*VERTEX_SCALE_PERCENTAGE);
        mGraph.moveVertexPositions(getWidth()*VERTEX_OFFSET, getHeight()*VERTEX_OFFSET);
        for (Vertex v: mGraph.getVertices()) {
            addVertex(v);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int y = (int) event.getY();
                int x = (int) event.getX();
                updateCirclePosition((VertexView) v, x, y);
        }
        return true;
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

    private void addVertex(Vertex v) {
        VertexView vView = new VertexView(v, getContext());
        vView.setColor(mVertexColor);
        vView.setOnTouchListener(this);
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(VERTEX_VIEW_SIZE, VERTEX_VIEW_SIZE, Gravity.LEFT | Gravity.TOP);
        params.setMargins(vView.getVertexX(), vView.getVertexY(), 0, 0);
        addView(vView, params);
        mVertexViews[vView.getVertex().getNum()] = vView;
    }

    private void updateCirclePosition(VertexView vView, int offsetX, int offsetY) {
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
