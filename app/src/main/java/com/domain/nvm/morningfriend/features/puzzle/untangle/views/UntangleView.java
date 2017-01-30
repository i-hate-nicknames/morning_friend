package com.domain.nvm.morningfriend.features.puzzle.untangle.views;

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
import com.domain.nvm.morningfriend.features.puzzle.Puzzle;
import com.domain.nvm.morningfriend.features.puzzle.PuzzleHost;
import com.domain.nvm.morningfriend.features.puzzle.untangle.data.CartesianVertex;
import com.domain.nvm.morningfriend.features.puzzle.untangle.data.IntersectingEdge;
import com.domain.nvm.morningfriend.features.puzzle.untangle.data.Untangle;
import com.domain.nvm.morningfriend.features.puzzle.untangle.utils.GraphReader;

public class UntangleView extends FrameLayout implements Puzzle, View.OnTouchListener {

    private static final int VERTEX_VIEW_SIZE = VertexView.RADIUS * 2;
    private static final int EDGE_WIDTH = 4;
    private static final int EDGE_COLOR = 0xff222222;
    private static final int CROSS_EDGE_COLOR = 0xffff0000;
    private static final float MARGIN_PERCENTAGE = .2f;
    private static final float VERTEX_SCALE_PERCENTAGE = 1 - MARGIN_PERCENTAGE;
    private static final float VERTEX_OFFSET = MARGIN_PERCENTAGE / 4;

    private Untangle mUntangle;
    private Paint mLinePaint;
    private Paint mIntersectingLinePaint;
    private VertexView[] mVertexViews;
    private int mVertexColor;
    private PuzzleHost mPuzzleHost;
    private boolean notificationSolvedSent;

    @Override
    public void init(Difficulty difficulty) {
        generateGraph(difficulty);
    }

    @Override
    public void setPuzzleHost(PuzzleHost host) {
        this.mPuzzleHost = host;
    }

    @Override
    public boolean isSolved() {
        return mUntangle != null && mUntangle.isSolved();
    }

    public UntangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public UntangleView(Context context) {
        super(context, null);
        init(context, null);
    }

    public void init(Context context, AttributeSet attrs) {
        mVertexViews = new VertexView[Untangle.MAX_ITEMS];
        for (int i = 0; i < mVertexViews.length; i++) {
            mVertexViews[i] = null;
        }
        int edgeColor, crossEdgeColor;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UntangleView);
            mVertexColor = a.getColor(R.styleable.UntangleView_vertexColor, VertexView.VERTEX_COLOR);
            edgeColor = a.getColor(R.styleable.UntangleView_edgeColor, EDGE_COLOR);
            crossEdgeColor = a.getColor(R.styleable.UntangleView_crossEdgeColor, CROSS_EDGE_COLOR);
            a.recycle();
        }
        // for the cases when we create programmatically without AttributeSet
        else {
            mVertexColor = VertexView.VERTEX_COLOR;
            edgeColor = EDGE_COLOR;
            crossEdgeColor = CROSS_EDGE_COLOR;
        }
        mLinePaint = new Paint();
        mLinePaint.setColor(edgeColor);
        mLinePaint.setStrokeWidth(EDGE_WIDTH);
        mIntersectingLinePaint = new Paint();
        mIntersectingLinePaint.setColor(crossEdgeColor);
        mIntersectingLinePaint.setStrokeWidth(EDGE_WIDTH);
        this.setWillNotDraw(false);
    }

    public void generateGraph(Difficulty difficulty) {
        mUntangle = GraphReader.getGraph(getContext(), difficulty);
        mUntangle.shufflePositions();
        mUntangle.scaleVertexPositions(getWidth()*VERTEX_SCALE_PERCENTAGE,
                getHeight()*VERTEX_SCALE_PERCENTAGE);
        mUntangle.moveVertexPositions(getWidth()*VERTEX_OFFSET, getHeight()*VERTEX_OFFSET);
        for (CartesianVertex v: mUntangle.getVertices()) {
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
                break;
            case MotionEvent.ACTION_UP:
                checkSolution();
        }
        mPuzzleHost.onPuzzleTouched();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (IntersectingEdge e: mUntangle.getIntersectingEdges()) {
            drawEdge(canvas, e, true);
        }
        for (IntersectingEdge e: mUntangle.getNonIntersectingEdges()) {
            drawEdge(canvas, e, false);
        }
    }

    private void checkSolution() {
        if (mPuzzleHost == null)
            return;
        if (!notificationSolvedSent && mUntangle.isSolved()) {
            mPuzzleHost.onPuzzleSolved();
            notificationSolvedSent = true;
        }
        else if (notificationSolvedSent && !mUntangle.isSolved()) {
            mPuzzleHost.onPuzzleSolutionBroken();
            notificationSolvedSent = false;
        }
    }

    private void addVertex(CartesianVertex v) {
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

    private void drawEdge(Canvas c, IntersectingEdge e, boolean intersect) {
        CartesianVertex u = e.getFirst();
        CartesianVertex v = e.getSecond();
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
