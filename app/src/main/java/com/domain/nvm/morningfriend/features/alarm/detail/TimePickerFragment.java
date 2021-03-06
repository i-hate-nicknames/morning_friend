package com.domain.nvm.morningfriend.features.alarm.detail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.DateTimeUtils;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    private TimePicker mTimePicker;

    private static final String ARG_TIME = "time";
    private static final String ARG_HOUR = "hour";
    private static final String ARG_MINUTE = "minute";

    public static TimePickerFragment newInstance(Date time) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static TimePickerFragment newInstance(int hour, int minute) {
        Bundle args = new Bundle();
        args.putInt(ARG_HOUR, hour);
        args.putInt(ARG_MINUTE, minute);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date time = getArgTime();

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.dialog_time_picker, null);

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setIs24HourView(true);
        setTimePickerValue(time);

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

    public void sendResult() {
        int hour, minute;
        if (Build.VERSION.SDK_INT < 23) {
            hour = mTimePicker.getCurrentHour();
            minute = mTimePicker.getCurrentMinute();
        }
        else {
            hour = mTimePicker.getHour();
            minute = mTimePicker.getMinute();
        }

        Date resultTime = DateTimeUtils.getDateWithHourMinute(hour, minute);
        Intent data = new Intent();
        data.putExtra(AlarmDetailFragment.KEY_TIME, resultTime.getTime());
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
    }

    private void setTimePickerValue(Date time) {
        int hour = DateTimeUtils.getHour(time);
        int minute = DateTimeUtils.getMinute(time);

        if (Build.VERSION.SDK_INT < 23) {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }
        else {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        }
    }

    private Date getArgTime() {
        Bundle args = getArguments();
        Date time = (Date) args.getSerializable(ARG_TIME);
        if (time != null) {
            return time;
        }
        int hour = args.getInt(ARG_HOUR);
        int minute = args.getInt(ARG_MINUTE);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        return c.getTime();
    }
}
