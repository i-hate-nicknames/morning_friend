package com.domain.nvm.morningfriend.features.training;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import com.domain.nvm.morningfriend.features.puzzle.Puzzle;
import com.domain.nvm.morningfriend.features.puzzle.Puzzle.Difficulty;
import com.domain.nvm.morningfriend.features.puzzle.Puzzle.PuzzleType;
import com.domain.nvm.morningfriend.features.puzzle.PuzzleHost;
import com.domain.nvm.morningfriend.features.puzzle.squares.SquaresView;
import com.domain.nvm.morningfriend.features.puzzle.untangle.views.UntangleView;

public class TrainingActivity extends AppCompatActivity implements PuzzleHost {

    private static final String EXTRA_PUZZLE_TYPE = "puzzleType";
    private static final String EXTRA_PUZZLE_DIFFICULTY = "puzzleDifficulty";

    private Puzzle mPuzzle;

    public static Intent makeIntent(Context context, PuzzleType puzzleType, Difficulty difficulty) {
        Intent i = new Intent(context, TrainingActivity.class);
        i.putExtra(EXTRA_PUZZLE_TYPE, puzzleType);
        i.putExtra(EXTRA_PUZZLE_DIFFICULTY, difficulty);
        return i;
    }

    @Override
    public void onPuzzleSolved() {

    }

    @Override
    public void onPuzzleSolutionBroken() {

    }

    @Override
    public void onPuzzleTouched() {

    }

    @Override
    public void snooze() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PuzzleType type = (PuzzleType) getIntent().getSerializableExtra(EXTRA_PUZZLE_TYPE);
        final Difficulty diff = (Difficulty) getIntent().getSerializableExtra(EXTRA_PUZZLE_DIFFICULTY);

        final View puzzleView;
        switch (type) {
            case GRAPH:
                puzzleView = new UntangleView(this);
                mPuzzle = (UntangleView) puzzleView;
                break;
            case SQUARES:
            default:
                puzzleView = new SquaresView(this);
                mPuzzle = (SquaresView) puzzleView;
                break;
        }

        setContentView(puzzleView);
        mPuzzle.setPuzzleHost(this);
        puzzleView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mPuzzle.init(diff);
                        puzzleView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

    }
}
