package com.domain.nvm.morningfriend;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.domain.nvm.morningfriend.alert.PuzzleActivity;
import com.domain.nvm.morningfriend.alert.RingingService;
import com.domain.nvm.morningfriend.scheduler.AlarmScheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity
        implements TimePickerFragment.TimePickerResultListener {

    private TextView mTimeTextView;
    private CheckBox mEnabledCheckBox;

    private Date mAlarmTime;

    private static final String TAG = "SettingsActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isRingingServiceRunning()) {
            Intent i = new Intent(this, PuzzleActivity.class);
            startActivity(i);
            finish();
        }
        setContentView(R.layout.activity_alarm_settings);

        mAlarmTime = AlarmScheduler.getAlarmTime(this);

        mTimeTextView = (TextView) findViewById(R.id.alarm_time_caption);
        mEnabledCheckBox = (CheckBox) findViewById(R.id.alarm_enabled_check_box);

        mEnabledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlarmScheduler.setEnabled(SettingsActivity.this, isChecked);
                AlarmScheduler.setRingingAlarm(SettingsActivity.this, mAlarmTime, isChecked);
                updateUI();
            }
        });

        mTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mgr = getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mAlarmTime);
                dialog.show(mgr, TAG);
            }
        });

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu_demo:
                Intent i = new Intent(this, PuzzleActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTimePickerResult(Date time) {
        AlarmScheduler.setAlarmTime(this, time);
        mAlarmTime = AlarmScheduler.getAlarmTime(this);
        updateUI();
    }

    private void updateUI() {
        mEnabledCheckBox.setChecked(AlarmScheduler.isEnabled(this));
        SimpleDateFormat format =
                new SimpleDateFormat("HH:mm, dd.MM", java.util.Locale.getDefault());
        mTimeTextView.setText(format.format(mAlarmTime));
    }

    private boolean isRingingServiceRunning() {
        ActivityManager mgr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service:
                mgr.getRunningServices(Integer.MAX_VALUE)) {
            if (RingingService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
