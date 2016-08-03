package com.domain.nvm.morningfriend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.domain.nvm.morningfriend.alert.RingingState;
import com.domain.nvm.morningfriend.alert.puzzles.squares.SquaresActivity;
import com.domain.nvm.morningfriend.database.AlarmsRepository;
import com.domain.nvm.morningfriend.scheduler.AlarmSettings;

import java.util.List;

public class AlarmsListActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    AlarmsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (RingingState.get().isRinging()) {
            Intent i = new Intent(this, SquaresActivity.class);
            startActivity(i);
            finish();
        }
        setContentView(R.layout.activity_alarms_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.alarms_list_list_view);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.alarms_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.alarms_list_add_alarm:
                Alarm a = AlarmsRepository.get(this).addAlarm();
                startActivity(AlarmDetailActivity.makeIntent(this, a));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new AlarmsAdapter(AlarmsRepository.get(this).getAlarms());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

    }

    private class AlarmsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mIdTextView;
        private TextView mTimeTextView;
        private Alarm mAlarm;

        public AlarmsHolder(View itemView) {
            super(itemView);
            mTimeTextView = (TextView) itemView.findViewById(R.id.alarm_list_item_time);
            mIdTextView = (TextView) itemView.findViewById(R.id.alarm_list_item_id);
            itemView.setOnClickListener(this);
        }

        public void bindAlarm(Alarm alarm) {
            this.mAlarm = alarm;
            mIdTextView.setText(Integer.toString(alarm.getId()));
            mTimeTextView.setText(AlarmSettings.formatDate(alarm.getTime()));
        }

        @Override
        public void onClick(View v) {
            Intent i = AlarmDetailActivity.makeIntent(AlarmsListActivity.this, mAlarm);
            startActivity(i);
        }
    }

    private class AlarmsAdapter extends RecyclerView.Adapter<AlarmsHolder> {

        private List<Alarm> mAlarmsList;

        public AlarmsAdapter(List<Alarm> alarmList) {
            mAlarmsList = alarmList;
        }

        @Override
        public AlarmsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(AlarmsListActivity.this);
            View v = inflater.inflate(R.layout.list_item_alarm, parent, false);
            return new AlarmsHolder(v);
        }

        @Override
        public void onBindViewHolder(AlarmsHolder holder, int position) {
            holder.bindAlarm(mAlarmsList.get(position));
        }

        @Override
        public int getItemCount() {
            return mAlarmsList.size();
        }
    }
}
