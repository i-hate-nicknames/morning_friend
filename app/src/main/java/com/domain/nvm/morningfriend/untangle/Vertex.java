package com.domain.nvm.morningfriend.untangle;

import android.graphics.PointF;

public class Vertex {

    private PointF position;
    private int mNum;

    public Vertex(int num, PointF position) {
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

    public void swapX(Vertex other) {
        float tmp = getX();
        setX(other.getX());
        other.setX(tmp);
    }

    public void swapY(Vertex other) {
        float tmp = getY();
        setY(other.getY());
        other.setY(tmp);
    }

    public void setPosition(PointF position) {
        this.position = position;
    }

    public int getNum() {
        return mNum;
    }


}
