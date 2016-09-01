package com.domain.nvm.morningfriend.alert.puzzles.untangle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.ViewTreeObserver;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.Logger;
import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.alert.PuzzleActivity;


public class UntangleActivity extends PuzzleActivity implements UntangleField.Callbacks {

    private static final String EXTRA_ALARM = "alarm";
    private static final int USER_INTERACTION_CHECK_FREQUENCY = 250;

    private UntangleField mField;
    private Alarm mAlarm;

    private boolean hasUserInteracted;

    private final Handler handler = new Handler();

    public void checkUserInteracted() {
        if (mService != null) {
            // service might not be connected yet
            if (!hasUserInteracted) {
                mService.increaseVolume();
            } else {
                mService.decreaseVolume();
            }
        }
        hasUserInteracted = false;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUserInteracted();
            }
        }, USER_INTERACTION_CHECK_FREQUENCY);
    }

    public static Intent newIntent(Context context, Alarm alarm) {
        Intent i = new Intent(context, UntangleActivity.class);
        i.putExtra(EXTRA_ALARM, alarm);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.write(this, "UntangleActivity::onCreate");
        setContentView(R.layout.activity_untangle);
        mAlarm = (Alarm) getIntent().getSerializableExtra(EXTRA_ALARM);
        mField = (UntangleField) findViewById(R.id.untangle_field);
        mField.setCallbacks(this);
        mField.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mField.generateGraph(mAlarm.getDifficulty());
                        mField.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
        showMuteMessage(mAlarm.getMessage());
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserInteracted();
    }

    @Override
    public void onGraphSolved() {
        stopRinging();
    }

    @Override
    public void onSolutionBroken() {
        // TODO: restart ringing
    }

    @Override
    public void onPuzzleTouched() {
        hasUserInteracted = true;
    }
}
