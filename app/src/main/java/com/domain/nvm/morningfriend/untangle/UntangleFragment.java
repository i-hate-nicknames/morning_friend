package com.domain.nvm.morningfriend.untangle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;



public class UntangleFragment extends Fragment implements UntangleField.Callbacks {

    private UntangleField mField;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mField = new UntangleField(getActivity());
        mField.setCallback(this);
        mField.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mField.generateGraph();
                mField.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        return mField;
    }

    @Override
    public void onGraphUntangled() {

    }

    @Override
    public void onGraphTangled() {

    }
}
