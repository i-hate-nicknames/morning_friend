package com.domain.nvm.morningfriend.ui.puzzle;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.alert.AlarmWakeLock;
import com.domain.nvm.morningfriend.alert.RingingService;
import com.domain.nvm.morningfriend.alert.RingingState;
import com.domain.nvm.morningfriend.alert.scheduler.AlarmScheduler;
import com.domain.nvm.morningfriend.ui.puzzle.squares.SquaresView;
import com.domain.nvm.morningfriend.ui.puzzle.untangle.UntangleField;


public class PuzzleActivity extends AppCompatActivity implements PuzzleHost {

    private static final int USER_INTERACTION_CHECK_FREQUENCY = 250;
    private static final String EXTRA_ALARM = "alarm";

    private final Handler handler = new Handler();

    private boolean hasUserInteracted;
    private RingingService mService;
    private boolean mBound = false;
    private boolean isSolved = false;
    private Puzzle mPuzzle;
    private Alarm mAlarm;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RingingService.RingingBinder binder = (RingingService.RingingBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public static Intent makeIntent(Context context, Alarm alarm) {
        Intent i = new Intent(context, PuzzleActivity.class);
        i.putExtra(EXTRA_ALARM, alarm);
        return i;
    }

    @Override
    public void onPuzzleSolved() {
        mService.stopPlaying();
        AlarmScheduler.setNextAlarm(this);
        RingingState.removeAlarm(this);
        finish();
    }

    @Override
    public void onPuzzleSolutionBroken() {

    }

    @Override
    public void onPuzzleTouched() {
        hasUserInteracted = true;
    }

    @Override
    public void snooze() {
        mService.stopPlaying();
        AlarmScheduler.snooze(this, mAlarm);
        RingingState.removeAlarm(this);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // might be null if alarm is ringing and app was started from launcher
        mAlarm = (Alarm) getIntent().getSerializableExtra(EXTRA_ALARM);
        AlarmWakeLock.acquireLock(this);
        final Window win = getWindow();
        win.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        Intent serviceIntent;
        serviceIntent = new Intent(this, RingingService.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        // this won't create another instance of service, but will call onStartCommand
        startService(serviceIntent);

        final View puzzleView;
        switch (mAlarm.getPuzzleType()) {
            case GRAPH:
                puzzleView = new UntangleField(this);
                mPuzzle = (UntangleField) puzzleView;
                break;
            case SQUARES:
            default:
                puzzleView = new SquaresView(this);
                mPuzzle = (SquaresView) puzzleView;
                break;
        }

        setContentView(puzzleView);
        mPuzzle.setPuzzleHost(this);
        puzzleView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mPuzzle.init(mAlarm.getDifficulty());
                        puzzleView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

    }


    @Override
    protected void onStart() {
        super.onStart();
        checkUserInteracted();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mPuzzle.isSolved()) {
            RingingState.saveAlarm(this, mAlarm);
        }
    }

    public void showMuteMessage(String message) {
        if (message == null) {
            message = getString(R.string.snackbar_mute_message);
        }
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.snackbar_mute_button_text),
                        new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mService.muteSound();
                    }
                })
                .show();
    }

    private void checkUserInteracted() {
        if (mService != null) {
            // service might not be connected yet
            mService.maximizeSystemVolume();
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

}
