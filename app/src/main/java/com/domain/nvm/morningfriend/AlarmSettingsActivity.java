package com.domain.nvm.morningfriend;

import android.support.v4.app.Fragment;

public class AlarmSettingsActivity extends SingleFragmentActivity {


    @Override
    public Fragment getFragment() {
        return AlarmSettingsFragment.createFragment();
    }
}
