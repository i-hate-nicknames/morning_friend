package com.domain.nvm.morningfriend;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class AlarmFragment extends Fragment {

    private TextView mTimeTextView;
    private CheckBox mEnabledCheckBox;

    public static AlarmFragment createFragment() {
        return new AlarmFragment();
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

        updateUI();

        return v;
    }

    private void updateUI() {
        mEnabledCheckBox.setChecked(AlarmPreferences.isEnabled(getActivity()));
    }
}
