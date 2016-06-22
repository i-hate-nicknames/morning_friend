package com.domain.nvm.morningfriend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmSettingsFragment extends Fragment {

    private TextView mTimeTextView;
    private CheckBox mEnabledCheckBox;

    private Date mAlarmTime;

    private static final String TAG = "AlarmSettingsFragment";
    private static final int REQ_TIME = 0;
    private static final String EXTRA_TIME = "time";

    public static AlarmSettingsFragment createFragment() {
        return new AlarmSettingsFragment();
    }

    public static Intent makeTimeIntent(Date time) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlarmTime = AlarmPreferences.getAlarmTime(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alarm_settings, container, false);

        mTimeTextView = (TextView) v.findViewById(R.id.alarm_time_caption);
        mEnabledCheckBox = (CheckBox) v.findViewById(R.id.alarm_enabled_check_box);

        mEnabledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlarmPreferences.setEnabled(getActivity(), isChecked);
                RingingActivity.setRingingAlarm(getActivity(), mAlarmTime, isChecked);
                updateUI();
            }
        });

        mTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mgr = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mAlarmTime);
                dialog.setTargetFragment(AlarmSettingsFragment.this, REQ_TIME);
                dialog.show(mgr, TAG);
            }
        });

        updateUI();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_TIME && resultCode == Activity.RESULT_OK) {
            mAlarmTime = (Date) data.getSerializableExtra(EXTRA_TIME);
            AlarmPreferences.setAlarmTime(getActivity(), mAlarmTime);
            updateUI();
        }
    }

    private void updateUI() {
        mEnabledCheckBox.setChecked(AlarmPreferences.isEnabled(getActivity()));
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
        mTimeTextView.setText(format.format(mAlarmTime));
    }
}
