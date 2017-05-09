package com.domain.nvm.morningfriend.features.alarm.detail;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domain.nvm.morningfriend.database.AlarmRepository;
import com.domain.nvm.morningfriend.features.alarm.Alarm;

public class AlarmDetailFragment extends PreferenceFragment {

    private static final String ARG_ALARM = "alarm";

    private Alarm alarm;

    public static AlarmDetailFragment makeFragment(Alarm alarm) {
        AlarmDetailFragment fragment = new AlarmDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ALARM, alarm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        alarm = (Alarm) getArguments().getSerializable(ARG_ALARM);
        if (alarm == null) {
            throw new IllegalArgumentException("Trying to instantiate " +
                    "alarm settings fragment without alarm object");
        }
        View v = super.onCreateView(inflater, container, savedInstanceState);
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(getActivity());
        SwitchPreference switchPref = new SwitchPreference(getActivity());
        switchPref.setTitle("Enabled");
        switchPref.setDefaultValue(alarm.isEnabled());
        switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                alarm.setEnabled((Boolean) newValue);
                updateAlarm();
                return true;
            }
        });
        screen.addPreference(switchPref);
        setPreferenceScreen(screen);
        return v;
    }

    private void updateAlarm() {
        AlarmRepository.get(getActivity()).updateAlarm(alarm);
    }
}
