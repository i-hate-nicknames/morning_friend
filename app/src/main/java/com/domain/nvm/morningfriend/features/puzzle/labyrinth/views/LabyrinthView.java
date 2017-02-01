package com.domain.nvm.morningfriend.features.puzzle.labyrinth.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.domain.nvm.morningfriend.features.puzzle.Puzzle;
import com.domain.nvm.morningfriend.features.puzzle.PuzzleHost;
import com.domain.nvm.morningfriend.features.puzzle.labyrinth.data.Labyrinth;

public class LabyrinthView extends View implements Puzzle {

    private Bitmap labyrinthImg;
    private Labyrinth labyrinth;
    private PuzzleHost puzzleHost;
    private int width, height, tileSize, wallWidth;

    @Override
    public void init(Difficulty difficulty) {
        width = getWidth();
        height = getHeight();
        generateLabyrinth();
        tileSize = width / labyrinth.getSize();
        wallWidth = (tileSize < 10) ? 1 : tileSize / 10;
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
        canvas.drawBitmap(labyrinthImg, 0, 0, null);
        // draw player position
    }

    private void generateLabyrinth() {
        labyrinth = new Labyrinth(5);
    }

    private void generateImage() {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        labyrinthImg = Bitmap.createBitmap(width, height, conf);
        Canvas c = new Canvas();
        c.setBitmap(labyrinthImg);
        Paint wallPaint = new Paint();
        wallPaint.setColor(Color.BLACK);
        wallPaint.setStrokeWidth(wallWidth);
        Paint tilePaint = new Paint();
        tilePaint.setColor(Color.LTGRAY);
        c.drawPaint(tilePaint);
        int wallOffset = wallWidth/2;
        // draw top and left walls
        c.drawLine(0, 0, width, 0, wallPaint);
        c.drawLine(0, 0, 0, height, wallPaint);
        for (int i = 0; i < labyrinth.getSize(); i++) {
            for (int j = 0; j < labyrinth.getSize(); j++) {
                int x1 = tileSize * i;
                int y1 = tileSize * j;
                int x2 = x1 + tileSize;
                int y2 = y1 + tileSize;
                if (!labyrinth.canMove(i, j, Labyrinth.Direction.RIGHT)) {
                    c.drawLine(x1-wallOffset, y2, x2+wallOffset, y2, wallPaint);
                }
                if (!labyrinth.canMove(i, j, Labyrinth.Direction.DOWN)) {
                    c.drawLine(x2, y1-wallOffset, x2, y2+wallOffset, wallPaint);
                }
            }
        }
    }
}
