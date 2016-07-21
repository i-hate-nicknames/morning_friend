package com.domain.nvm.morningfriend.alert;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.domain.nvm.morningfriend.SingleFragmentActivity;
import com.domain.nvm.morningfriend.alert.puzzles.untangle.UntangleFragment;

import java.util.Date;

public class DemoActivity extends SingleFragmentActivity implements RingingControls {

    @Override
    public Fragment getFragment() {
        // TODO: get current puzzle setting and use appropriate fragment
        return new UntangleFragment();
    }

    @Override
    public void stopRinging() {
        Toast.makeText(this, "Ringing stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void stopAndRestartRinging(Date restartTime) {
        Toast.makeText(this, "Snooze called", Toast.LENGTH_LONG).show();
    }
}
