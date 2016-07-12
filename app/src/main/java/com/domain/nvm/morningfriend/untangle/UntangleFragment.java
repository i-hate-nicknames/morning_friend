package com.domain.nvm.morningfriend.untangle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.domain.nvm.morningfriend.R;


public class UntangleFragment extends Fragment {

    private UntangleField mField;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_untangle_field, container, false);
        mField = (UntangleField) v.findViewById(R.id.untangle_field);
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








}
