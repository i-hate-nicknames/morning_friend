package com.domain.nvm.morningfriend.ui.puzzle.squares;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.ui.puzzle.Puzzle;
import com.domain.nvm.morningfriend.ui.puzzle.PuzzleHost;

public class SquaresView extends View implements Puzzle {

    private Paint mRedPaint, mGreenPaint, mBackgroundPaint;
    private Square mRedSquare, mGreenSquare;
    private PointF mOriginLeft, mOriginRight;
    private boolean isSolved;

    private static final int SQUARE_SIZE = 200;

    private PuzzleHost mPuzzleHost;

    @Override
    public void init(Difficulty difficulty) {
        initSquares();
    }

    @Override
    public void setPuzzleHost(PuzzleHost host) {
        this.mPuzzleHost = host;
    }

    @Override
    public boolean isSolved() {
        return isSolved;
    }

    public SquaresView(Context context) {
        super(context, null);
        mRedPaint = new Paint();
        mRedPaint.setColor(0x88dd0000);
        mGreenPaint = new Paint();
        mGreenPaint.setColor(0x8800dd00);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe2);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mPuzzleHost == null) {
            return super.onTouchEvent(event);
        }
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                PointF click = new PointF(event.getX(), event.getY());
                PointF leftCurrent =
                        new PointF(mOriginLeft.x + SQUARE_SIZE, mOriginLeft.y + SQUARE_SIZE);
                PointF rightCurrent =
                        new PointF(mOriginRight.x + SQUARE_SIZE, mOriginRight.y + SQUARE_SIZE);
                if (inArea(click, mOriginLeft, leftCurrent)) {
                    isSolved = true;
                    mPuzzleHost.snooze();
                }
                if (inArea(click, mOriginRight, rightCurrent)) {
                    isSolved = true;
                    mPuzzleHost.onPuzzleSolved();
                }
        }
        return true;
    }

    public void initSquares() {
        int w = this.getWidth();
        int h = this.getHeight();
        int leftHalfCenterX = w / 4;
        int rightHalfCenterX = w - (w/4);
        int centerY = h / 2;
        mOriginLeft = new PointF(leftHalfCenterX - SQUARE_SIZE/2, centerY - SQUARE_SIZE/2);
        mOriginRight = new PointF(rightHalfCenterX - SQUARE_SIZE/2, centerY - SQUARE_SIZE/2);
        mGreenSquare = new Square(mOriginLeft, SQUARE_SIZE);
        mRedSquare = new Square(mOriginRight, SQUARE_SIZE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        drawSquare(canvas, mGreenSquare, mGreenPaint);
        drawSquare(canvas, mRedSquare, mRedPaint);
    }

    private void drawSquare(Canvas canvas, Square square, Paint paint) {
        float left = square.getPosition().x;
        float right = left + square.getSize();
        float top = square.getPosition().y;
        float bottom = top + square.getSize();
        canvas.drawRect(left, top, right, bottom, paint);
    }

    /**
     * true if point r is inside rectangle formed by points a and b
     */
    private boolean inArea(PointF r, PointF a, PointF b) {
        boolean inX = r.x <= Math.max(a.x, b.x) && r.x >= Math.min(a.x, b.x);
        boolean inY = r.y <= Math.max(a.y, b.y) && r.y >= Math.min(a.y, b.y);
        return inX && inY;
    }


}
