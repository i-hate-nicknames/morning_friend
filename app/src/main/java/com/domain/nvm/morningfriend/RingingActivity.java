package com.domain.nvm.morningfriend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Date;


public class RingingActivity extends AppCompatActivity {

    private Button mStopButton;
    private Button mSnoozeButton;
    private RingingService mService;
    private boolean mBound = false;

    private static final long SNOOZE_TIME = 1 * 60 * 1000;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RingingService.RingingBinder binder = (RingingService.RingingBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringing);

        mStopButton = (Button) findViewById(R.id.button_ringing_stop);
        mSnoozeButton = (Button) findViewById(R.id.button_ringing_snooze);

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.stopPlaying();
                finish();
            }
        });

        mSnoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.stopPlaying();
                long fiveMinLater = System.currentTimeMillis() + SNOOZE_TIME;
                setRingingAlarm(getApplicationContext(), new Date(fiveMinLater), true);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, RingingService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        // this won't create another instance of service, but will call onStartCommand
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    public static void setRingingAlarm(Context context, Date time, boolean isOn) {
        Intent i = new Intent(context, RingingActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (isOn) {
            am.set(AlarmManager.RTC_WAKEUP, time.getTime(), pi);
        }
        else {
            am.cancel(pi);
            pi.cancel();
        }
    }
}
