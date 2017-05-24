package com.domain.nvm.morningfriend.features.puzzle.equation.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.domain.nvm.morningfriend.R;
import com.domain.nvm.morningfriend.features.puzzle.Puzzle;
import com.domain.nvm.morningfriend.features.puzzle.PuzzleHost;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.Equation;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.EquationGenerator;

public class EquationPuzzleView extends LinearLayout implements Puzzle {

    private static final int MAX_LIVES = 3;

    private Context context;
    private Equation equation;
    private PuzzleHost puzzleHost;
    private boolean isSolved;
    private int livesLeft;
    private TextView equationTextView, livesLeftTextView;
    private Difficulty difficulty;

    public EquationPuzzleView(Context context) {
        super(context, null);
        this.context = context;
//        init(context, null);
    }

    @Override
    public void init(Difficulty difficulty) {
        this.difficulty = difficulty;
        initView();
    }

    @Override
    public void setPuzzleHost(PuzzleHost host) {
        puzzleHost = host;
    }

    @Override
    public boolean isSolved() {
        return isSolved;
    }

    public void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.puzzle_equation, this, true);
        equationTextView = (TextView) findViewById(R.id.equation_body);
        livesLeftTextView = (TextView) findViewById(R.id.equation_lives_left);
        generateEquation();
        updateViews();
        final EditText solutionEditText = (EditText) findViewById(R.id.equation_solution_edit_text);
        Button okButton = (Button) findViewById(R.id.equation_solution_button_ok);
        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String solutionString = solutionEditText.getText().toString();
                if (solutionString.equals("")) {
                    return;
                }
                int userSolution = Integer.parseInt(solutionString);
                if (equation.checkSolution(userSolution)) {
                    isSolved = true;
                    puzzleHost.onPuzzleSolved();
                }
                else {
                    if (livesLeft == 1) {
                        isSolved = false;
                        generateEquation();
                        updateViews();
                    }
                    else {
                        livesLeft--;
                        updateViews();
                    }
                }
            }
        });
    }

    private void generateEquation() {
        // todo: add generate except value method that generates an equation which unknown's value
        // doesn't have given value
        // it will be used to prevent user from taking one value n and regenerate equation by spending
        // all lives over and over until he meets an equation whose solution is n.
        equation = new EquationGenerator().generate(difficulty);
        livesLeft = MAX_LIVES;
    }

    private void updateViews() {
        equationTextView.setText(equation.toString());
        // todo: move to strings.xml
        livesLeftTextView.setText("Remaining lives " + livesLeft);
        // update also lives count view here
    }
}
