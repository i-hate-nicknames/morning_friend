package com.domain.nvm.morningfriend.features.alarm.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.domain.nvm.morningfriend.features.alarm.Alarm;
import com.domain.nvm.morningfriend.features.alarm.Alarm.Days.Names;
import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.DateTimeUtils;
import com.domain.nvm.morningfriend.database.AlarmRepository;

import java.util.Date;

public class AlarmDetailActivity extends AppCompatActivity {


    private Alarm mAlarm;

    private static final String TAG = "AlarmDetailActivity";
    private static final String EXTRA_ALARM_ID = "extraAlarmId";


    public static Intent makeIntent(Context context, Alarm alarm) {
        Intent i = new Intent(context, AlarmDetailActivity.class);
        i.putExtra(EXTRA_ALARM_ID, alarm.getId());
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int alarmId = getIntent().getIntExtra(EXTRA_ALARM_ID, -1);
        mAlarm = AlarmRepository.get(this).getAlarm(alarmId);
        setContentView(R.layout.single_fragment_activity);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = AlarmDetailFragment.makeFragment(mAlarm);
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detail_menu_delete:
                AlarmRepository.get(this).deleteAlarm(mAlarm);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
