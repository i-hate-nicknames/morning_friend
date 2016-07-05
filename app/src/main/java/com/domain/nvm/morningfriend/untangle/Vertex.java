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

    public void setPosition(PointF position) {
        this.position = position;
    }

    public int getNum() {
        return mNum;
    }


}
