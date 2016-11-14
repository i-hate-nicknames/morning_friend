package com.domain.nvm.morningfriend.ui.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.domain.nvm.morningfriend.R;

public class SelectPuzzleActivity extends AppCompatActivity {

    private Spinner mDifficulty;
    private Spinner mPuzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_puzzle);

        mDifficulty = (Spinner) findViewById(R.id.alarm_detail_spinner_difficulty);
        mPuzzle = (Spinner) findViewById(R.id.alarm_detail_spinner_puzzle);

        String[] choices = getResources().getStringArray(R.array.pref_difficulty);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, choices);
        mDifficulty.setAdapter(adapter);

        choices = getResources().getStringArray(R.array.pref_puzzle);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, choices);
        mPuzzle.setAdapter(adapter);
    }
}
