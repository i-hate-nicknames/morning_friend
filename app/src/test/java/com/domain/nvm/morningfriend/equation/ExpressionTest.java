package com.domain.nvm.morningfriend.equation;

import com.domain.nvm.morningfriend.features.puzzle.equation.data.Constant;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.Environment;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExpressionTest {

    private Environment emptyEnv;

    @Before
    public void setUp() {
        emptyEnv = new Environment();
    }

    @Test
    public void evalConstant() {
        Constant x1 = new Constant(1);
        assertEquals(1, x1.eval(emptyEnv));
    }
}
