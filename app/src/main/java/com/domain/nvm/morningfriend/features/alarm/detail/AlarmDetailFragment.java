package com.domain.nvm.morningfriend.features.alarm.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.XpPreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domain.nvm.morningfriend.DateTimeUtils;
import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.database.AlarmRepository;
import com.domain.nvm.morningfriend.features.alarm.Alarm;

import net.xpece.android.support.preference.SwitchPreference;
import net.xpece.android.support.preference.DialogPreference;

public class AlarmDetailFragment extends XpPreferenceFragment
        implements PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback {

    private static final String ARG_ALARM = "alarm";

    public static final String KEY_TIME = "alarm_time";
    public static final String KEY_PUZZLE = "alarm_puzzle";
    public static final String KEY_DIFFICULTY = "alarm_difficulty";
    public static final String KEY_DAYS = "alarm_days";

    public static final int REQUEST_TIME = 0;
    public static final int REQUEST_PUZZLE = 1;
    public static final int REQUEST_DIFFICULTY = 2;
    public static final int REQUEST_DAYS = 3;

    private Alarm alarm;
    private DialogPreference timePref;
    private SwitchPreference switchPref;

    public static AlarmDetailFragment makeFragment(Alarm alarm) {
        AlarmDetailFragment fragment = new AlarmDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ALARM, alarm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
        alarm = (Alarm) getArguments().getSerializable(ARG_ALARM);
        if (alarm == null) {
            throw new IllegalArgumentException("Trying to instantiate " +
                    "alarm settings fragment without alarm object");
        }
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(getActivity());
        switchPref = new SwitchPreference(getActivity());
        switchPref.setShouldDisableView(true);
        switchPref.setTitle(R.string.alarm_enabled_caption);
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

        timePref = new DialogPreference(getActivity()) {};
        timePref.setTitle(R.string.details_time_label);
        timePref.setDialogLayoutResource(R.layout.dialog_time_picker);
        timePref.setKey(KEY_TIME);
        screen.addPreference(timePref);

        setPreferenceScreen(screen);
        updatePrefsUI();
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        super.onDisplayPreferenceDialog(preference);
    }

    @Override
    public boolean onPreferenceDisplayDialog(PreferenceFragmentCompat caller, Preference pref) {
        if (pref.getKey().equals(KEY_TIME)) {
            DialogFragment f = TimePickerFragment.newInstance(alarm.getHour(), alarm.getMinute());
            f.setTargetFragment(this, REQUEST_TIME);
            f.show(this.getFragmentManager(), DIALOG_FRAGMENT_TAG);
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TIME:
                if (resultCode == Activity.RESULT_OK) {
                    long time = data.getLongExtra(KEY_TIME, -1);
                    if (time != -1) {
                        alarm.setHour(DateTimeUtils.getHour(time));
                        alarm.setMinute(DateTimeUtils.getMinute(time));
                        updatePrefsUI();
                        updateAlarm();
                    }
                }
                return;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = super.onCreateView(inflater, container, savedInstanceState);

        return v;
    }

    private void updateAlarm() {
        AlarmRepository.get(getActivity()).updateAlarm(alarm);
    }

    private void updatePrefsUI() {
        timePref.setSummary(DateTimeUtils.formatTime(alarm.getHour(), alarm.getMinute()));
    }
}
