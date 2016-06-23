package com.domain.nvm.morningfriend.squares;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domain.nvm.morningfriend.RingingControls;

public class SquaresFragment extends Fragment {

    private SquaresView mView;
    private RingingControls mControls;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mControls = (RingingControls) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = new SquaresView(getActivity());
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mControls.stopRinging();
                getActivity().finish();
            }
        });
        return mView;
    }
}
