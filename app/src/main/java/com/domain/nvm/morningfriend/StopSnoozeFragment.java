package com.domain.nvm.morningfriend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Date;

public class StopSnoozeFragment extends Fragment {

    private Button mStopButton;
    private Button mSnoozeButton;
    private static final long SNOOZE_TIME = 1 * 60 * 1000;
    private RingingControls mControls;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mControls = (RingingControls) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stop_snooze, container, false);
        mStopButton = (Button) v.findViewById(R.id.button_ringing_stop);
        mSnoozeButton = (Button) v.findViewById(R.id.button_ringing_snooze);

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mControls.stopRinging();
                getActivity().finish();
            }
        });

        mSnoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long fiveMinLater = System.currentTimeMillis() + SNOOZE_TIME;
                mControls.stopAndRestartRinging(new Date(fiveMinLater));
                getActivity().finish();
            }
        });

        return v;
    }
}
