package com.domain.nvm.morningfriend;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
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
import com.domain.nvm.morningfriend.database.AlarmRepository;

import java.util.Date;
import java.util.List;

public class AlarmListActivity extends AppCompatActivity {

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
        mRecyclerView.addItemDecoration(new AlarmItemDecoration(this, 15));

        TextView nextAlarmText = (TextView) findViewById(R.id.alarms_list_next_alarm);
        String pref = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("pref_snooze", null);
        long time = Long.parseLong(pref.split("_")[0]);
        nextAlarmText.setText(Utils.formatDate(new Date(time)));
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
                Alarm a = AlarmRepository.get(this).addAlarm();
                startActivity(AlarmDetailActivity.makeIntent(this, a));
                return true;
            case R.id.list_menu_logs:
                Intent log = new Intent(this, LogActivity.class);
                startActivity(log);
                return true;
            case R.id.list_menu_clear:
                Logger.clear(this);
                return true;
            case R.id.list_menu_demo:
                Intent demo = new Intent(this, SquaresActivity.class);
                startActivity(demo);
                return true;
            case R.id.list_menu_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = new AlarmsAdapter(AlarmRepository.get(this).getAlarms());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

    }

    private class AlarmsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private TextView mTimeTextView;
        private Alarm mAlarm;

        public AlarmsHolder(View itemView) {
            super(itemView);
            mTimeTextView = (TextView) itemView.findViewById(R.id.alarm_list_item_time);
            mTitle = (TextView) itemView.findViewById(R.id.alarm_list_item_id);
            itemView.setOnClickListener(this);
        }

        public void bindAlarm(Alarm alarm) {
            this.mAlarm = alarm;
            mTitle.setText(alarm.getMessage());
            mTimeTextView.setText(Utils.formatTime(alarm.getHour(), alarm.getMinute()));
        }

        @Override
        public void onClick(View v) {
            Intent i = AlarmDetailActivity.makeIntent(AlarmListActivity.this, mAlarm);
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
            LayoutInflater inflater = LayoutInflater.from(AlarmListActivity.this);
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

    private class AlarmItemDecoration extends RecyclerView.ItemDecoration {

        private final int[] mAttrs = new int[] {android.R.attr.listDivider};

        private final int mVerticalSpaceHeight;

        private Drawable mDivider;

        public AlarmItemDecoration(Context context, int spaceHeight) {
            this.mVerticalSpaceHeight = spaceHeight;
            final TypedArray styledAttrs = context.obtainStyledAttributes(mAttrs);
            mDivider = styledAttrs.getDrawable(0);
            styledAttrs.recycle();
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = mVerticalSpaceHeight;
            }
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params =
                        (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
