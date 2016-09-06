package com.domain.nvm.morningfriend.ui.alarm_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.Alarm.Days.Names;
import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.Utils;
import com.domain.nvm.morningfriend.database.AlarmRepository;

import java.util.Date;

public class AlarmDetailActivity extends AppCompatActivity
        implements TimePickerFragment.TimePickerResultListener {

    private TextView mTimeTextView;
    private CheckBox mEnabledCheckBox;
    private Spinner mDifficulty;
    private Spinner mPuzzle;
    private EditText mMessageEdit;
    private CheckBox[] mDayCheckBoxes;

    private Alarm mAlarm;

    private static final String TAG = "AlarmDetailActivity";
    private static final String EXTRA_ALARM_ID = "extraAlarmId";

    private int[] checkBoxIds = new int[] {R.id.details_repeating_days_sun_check_box,
            R.id.details_repeating_days_mon_check_box, R.id.details_repeating_days_tue_check_box,
            R.id.details_repeating_days_wed_check_box, R.id.details_repeating_days_thu_check_box,
            R.id.details_repeating_days_fri_check_box, R.id.details_repeating_days_sat_check_box};

    public static Intent makeIntent(Context context, Alarm alarm) {
        Intent i = new Intent(context, AlarmDetailActivity.class);
        i.putExtra(EXTRA_ALARM_ID, alarm.getId());
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm_detail);
        int alarmId = getIntent().getIntExtra(EXTRA_ALARM_ID, -1);
        mAlarm = AlarmRepository.get(this).getAlarm(alarmId);

        mTimeTextView = (TextView) findViewById(R.id.alarm_detail_alarm_time_caption);
        mEnabledCheckBox = (CheckBox) findViewById(R.id.alarm_detail_enabled_check_box);
        mMessageEdit = (EditText) findViewById(R.id.alarm_detail_alarm_message_edit_text);
        mDifficulty = (Spinner) findViewById(R.id.alarm_detail_spinner_difficulty);
        mPuzzle = (Spinner) findViewById(R.id.alarm_detail_spinner_puzzle);

        mDayCheckBoxes = new CheckBox[7];
        for (int i = 0; i < checkBoxIds.length; i++) {
            mDayCheckBoxes[i] = (CheckBox) findViewById(checkBoxIds[i]);
        }

        String[] choices = getResources().getStringArray(R.array.pref_difficulty);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, choices);
        mDifficulty.setAdapter(adapter);

        choices = getResources().getStringArray(R.array.pref_puzzle);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, choices);
        mPuzzle.setAdapter(adapter);

        mEnabledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlarm.setEnabled(isChecked);
                updateAlarmWithUI();
            }
        });

        mTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mgr = getSupportFragmentManager();
                TimePickerFragment dialog =
                        TimePickerFragment.newInstance(mAlarm.getHour(), mAlarm.getMinute());
                dialog.show(mgr, TAG);
            }
        });

        mDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAlarm.setDifficulty(position);
                updateAlarmWithUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPuzzle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAlarm.setPuzzle(position);
                updateAlarmWithUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMessageEdit.setText(mAlarm.getMessage());
        mMessageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAlarm.setMessage(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateAlarm();
            }
        });
        updateUI();
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

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) {
            return;
        }
        switch (view.getId()) {
            case R.id.alarm_detail_days_workdays_radio_button:
                mAlarm.getRepeatDays().setOnlyWorkDays();
                updateAlarm();
                updateDaysViews(false);
                break;
            case R.id.alarm_detail_days_weekends_radio_button:
                mAlarm.getRepeatDays().setOnlyWeekends();
                updateAlarm();
                updateDaysViews(false);
                break;
            default:
                updateDaysViews(true);
        }

    }

    public void onCheckBoxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.details_repeating_days_sun_check_box:
                setAlarmRepeatDay(Names.SUN, checked);
                break;
            case R.id.details_repeating_days_mon_check_box:
                setAlarmRepeatDay(Names.MON, checked);
                break;
            case R.id.details_repeating_days_tue_check_box:
                setAlarmRepeatDay(Names.TUE, checked);
                break;
            case R.id.details_repeating_days_wed_check_box:
                setAlarmRepeatDay(Names.WED, checked);
                break;
            case R.id.details_repeating_days_thu_check_box:
                setAlarmRepeatDay(Names.THU, checked);
                break;
            case R.id.details_repeating_days_fri_check_box:
                setAlarmRepeatDay(Names.FRI, checked);
                break;
            case R.id.details_repeating_days_sat_check_box:
                setAlarmRepeatDay(Names.SAT, checked);
                break;
        }
    }

    private void setAlarmRepeatDay(Names dayName, boolean isOn) {
        mAlarm.getRepeatDays().setDay(dayName, isOn);
        updateAlarm();
        updateDaysViews(true);
    }

    @Override
    public void onTimePickerResult(Date time) {
        mAlarm.setHour(Utils.getHour(time));
        mAlarm.setMinute(Utils.getMinute(time));
        updateAlarmWithUI();
    }

    private void updateAlarm() {
        AlarmRepository.get(this).updateAlarm(mAlarm);
    }

    private void updateAlarmWithUI() {
        updateAlarm();
        updateUI();
    }

    private void updateUI() {
        mEnabledCheckBox.setChecked(mAlarm.isEnabled());
        mTimeTextView.setText(Utils.formatTime(mAlarm.getHour(), mAlarm.getMinute()));
        mDifficulty.setSelection(mAlarm.getDifficulty().ordinal());
        mPuzzle.setSelection(mAlarm.getPuzzle().ordinal());
        updateDaysViews(false);
    }

    private void updateDaysViews(boolean enableCheckBoxes) {
        RadioGroup daysRadioGroup = (RadioGroup) findViewById(R.id.alarm_detail_days_radio_group);
        if (mAlarm.getRepeatDays().isOnlyWorkDays()) {
            daysRadioGroup.check(R.id.alarm_detail_days_workdays_radio_button);
            for (int dayCode = 0; dayCode < mDayCheckBoxes.length; dayCode++) {
                mDayCheckBoxes[dayCode].setChecked(Alarm.Days.isDayWorkday(dayCode));
                mDayCheckBoxes[dayCode].setEnabled(enableCheckBoxes);
            }

        }
        else if (mAlarm.getRepeatDays().isOnlyWeekends()) {
            daysRadioGroup.check(R.id.alarm_detail_days_weekends_radio_button);
            for (int dayCode = 0; dayCode < mDayCheckBoxes.length; dayCode++) {
                mDayCheckBoxes[dayCode].setChecked(Alarm.Days.isDayWeekend(dayCode));
                mDayCheckBoxes[dayCode].setEnabled(enableCheckBoxes);
            }
        }
        else {
            daysRadioGroup.check(R.id.alarm_detail_days_custom_radio_button);
            for (int dayCode = 0; dayCode < mDayCheckBoxes.length; dayCode++) {
                mDayCheckBoxes[dayCode]
                        .setChecked(mAlarm.getRepeatDays().isDayActive(Names.values()[dayCode]));
            }
        }

    }

}
