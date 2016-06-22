package com.domain.nvm.morningfriend;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class AlarmSettingsActivity extends SingleFragmentActivity {


    @Override
    public Fragment getFragment() {
        return AlarmSettingsFragment.createFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isRingingServiceRunning()) {
            Intent i = new Intent(this, RingingActivity.class);
            startActivity(i);
            finish();
        }
    }

    private boolean isRingingServiceRunning() {
        ActivityManager mgr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service:
                mgr.getRunningServices(Integer.MAX_VALUE)) {
            if (RingingService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
