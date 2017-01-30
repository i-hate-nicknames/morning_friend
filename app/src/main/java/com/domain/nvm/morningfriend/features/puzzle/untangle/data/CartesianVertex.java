package com.domain.nvm.morningfriend.features.puzzle.untangle.data;

import android.graphics.PointF;

import com.domain.nvm.morningfriend.features.puzzle.data.Vertex;

public class CartesianVertex implements Vertex {

    private PointF position;
    private int mNum;

    public CartesianVertex(int num, PointF position) {
        mNum = num;
        this.position = position;
    }

    public PointF getPosition() {
        return position;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void setPosition(float x, float y) {
        setPosition(new PointF(x, y));
    }

    public void setX(float x) {
        setPosition(new PointF(x, position.y));
    }

    public void setY(float y) {
        setPosition(new PointF(position.x, y));
    }

    public void swapX(CartesianVertex other) {
        float tmp = getX();
        setX(other.getX());
        other.setX(tmp);
    }

    public void swapY(CartesianVertex other) {
        float tmp = getY();
        setY(other.getY());
        other.setY(tmp);
    }

    public void setPosition(PointF position) {
        this.position = position;
    }

    @Override
    public int getNum() {
        return mNum;
    }


}
