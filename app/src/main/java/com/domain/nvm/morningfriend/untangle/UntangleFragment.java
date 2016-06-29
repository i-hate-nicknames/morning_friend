package com.domain.nvm.morningfriend.untangle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class UntangleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        TextView tv = new TextView(getActivity());
        tv.setText("text view");
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(200, 200, Gravity.LEFT | Gravity.TOP);
        layout.addView(tv, params);
        return layout;
    }
}
