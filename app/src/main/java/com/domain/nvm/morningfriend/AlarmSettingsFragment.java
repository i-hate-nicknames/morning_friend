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
    private Button mStartRinging;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alarm_settings, container, false);

        mTimeTextView = (TextView) v.findViewById(R.id.alarm_time_caption);
        mEnabledCheckBox = (CheckBox) v.findViewById(R.id.alarm_enabled_check_box);
        mStartRinging = (Button) v.findViewById(R.id.button_open_ringing);

        mStartRinging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RingingActivity.class));
            }
        });

        mEnabledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlarmPreferences.setEnabled(getActivity(), isChecked);
                updateUI();
            }
        });

        mTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mgr = getFragmentManager();
                long time = AlarmPreferences.getAlarmTime(getActivity());
                TimePickerFragment dialog = TimePickerFragment.newInstance(new Date(time));
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
            Date time = (Date) data.getSerializableExtra(EXTRA_TIME);
            AlarmPreferences.setAlarmTime(getActivity(), time.getTime());
            updateUI();
        }
    }

    private void updateUI() {
        mEnabledCheckBox.setChecked(AlarmPreferences.isEnabled(getActivity()));
        Date time = new Date(AlarmPreferences.getAlarmTime(getActivity()));
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
        mTimeTextView.setText(format.format(time));
    }
}
