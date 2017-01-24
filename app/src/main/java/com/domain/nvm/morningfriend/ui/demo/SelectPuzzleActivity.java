package com.domain.nvm.morningfriend.ui.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.puzzle.Puzzle;
import com.domain.nvm.morningfriend.ui.puzzle.TrainingActivity;

public class SelectPuzzleActivity extends AppCompatActivity {

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_puzzle);

        final Spinner difficultySpinner, puzzleSpinner;

        difficultySpinner = (Spinner) findViewById(R.id.alarm_detail_spinner_difficulty);
        puzzleSpinner = (Spinner) findViewById(R.id.alarm_detail_spinner_puzzle);

        String[] choices = getResources().getStringArray(R.array.pref_difficulty);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, choices);
        difficultySpinner.setAdapter(adapter);

        choices = getResources().getStringArray(R.array.pref_puzzle);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, choices);
        puzzleSpinner.setAdapter(adapter);

        Button tryPuzzle = (Button) findViewById(R.id.demo_try_puzzle);
        tryPuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int difficultyPos = difficultySpinner.getSelectedItemPosition();
                Puzzle.Difficulty diff = Puzzle.Difficulty.values()[difficultyPos];
                int puzzlePos = puzzleSpinner.getSelectedItemPosition();
                Puzzle.PuzzleType puzzleType = Puzzle.PuzzleType.values()[puzzlePos];
                Intent training =
                        TrainingActivity.makeIntent(SelectPuzzleActivity.this, puzzleType, diff);
                startActivity(training);
            }
        });
    }
}
