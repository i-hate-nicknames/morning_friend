package com.domain.nvm.morningfriend;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.domain.nvm.morningfriend.alert.RingingService;
import com.domain.nvm.morningfriend.alert.puzzles.squares.SquaresActivity;
import com.domain.nvm.morningfriend.scheduler.AlarmSettings;

import java.util.Date;

public class AlarmDetailActivity extends AppCompatActivity
        implements TimePickerFragment.TimePickerResultListener {

    private TextView mTimeTextView;
    private CheckBox mEnabledCheckBox;
    private Spinner mDifficulty;
    private Alarm mAlarm;

    private static final String TAG = "AlarmDetailActivity";
    private static final String EXTRA_ALARM = "extraAlarm";

    public static Intent makeIntent(Context context, Alarm alarm) {
        Intent i = new Intent(context, AlarmDetailActivity.class);
        i.putExtra(EXTRA_ALARM, alarm);
        return i;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm_settings);
        mAlarm = (Alarm) getIntent().getSerializableExtra(EXTRA_ALARM);

        mTimeTextView = (TextView) findViewById(R.id.alarm_time_caption);
        mEnabledCheckBox = (CheckBox) findViewById(R.id.alarm_enabled_check_box);
        mDifficulty = (Spinner) findViewById(R.id.settings_spinner_difficulty);

        String[] choices = getResources().getStringArray(R.array.pref_difficulty);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, choices);
        mDifficulty.setAdapter(adapter);

        mEnabledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateUI();
            }
        });

        mTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mgr = getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mAlarm.getTime());
                dialog.show(mgr, TAG);
            }
        });

        mDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAlarm.setDifficulty(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                Intent i = new Intent(this, SquaresActivity.class);
                startActivity(i);
                return true;
            case R.id.settings_menu_logs:
                Intent log = new Intent(this, LogActivity.class);
                startActivity(log);
                return true;
            case R.id.settings_menu_clear:
                Logger.clear(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTimePickerResult(Date time) {
        mAlarm.setTime(time);
        updateUI();
    }

    private void updateUI() {
        mEnabledCheckBox.setChecked(mAlarm.isEnabled());
        mTimeTextView.setText(AlarmSettings.formatDate(mAlarm.getTime()));
        mDifficulty.setSelection(mAlarm.getDifficulty().ordinal());
    }

}
