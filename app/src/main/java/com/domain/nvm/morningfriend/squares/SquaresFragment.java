package com.domain.nvm.morningfriend.squares;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.domain.nvm.morningfriend.RingingControls;

import java.util.Date;

public class SquaresFragment extends Fragment implements SquaresView.SquareClickedListener {

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
        mView.setCallback(this);
        mView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mView.initSquares();
                mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        return mView;
    }

    @Override
    public void onRedClicked() {
        mControls.stopRinging();
        getActivity().finish();
    }

    @Override
    public void onGreenClicked() {
        long nextStart = System.currentTimeMillis() + 5 * 1000;
        mControls.stopAndRestartRinging(new Date(nextStart));
        getActivity().finish();
    }
}
