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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class AlarmFragment extends Fragment {

    private TextView mTimeTextView;
    private CheckBox mEnabledCheckBox;

    private static final String TAG = "AlarmFragment";
    private static final int REQ_TIME = 0;
    private static final String EXTRA_TIME = "time";

    public static AlarmFragment createFragment() {
        return new AlarmFragment();
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
        View v = inflater.inflate(R.layout.fragment_alarm, container, false);

        mTimeTextView = (TextView) v.findViewById(R.id.alarm_time_caption);
        mEnabledCheckBox = (CheckBox) v.findViewById(R.id.alarm_enabled_check_box);

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
                TimePickerFragment dialog = TimePickerFragment.newInstance(new Date());
                dialog.setTargetFragment(AlarmFragment.this, REQ_TIME);
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
            Toast.makeText(getActivity(), time.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        mEnabledCheckBox.setChecked(AlarmPreferences.isEnabled(getActivity()));
    }
}
