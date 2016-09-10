package com.domain.nvm.morningfriend.ui.alarm_list;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.domain.nvm.morningfriend.Alarm;
import com.domain.nvm.morningfriend.ui.alarm_detail.AlarmDetailActivity;
import com.domain.nvm.morningfriend.ui.logs.LogActivity;
import com.domain.nvm.morningfriend.ui.logs.Logger;
import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.ui.settings.SettingsActivity;
import com.domain.nvm.morningfriend.Utils;
import com.domain.nvm.morningfriend.alert.RingingState;
import com.domain.nvm.morningfriend.ui.puzzle.squares.SquaresActivity;
import com.domain.nvm.morningfriend.ui.puzzle.untangle.UntangleActivity;
import com.domain.nvm.morningfriend.database.AlarmRepository;
import com.domain.nvm.morningfriend.alert.scheduler.AlarmScheduler;

import java.util.ArrayList;
import java.util.List;

public class AlarmListActivity extends AppCompatActivity {

    private static final String BUNDLE_RECYCLER_LAYOUT = "AlarmListActivity.recycler.layout";

    private RecyclerView mRecyclerView;
    private TextView mNextAlarm;
    private FloatingActionButton mAddButton;
    private AlarmAdapter mAdapter;
    private List<Alarm> mAlarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Alarm alarm = RingingState.getAlarm(this);
        if (alarm != null) {
            Intent puzzle;
            switch (alarm.getPuzzle()) {
                case GRAPH:
                    puzzle = UntangleActivity.newIntent(this, alarm);
                    break;
                default:
                case SQUARES:
                    puzzle = SquaresActivity.newIntent(this, alarm);
                    break;
            }
            startActivity(puzzle);
            finish();
        }
        setContentView(R.layout.activity_alarms_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.alarms_list_list_view);
        mRecyclerView.addItemDecoration(new AlarmItemDecoration(this, 15));

        mNextAlarm = (TextView) findViewById(R.id.alarms_list_next_alarm);
        mAddButton = (FloatingActionButton) findViewById(R.id.fab_add);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAlarm();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAlarmList = new ArrayList<>();
        mAdapter = new AlarmAdapter(mAlarmList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void createNewAlarm() {
        Alarm a = AlarmRepository.get(this).addAlarm();
        startActivity(AlarmDetailActivity.makeIntent(this, a));
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
                createNewAlarm();
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
        updateUI();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Parcelable layoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
        mRecyclerView.getLayoutManager().onRestoreInstanceState(layoutState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    private void updateUI() {
        mAlarmList.clear();
        mAlarmList.addAll(AlarmRepository.get(this).getSortedAlarms());
        mAdapter.notifyDataSetChanged();
        Alarm closest = AlarmScheduler.getClosestAlarm(this);
        if (closest != null) {
            long timeDiff = closest.getTime() - System.currentTimeMillis();
            mNextAlarm.setText(Utils.formatRemainingTime(timeDiff));
        }
        if (mAddButton.getVisibility() != View.VISIBLE) {
            mAddButton.show();
        }
    }

    private class AlarmsHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, CheckBox.OnCheckedChangeListener {

        private TextView mRepeatDays;
        private TextView mTimeTextView;
        private Alarm mAlarm;
        private CheckBox mCheckBox;

        public AlarmsHolder(View itemView) {
            super(itemView);
            mTimeTextView = (TextView) itemView.findViewById(R.id.alarm_list_item_time);
            mRepeatDays = (TextView) itemView.findViewById(R.id.alarm_list_item_repeat);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.alarm_list_enabled_check_box);
            itemView.setOnClickListener(this);
        }

        public void bindAlarm(Alarm alarm) {
            this.mAlarm = alarm;
            mRepeatDays.setText(Utils.formatRepeatingDays(AlarmListActivity.this,
                    alarm.getRepeatDays()));
            mTimeTextView.setText(Utils.formatTime(alarm.getHour(), alarm.getMinute()));
            mCheckBox.setChecked(alarm.isEnabled());
            mCheckBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = AlarmDetailActivity.makeIntent(AlarmListActivity.this, mAlarm);
            startActivity(i);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mAlarm.setEnabled(isChecked);
            AlarmRepository.get(AlarmListActivity.this).updateAlarm(mAlarm);
        }
    }

    private class AlarmAdapter extends RecyclerView.Adapter<AlarmsHolder> {

        private List<Alarm> mAlarmsList;

        public AlarmAdapter(List<Alarm> alarmList) {
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
