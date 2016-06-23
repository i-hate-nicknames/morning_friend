package com.domain.nvm.morningfriend.squares;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

public class SquaresView extends View {

    private Paint mRedPaint, mGreenPaint, mBackgroundPaint;
    private Square mSquare;

    public SquaresView(Context context) {
        super(context, null);
        mRedPaint = new Paint();
        mRedPaint.setColor(0x22ff0000);
        mGreenPaint = new Paint();
        mGreenPaint.setColor(0x2200ff00);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe2);
        initSquares();
    }

    private void initSquares() {

        mSquare = new Square(new PointF(100, 100), 200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        float left = mSquare.getPosition().x;
        float right = left + mSquare.getSize();
        float top = mSquare.getPosition().y;
        float bottom = top + mSquare.getSize();
        canvas.drawRect(left, top, right, bottom, mGreenPaint);
    }
}
