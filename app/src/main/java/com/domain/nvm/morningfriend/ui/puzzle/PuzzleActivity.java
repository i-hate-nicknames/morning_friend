package com.domain.nvm.morningfriend.ui.puzzle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.alert.AlarmWakeLock;
import com.domain.nvm.morningfriend.alert.RingtonePlayer;
import com.domain.nvm.morningfriend.alert.scheduler.AlarmScheduler;
import com.domain.nvm.morningfriend.ui.logs.Logger;
import com.domain.nvm.morningfriend.ui.puzzle.squares.SquaresView;
import com.domain.nvm.morningfriend.ui.puzzle.untangle.UntangleField;


public class PuzzleActivity extends AppCompatActivity implements PuzzleHost {

    private static final int USER_INTERACTION_CHECK_FREQUENCY = 250;
    private static final String EXTRA_ALARM = "alarm";

    private final Handler handler = new Handler();

    private RingtonePlayer mPlayer;
    private boolean mBound = false;
    private boolean isSolved = false;
    private Puzzle mPuzzle;
    private Alarm mAlarm;


    public static Intent makeIntent(Context context, Alarm alarm) {
        Intent i = new Intent(context, PuzzleActivity.class);
        i.putExtra(EXTRA_ALARM, alarm);
        return i;
    }

    @Override
    public void onPuzzleSolved() {
        mPlayer.stop();
        AlarmScheduler.setNextAlarm(this);
        finish();
    }

    @Override
    public void onPuzzleSolutionBroken() {

    }

    @Override
    public void onPuzzleTouched() {

    }

    @Override
    public void snooze() {
        mPlayer.stop();
        AlarmScheduler.snooze(this, mAlarm);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.write(this, "PuzzleActivity::onCreate");
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
        mPlayer = new RingtonePlayer(this);
        mPlayer.play();
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
    protected void onPause() {
        super.onPause();
        if (!mPuzzle.isSolved()) {
            repeatAlarm();
        }
        else {
            AlarmWakeLock.releaseLock(this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPuzzle.isSolved()) {
            return super.onKeyDown(keyCode, event);
        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) || (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
                || (keyCode == KeyEvent.KEYCODE_POWER)){
            repeatAlarm();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Close current puzzle and register same alarm to be fired soon
     */
    private void repeatAlarm() {
        mPlayer.stop();
        AlarmScheduler.puzzleInterruptedSnooze(this, mAlarm);
        finish();
    }

}
