package com.domain.nvm.morningfriend.features.puzzle;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.domain.nvm.morningfriend.features.alarm.Alarm;
import com.domain.nvm.morningfriend.features.alert.AlarmWakeLock;
import com.domain.nvm.morningfriend.features.alert.RingtonePlayer;
import com.domain.nvm.morningfriend.features.alert.scheduler.AlarmScheduler;
import com.domain.nvm.morningfriend.features.log.Logger;
import com.domain.nvm.morningfriend.features.puzzle.equation.views.EquationPuzzleView;
import com.domain.nvm.morningfriend.features.puzzle.labyrinth.views.LabyrinthView;
import com.domain.nvm.morningfriend.features.puzzle.squares.SquaresView;
import com.domain.nvm.morningfriend.features.puzzle.untangle.views.UntangleView;


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
                puzzleView = new UntangleView(this);
                break;
            case LABYRINTH:
                puzzleView = new LabyrinthView(this);
                break;
            case EQUATION:
                puzzleView = new EquationPuzzleView(this);
                break;
            case SQUARES:
            default:
                puzzleView = new SquaresView(this);
        }

        setContentView(puzzleView);
        mPuzzle = (Puzzle) puzzleView;
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
        Logger.write(this, "PuzzleActivity::onPause");
        // there is "fake" onPause call when dismissing keyguard and turning screen on
        if (!isScreenOn()) {
            return;
        }
        if (!mPuzzle.isSolved()) {
            repeatAlarm();
        }
        else {
            AlarmWakeLock.releaseLock(this);
        }
    }

    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= 20) {
            return powerManager.isInteractive();
        }
        else {
            return powerManager.isScreenOn();
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
