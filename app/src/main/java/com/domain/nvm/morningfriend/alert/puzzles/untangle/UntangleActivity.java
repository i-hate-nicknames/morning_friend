package com.domain.nvm.morningfriend.alert.puzzles.untangle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewTreeObserver;

import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.alert.PuzzleActivity;


public class UntangleActivity extends PuzzleActivity implements UntangleField.Callbacks {

    private UntangleField mField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_untangle);
        mField = (UntangleField) findViewById(R.id.untangle_field);
        mField.setCallbacks(this);
        mField.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mField.generateGraph();
                        mField.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    @Override
    public void onGraphSolved() {
        stopRinging();
    }

    @Override
    public void onSolutionBroken() {
        // TODO: restart ringing
    }
}
