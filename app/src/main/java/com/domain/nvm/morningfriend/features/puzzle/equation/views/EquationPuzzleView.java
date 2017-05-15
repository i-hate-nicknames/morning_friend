package com.domain.nvm.morningfriend.features.puzzle.equation.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.features.puzzle.Puzzle;
import com.domain.nvm.morningfriend.features.puzzle.PuzzleHost;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.EquationGenerator;

public class EquationPuzzleView extends LinearLayout implements Puzzle {

    private Context context;

    public EquationPuzzleView(Context context) {
        super(context, null);
        this.context = context;
//        init(context, null);
    }

    @Override
    public void init(Difficulty difficulty) {
        initView();
    }

    @Override
    public void setPuzzleHost(PuzzleHost host) {

    }

    @Override
    public boolean isSolved() {
        return false;
    }

    public void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.puzzle_equation, this, true);
        TextView eqText = (TextView) findViewById(R.id.equation_body);
        eqText.setText(new EquationGenerator().generate(Difficulty.EASY).toString());
    }
}
