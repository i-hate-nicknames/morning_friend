package com.domain.nvm.morningfriend.features.puzzle.labyrinth.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.domain.nvm.morningfriend.features.puzzle.Puzzle;
import com.domain.nvm.morningfriend.features.puzzle.PuzzleHost;

public class LabyrinthView extends View implements Puzzle {

    private static final int OFFSET = 25;

    private Bitmap labyrinthImg;
    private PuzzleHost puzzleHost;
    private int width, height, top, left;

    @Override
    public void init(Difficulty difficulty) {
        width = getWidth() - OFFSET*2;
        height = getHeight() - OFFSET*2;
        top = OFFSET;
        left = OFFSET;
        generateImage();
    }

    @Override
    public void setPuzzleHost(PuzzleHost host) {
        puzzleHost = host;
    }

    @Override
    public boolean isSolved() {
        return false;
    }

    public LabyrinthView(Context context) {
        super(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(labyrinthImg, left, top, null);
        // draw player position

    }

    private void generateImage() {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        labyrinthImg = Bitmap.createBitmap(width, height, conf);
        Canvas c = new Canvas();
        c.setBitmap(labyrinthImg);
        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(2);
        c.drawLine(0, 0, 200, 200, linePaint);
    }
}
