package com.domain.nvm.morningfriend.features.alarm.detail;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.XpPreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domain.nvm.morningfriend.DateTimeUtils;
import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.database.AlarmRepository;
import com.domain.nvm.morningfriend.features.alarm.Alarm;
import com.domain.nvm.morningfriend.features.alarm.Utils;

import net.xpece.android.support.preference.EditTextPreference;
import net.xpece.android.support.preference.ListPreference;
import net.xpece.android.support.preference.SwitchPreference;
import net.xpece.android.support.preference.DialogPreference;

public class AlarmDetailFragment extends XpPreferenceFragment
        implements PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback {

    private static final String ARG_ALARM = "alarm";

    public static final String KEY_TIME = "alarm_time";
    public static final String KEY_MESSAGE = "alarm_message";
    public static final String KEY_PUZZLE = "alarm_puzzle";
    public static final String KEY_DIFFICULTY = "alarm_difficulty";
    public static final String KEY_DAYS = "alarm_days";

    public static final int REQUEST_TIME = 0;
    public static final int REQUEST_PUZZLE = 1;
    public static final int REQUEST_DIFFICULTY = 2;
    public static final int REQUEST_DAYS = 3;

    private Alarm alarm;
    private DialogPreference timePref, daysPref;
    private SwitchPreference switchPref;
    private ListPreference puzzlePref;
    private ListPreference difficultyPref;
    private EditTextPreference messagePref;

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
        updatePrefs();
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
        timePref.setKey(KEY_TIME);
        screen.addPreference(timePref);

        puzzlePref = new ListPreference(getActivity());
        puzzlePref.setKey(KEY_PUZZLE);
        puzzlePref.setTitle(R.string.choose_puzzle_caption);
        puzzlePref.setEntries(R.array.pref_puzzle);
        puzzlePref.setEntryValues(R.array.pref_puzzle_values);
        puzzlePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String puzzle = newValue.toString();
                int index = puzzlePref.findIndexOfValue(puzzle);
                alarm.setPuzzleType(index);
                onFieldUpdated();
                return true;
            }
        });
        screen.addPreference(puzzlePref);

        difficultyPref = new ListPreference(getActivity());
        difficultyPref.setKey(KEY_DIFFICULTY);
        difficultyPref.setTitle(R.string.choose_difficulty_caption);
        difficultyPref.setEntries(R.array.pref_difficulty);
        difficultyPref.setEntryValues(R.array.pref_difficulty_values);
        difficultyPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String difficulty = newValue.toString();
                int index = difficultyPref.findIndexOfValue(difficulty);
                alarm.setDifficulty(index);
                onFieldUpdated();
                return true;
            }
        });
        screen.addPreference(difficultyPref);

        messagePref = new EditTextPreference(getActivity());
        messagePref.setKey(KEY_MESSAGE);
        messagePref.setTitle(R.string.details_message_label);
        messagePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                alarm.setMessage(newValue.toString());
                onFieldUpdated();
                return true;
            }
        });
        screen.addPreference(messagePref);

        daysPref = new DialogPreference(getActivity()) {};
        daysPref.setTitle("Repeat");
        daysPref.setKey(KEY_DAYS);
        screen.addPreference(daysPref);

        setPreferenceScreen(screen);
        updatePrefsUI();
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        super.onDisplayPreferenceDialog(preference);
    }

    @Override
    public boolean onPreferenceDisplayDialog(PreferenceFragmentCompat caller, Preference pref) {
        if (KEY_TIME.equals(pref.getKey())) {
            DialogFragment f = TimePickerFragment.newInstance(alarm.getHour(), alarm.getMinute());
            f.setTargetFragment(this, REQUEST_TIME);
            f.show(this.getFragmentManager(), DIALOG_FRAGMENT_TAG);
            return true;
        }
        else if (KEY_DAYS.equals(pref.getKey())) {
            DialogFragment f = RepeatDaysPickerFragment.newInstance(alarm.getRepeatDays());
            f.setTargetFragment(this, REQUEST_DAYS);
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
                        onFieldUpdated();
                    }
                }
                return;
            case REQUEST_DAYS:
                if (resultCode == Activity.RESULT_OK) {
                    Alarm.Days repeat = (Alarm.Days) data.getSerializableExtra(KEY_DAYS);
                    if (repeat != null) {
                        alarm.setRepeatDays(repeat);
                        onFieldUpdated();
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

    private void onFieldUpdated() {
        updatePrefs();
        updateAlarm();
        updatePrefsUI();
    }

    /**
     * The values of the shared prefs aren't used anywhere in the program, so we technically
     * don't even care what is set there.
     * Unfortunately, these values are still shown when we edit our programmatically created
     * preferences
     * This method sets
     */
    private void updatePrefs() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.edit()
                .putString(KEY_MESSAGE, alarm.getMessage())
                .putString(KEY_DIFFICULTY, Utils.getAlarmDifficultyKey(getActivity(), alarm))
                .putString(KEY_PUZZLE, Utils.getAlarmPuzzleKey(getActivity(), alarm))
                .apply();
    }

    private void updateAlarm() {
        AlarmRepository.get(getActivity()).updateAlarm(alarm);
    }

    private void updatePrefsUI() {
        timePref.setSummary(DateTimeUtils.formatTime(alarm.getHour(), alarm.getMinute()));
        puzzlePref.setSummary(Utils.getAlarmPuzzleTitle(getActivity(), alarm));
        difficultyPref.setSummary(Utils.getAlarmDifficultyTitle(getActivity(), alarm));
        messagePref.setSummary(alarm.getMessage());
        daysPref.setSummary(DateTimeUtils.formatRepeatingDays(getActivity(),
                alarm.getRepeatDays()));
    }
}
