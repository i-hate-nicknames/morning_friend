package com.domain.nvm.morningfriend.ui.puzzle.squares;

import android.graphics.PointF;

public class Square {

    private PointF position;
    private int size;

    public Square(PointF position, int size) {
        this.position = position;
        this.size = size;
    }

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        this.position = position;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
