package com.domain.nvm.morningfriend.squares;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

public class SquaresView extends View {

    private Paint mRedPaint, mGreenPaint, mBackgroundPaint;
    private Square mRedSquare, mGreenSquare;

    private static final int SQUARE_SIZE = 200;

    public SquaresView(Context context) {
        super(context, null);
        mRedPaint = new Paint();
        mRedPaint.setColor(0x88dd0000);
        mGreenPaint = new Paint();
        mGreenPaint.setColor(0x8800dd00);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe2);
    }

    public void initSquares() {
        int w = this.getWidth();
        int h = this.getHeight();
        int leftHalfCenterX = w / 4;
        int rightHalfCenterX = w - (w/4);
        int centerY = h / 2;
        PointF originLeft =
                new PointF(leftHalfCenterX - SQUARE_SIZE/2, centerY - SQUARE_SIZE/2);
        PointF originRight =
                new PointF(rightHalfCenterX - SQUARE_SIZE/2, centerY - SQUARE_SIZE/2);
        mGreenSquare = new Square(originLeft, SQUARE_SIZE);
        mRedSquare = new Square(originRight, SQUARE_SIZE);
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
}
