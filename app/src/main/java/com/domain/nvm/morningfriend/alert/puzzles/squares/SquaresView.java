package com.domain.nvm.morningfriend.alert.puzzles.squares;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

public class SquaresView extends View {

    private Paint mRedPaint, mGreenPaint, mBackgroundPaint;
    private Square mRedSquare, mGreenSquare;
    private PointF mOriginLeft, mOriginRight;

    private static final int SQUARE_SIZE = 200;

    private SquareClickedListener mCallback;

    public interface SquareClickedListener {
        void onRedClicked();
        void onGreenClicked();
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

    public void setCallback(SquareClickedListener callback) {
        mCallback = callback;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCallback == null) {
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
                    mCallback.onGreenClicked();
                }
                if (inArea(click, mOriginRight, rightCurrent)) {
                    mCallback.onRedClicked();
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
