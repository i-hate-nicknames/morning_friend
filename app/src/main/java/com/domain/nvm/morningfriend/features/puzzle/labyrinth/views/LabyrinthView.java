package com.domain.nvm.morningfriend.features.puzzle.labyrinth.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.domain.nvm.morningfriend.features.puzzle.Puzzle;
import com.domain.nvm.morningfriend.features.puzzle.PuzzleHost;
import com.domain.nvm.morningfriend.features.puzzle.labyrinth.data.Labyrinth;

import static com.domain.nvm.morningfriend.features.puzzle.labyrinth.data.Labyrinth.*;

public class LabyrinthView extends View implements Puzzle, View.OnTouchListener {

    private static final int PUZZLE_OFFSET = 50;

    private Bitmap labyrinthImg;
    private Labyrinth labyrinth;
    private PuzzleHost puzzleHost;
    private Paint playerPaint;
    // coordinates of labyrinth's origin
    private int puzzleX, puzzleY;
    private int tileSize;
    private float aspectRatio;

    @Override
    public void init(Difficulty difficulty) {
        int mainWidth = getWidth();
        int mainHeight = getHeight();
        aspectRatio = (float) mainWidth / mainHeight;
        labyrinth = generateLabyrinth();
        int puzzleWidth = mainWidth - PUZZLE_OFFSET*2;
        puzzleX = PUZZLE_OFFSET;
        puzzleY = (mainHeight - puzzleWidth) / 2;
        labyrinthImg = generateImage(puzzleWidth, labyrinth);
        playerPaint = new Paint();
        playerPaint.setColor(Color.RED);
        setOnTouchListener(this);
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
        canvas.drawBitmap(labyrinthImg, puzzleX, puzzleY, null);
        drawPlayer(canvas);
    }

    private Labyrinth generateLabyrinth() {
        return new Labyrinth(10);
    }

    private Bitmap generateImage(int width, Labyrinth lab) {
        tileSize = width / labyrinth.getSize();
        int wallWidth = (tileSize < 10) ? 1 : tileSize / 10;
        // coordinates of leftmost topmost tile
        int originX = wallWidth, originY = wallWidth;
        int canvasWidth = width+wallWidth, canvasHeight = width+wallWidth;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap image = Bitmap.createBitmap(canvasWidth, canvasHeight, conf);
        Canvas c = new Canvas();
        c.setBitmap(image);
        Paint wallPaint = new Paint();
        wallPaint.setColor(Color.BLACK);
        Paint tilePaint = new Paint();
        tilePaint.setColor(Color.LTGRAY);

        c.drawPaint(tilePaint);
        // draw top and left walls outside of tiles
        // todo: draw the entrance to the labyrinth
        c.drawRect(0, 0, canvasWidth, wallWidth, wallPaint);
        c.drawRect(0, 0, wallWidth, canvasHeight, wallPaint);
        // draw all right and bottom walls so that they would occupy tiles' space
        for (int i = 0; i < labyrinth.getSize(); i++) {
            for (int j = 0; j < labyrinth.getSize(); j++) {
                int x1 = originX + tileSize * i;
                int y1 = originY + tileSize * j;
                int x2 = x1 + tileSize;
                int y2 = y1 + tileSize;
                if (!labyrinth.canMove(i, j, Direction.RIGHT)) {
                    c.drawRect(x2-wallWidth, y1-wallWidth, x2, y2, wallPaint);
                }
                if (!labyrinth.canMove(i, j, Direction.DOWN)) {
                    c.drawRect(x1-wallWidth, y2-wallWidth, x2, y2, wallPaint);
                }
            }
        }
        return image;
    }

    private void drawPlayer(Canvas c) {
        int tile = labyrinth.getPlayerTile();
        int tileOriginX = puzzleX + labyrinth.getTileCol(tile) * tileSize;
        int tileOriginY = puzzleY + labyrinth.getTileRow(tile) * tileSize;
        int x = tileOriginX + tileSize/2, y = tileOriginY + tileSize/2;
        c.drawCircle(x, y, tileSize/4, playerPaint);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Direction move = getClickDirection(event.getX(), event.getY());
        }
        return false;
    }

    private Direction getClickDirection(float clickX, float clickY) {
        // find Y coordinate of a point on the "main" diagonal with the same X coordinate as clicked
        int mainY = (int) Math.floor(clickX / aspectRatio);
        // same for the "anti" diagonal
        int antiY = getHeight() - mainY;
        if (clickY < mainY) {
            if (clickY < antiY) {
                return Direction.UP;
            }
            else {
                return Direction.RIGHT;
            }
        }
        else {
            if (clickY < antiY) {
                return Direction.LEFT;
            }
            else {
                return Direction.DOWN;
            }
        }
    }

}
