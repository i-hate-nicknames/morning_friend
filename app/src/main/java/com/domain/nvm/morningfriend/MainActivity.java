package com.domain.nvm.morningfriend;

import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity {


    @Override
    public Fragment getFragment() {
        return AlarmFragment.createFragment();
    }
}
