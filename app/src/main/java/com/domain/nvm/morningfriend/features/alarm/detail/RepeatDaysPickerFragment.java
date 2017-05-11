package com.domain.nvm.morningfriend.features.alarm.detail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.features.alarm.Alarm;
import com.domain.nvm.morningfriend.features.alarm.Alarm.Days;


public class RepeatDaysPickerFragment extends DialogFragment {

    private static final String ARG_DAYS = "days";

    private Days repeatDays;
    RadioGroup daysRadioGroup;
    private CheckBox[] dayCheckBoxes;

    private int[] checkBoxIds = new int[] {R.id.details_repeating_days_sun_check_box,
            R.id.details_repeating_days_mon_check_box, R.id.details_repeating_days_tue_check_box,
            R.id.details_repeating_days_wed_check_box, R.id.details_repeating_days_thu_check_box,
            R.id.details_repeating_days_fri_check_box, R.id.details_repeating_days_sat_check_box};



    public static RepeatDaysPickerFragment newInstance(Days repeatDays) {
        RepeatDaysPickerFragment fragment = new RepeatDaysPickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DAYS, repeatDays);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        repeatDays = (Days) getArguments().getSerializable(ARG_DAYS);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.dialog_repeat_days_picker, null);

        daysRadioGroup = (RadioGroup) v.findViewById(R.id.alarm_detail_days_radio_group);
        daysRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.alarm_detail_days_workdays_radio_button:
                        repeatDays.setOnlyWorkDays();
                        updateDaysViews(false);
                        break;
                    case R.id.alarm_detail_days_weekends_radio_button:
                        repeatDays.setOnlyWeekends();
                        updateDaysViews(false);
                        break;
                    default:
                        updateDaysViews(true);
                }
            }
        });
        dayCheckBoxes = new CheckBox[checkBoxIds.length];
        for (int i = 0; i < checkBoxIds.length; i++) {
            dayCheckBoxes[i] = (CheckBox) v.findViewById(checkBoxIds[i]);
            dayCheckBoxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    switch (buttonView.getId()) {
                        case R.id.details_repeating_days_sun_check_box:
                            setAlarmRepeatDay(Days.Names.SUN, isChecked);
                            break;
                        case R.id.details_repeating_days_mon_check_box:
                            setAlarmRepeatDay(Days.Names.MON, isChecked);
                            break;
                        case R.id.details_repeating_days_tue_check_box:
                            setAlarmRepeatDay(Days.Names.TUE, isChecked);
                            break;
                        case R.id.details_repeating_days_wed_check_box:
                            setAlarmRepeatDay(Days.Names.WED, isChecked);
                            break;
                        case R.id.details_repeating_days_thu_check_box:
                            setAlarmRepeatDay(Days.Names.THU, isChecked);
                            break;
                        case R.id.details_repeating_days_fri_check_box:
                            setAlarmRepeatDay(Days.Names.FRI, isChecked);
                            break;
                        case R.id.details_repeating_days_sat_check_box:
                            setAlarmRepeatDay(Days.Names.SAT, isChecked);
                            break;
                    }
                }
            });
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult();
                    }
                })
                .create();
    }

    private void sendResult() {
        Intent data = new Intent();
        data.putExtra(AlarmDetailFragment.KEY_DAYS, repeatDays);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
    }

    private void setAlarmRepeatDay(Days.Names dayName, boolean isOn) {
        updateDaysViews(true);
    }


    private void updateDaysViews(boolean enableCheckBoxes) {

        if (repeatDays.isOnlyWorkDays()) {
            daysRadioGroup.check(R.id.alarm_detail_days_workdays_radio_button);
            for (int dayCode = 0; dayCode < dayCheckBoxes.length; dayCode++) {
                dayCheckBoxes[dayCode].setChecked(Alarm.Days.isDayWorkday(dayCode));
                dayCheckBoxes[dayCode].setEnabled(enableCheckBoxes);
            }

        }
        else if (repeatDays.isOnlyWeekends()) {
            daysRadioGroup.check(R.id.alarm_detail_days_weekends_radio_button);
            for (int dayCode = 0; dayCode < dayCheckBoxes.length; dayCode++) {
                dayCheckBoxes[dayCode].setChecked(Alarm.Days.isDayWeekend(dayCode));
                dayCheckBoxes[dayCode].setEnabled(enableCheckBoxes);
            }
        }
        else {
            daysRadioGroup.check(R.id.alarm_detail_days_custom_radio_button);
            for (int dayCode = 0; dayCode < dayCheckBoxes.length; dayCode++) {
                dayCheckBoxes[dayCode]
                        .setChecked(repeatDays.isDayActive(Days.Names.values()[dayCode]));
            }
        }

    }
}
