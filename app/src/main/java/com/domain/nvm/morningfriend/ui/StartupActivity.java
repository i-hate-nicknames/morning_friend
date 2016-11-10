package com.domain.nvm.morningfriend.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.alert.RingingState;
import com.domain.nvm.morningfriend.ui.alarm_list.AlarmListActivity;
import com.domain.nvm.morningfriend.ui.puzzle.PuzzleActivity;

public class StartupActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Alarm alarm = RingingState.getAlarm(this);
        if (alarm != null) {
            Intent puzzle = PuzzleActivity.makeIntent(this, alarm);
            puzzle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(puzzle);
        }
        else {
            startActivity(new Intent(this, AlarmListActivity.class));
        }
        finish();
    }
}
