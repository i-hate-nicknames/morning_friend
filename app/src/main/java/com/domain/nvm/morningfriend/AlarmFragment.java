package com.domain.nvm.morningfriend;

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

public class AlarmFragment extends Fragment {

    private TextView mTimeTextView;
    private CheckBox mEnabledCheckBox;

    private static final String TAG = "AlarmFragment";

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

        mTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mgr = getFragmentManager();
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.show(mgr, TAG);
            }
        });

        updateUI();

        return v;
    }

    private void updateUI() {
        mEnabledCheckBox.setChecked(AlarmPreferences.isEnabled(getActivity()));
    }
}
