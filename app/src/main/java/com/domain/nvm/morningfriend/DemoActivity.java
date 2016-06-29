package com.domain.nvm.morningfriend;

import android.support.v4.app.Fragment;

import com.domain.nvm.morningfriend.untangle.UntangleFragment;

public class DemoActivity extends SingleFragmentActivity {

    @Override
    public Fragment getFragment() {
        // TODO: get current puzzle setting and use appropriate fragment
        return new UntangleFragment();
    }
}
