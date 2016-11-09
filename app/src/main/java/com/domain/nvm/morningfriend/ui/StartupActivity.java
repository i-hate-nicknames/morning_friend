package com.domain.nvm.morningfriend.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.alert.RingingState;
import com.domain.nvm.morningfriend.ui.alarm_list.AlarmListActivity;
import com.domain.nvm.morningfriend.ui.puzzle.squares.SquaresActivity;
import com.domain.nvm.morningfriend.ui.puzzle.untangle.UntangleActivity;

public class StartupActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Alarm alarm = RingingState.getAlarm(this);
        if (alarm != null) {
            Intent puzzle;
            switch (alarm.getPuzzle()) {
                case GRAPH:
                    puzzle = UntangleActivity.newIntent(this, alarm);
                    break;
                default:
                case SQUARES:
                    puzzle = SquaresActivity.newIntent(this, alarm);
                    break;
            }
            startActivity(puzzle);
        }
        else {
            startActivity(new Intent(this, AlarmListActivity.class));
        }
        finish();
    }
}
