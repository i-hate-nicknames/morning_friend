package com.domain.nvm.morningfriend.equation;

import com.domain.nvm.morningfriend.features.puzzle.equation.data.Constant;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.Equation;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.Expression;

import org.junit.Test;

import static org.junit.Assert.*;

public class EquationTest {

    @Test
    public void checkSolution() {
        int val = 5;
        Expression left = Equation.getUnknown();
        Expression right = new Constant(val);
        Equation eq = new Equation(left, right, val);
        assertTrue(eq.checkSolution(val));
    }
}
