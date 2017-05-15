package com.domain.nvm.morningfriend.features.puzzle.equation.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.features.puzzle.Puzzle;
import com.domain.nvm.morningfriend.features.puzzle.PuzzleHost;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.Equation;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.EquationGenerator;

public class EquationPuzzleView extends LinearLayout implements Puzzle {

    private Context context;
    private Equation equation;
    private PuzzleHost puzzleHost;

    public EquationPuzzleView(Context context) {
        super(context, null);
        this.context = context;
//        init(context, null);
    }

    @Override
    public void init(Difficulty difficulty) {
        equation = new EquationGenerator().generate(difficulty);
        initView();
    }

    @Override
    public void setPuzzleHost(PuzzleHost host) {
        puzzleHost = host;
    }

    @Override
    public boolean isSolved() {
        return false;
    }

    public void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.puzzle_equation, this, true);
        TextView equationText = (TextView) findViewById(R.id.equation_body);
        equationText.setText(equation.toString());
        final EditText solutionEditText = (EditText) findViewById(R.id.equation_solution_edit_text);
        Button okButton = (Button) findViewById(R.id.equation_solution_button_ok);
        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int userSolution = Integer.parseInt(solutionEditText.getText().toString());
                if (equation.checkSolution(userSolution)) {
                    Toast.makeText(context, "Very well done mon ami!", Toast.LENGTH_SHORT).show();
                    puzzleHost.onPuzzleSolved();
                }
            }
        });

    }
}
