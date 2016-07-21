package com.domain.nvm.morningfriend.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmRegisterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, AlarmRegisterService.class);
        context.startService(serviceIntent);
    }
}
