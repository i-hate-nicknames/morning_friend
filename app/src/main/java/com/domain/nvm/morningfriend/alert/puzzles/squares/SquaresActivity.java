package com.domain.nvm.morningfriend.alert.puzzles.squares;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewTreeObserver;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.Logger;
import com.domain.nvm.morningfriend.alert.PuzzleActivity;

import java.util.Date;

public class SquaresActivity extends PuzzleActivity implements SquaresView.SquareClickedListener {

    private SquaresView mView;
    private Alarm mAlarm;

    private static final String EXTRA_ALARM = "alarm";

    public static Intent newIntent(Context context, Alarm alarm) {
        Intent i = new Intent(context, SquaresActivity.class);
        i.putExtra(EXTRA_ALARM, alarm);
        return i;
    }

    @Override
    public Alarm getAlarm() {
        return mAlarm;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlarm = (Alarm) getIntent().getSerializableExtra(EXTRA_ALARM);
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
        stopAndRestartRinging(mAlarm);
        finish();
    }
}
