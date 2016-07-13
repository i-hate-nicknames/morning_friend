package com.domain.nvm.morningfriend.untangle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.RingingControls;


public class UntangleFragment extends Fragment implements UntangleField.Callbacks {

    private UntangleField mField;
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
        View v = inflater.inflate(R.layout.fragment_untangle_field, container, false);
        mField = (UntangleField) v.findViewById(R.id.untangle_field);
        mField.setCallbacks(this);
        mField.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mField.generateGraph();
                mField.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        return v;
    }

    @Override
    public void onGraphSolved() {
        mControls.stopRinging();
    }

    @Override
    public void onSolutionBroken() {
        // TODO: restart ringing
    }
}
