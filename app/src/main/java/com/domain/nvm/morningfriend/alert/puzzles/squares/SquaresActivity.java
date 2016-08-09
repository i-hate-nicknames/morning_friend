package com.domain.nvm.morningfriend.alert.puzzles.squares;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewTreeObserver;

import com.domain.nvm.morningfriend.Logger;
import com.domain.nvm.morningfriend.alert.PuzzleActivity;

import java.util.Date;

public class SquaresActivity extends PuzzleActivity implements SquaresView.SquareClickedListener {

    private SquaresView mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.write(this, "SquaresActivity::onCreate");
        mView = new SquaresView(this);
        mView.setCallback(this);
        mView.getViewTreeObserver()
            .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mView.initSquares();
                    mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        setContentView(mView);
    }

    @Override
    public void onRedClicked() {
        stopRinging();
        finish();
    }

    @Override
    public void onGreenClicked() {
        stopAndRestartRinging();
        finish();
    }
}
