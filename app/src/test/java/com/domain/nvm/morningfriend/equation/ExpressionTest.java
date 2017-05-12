package com.domain.nvm.morningfriend.equation;

import com.domain.nvm.morningfriend.features.puzzle.equation.data.Constant;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.Environment;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.Sum;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.Variable;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExpressionTest {

    private Environment emptyEnv;
    private Environment env;

    @Before
    public void setUp() {
        emptyEnv = new Environment();
        env = new Environment();
        env.set(new Variable("x"), 5);
    }

    @Test
    public void isCompoundConstant() {
        Constant x1 = new Constant(1);
        assertFalse(x1.isCompound());
    }

    @Test
    public void isCompoundVariable() {
        Variable x = new Variable("x");
        assertFalse(x.isCompound());
    }

    @Test
    public void isCompoundSum() {
        Sum s = new Sum(new Constant(2), new Constant(3));
        assertTrue(s.isCompound());
    }

    @Test
    public void evalConstant() {
        Constant x1 = new Constant(1);
        assertEquals(1, x1.eval(emptyEnv));
    }


    @Test(expected= Environment.UndefinedVariableException.class)
    public void evalVariableNotFound() throws Environment.UndefinedVariableException{
        Variable x = new Variable("x");
        int v = x.eval(emptyEnv);
    }

    @Test
    public void evalVariable() throws Environment.UndefinedVariableException {
        Variable x = new Variable("x");
        assertEquals(5, x.eval(env));
    }

    @Test
    public void evalSumOfConsts() throws Environment.UndefinedVariableException {
        Sum s = new Sum(new Constant(2), new Constant(3));
        assertEquals(5, s.eval(emptyEnv));
    }

    @Test
    public void evalSumOfVars() throws Environment.UndefinedVariableException {
        Sum s = new Sum(new Variable("x"), new Variable("x"));
        assertEquals(10, s.eval(env));
    }

    @Test
    public void variableGetVarname() {
        Variable x = new Variable("x");
        assertEquals("x", x.getVariableName());
    }

}
