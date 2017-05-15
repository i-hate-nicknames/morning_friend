package com.domain.nvm.morningfriend.equation;

import com.domain.nvm.morningfriend.features.puzzle.equation.data.Constant;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.Environment;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.Negation;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.Sum;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.Variable;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExpressionTest {

    private static Environment emptyEnv;
    private static Environment env;
    private static Constant const1, const2, const3;
    private static Variable varX;

    /*
     * Since all our data is immutable we may as well
     * initialize it once at class level for all tests
     */
    @BeforeClass
    public static void setUp() {
        emptyEnv = new Environment();
        varX = new Variable("x");
        env = new Environment();
        env.set(varX, 5);
        const1 = new Constant(1);
        const2 = new Constant(2);
        const3 = new Constant(3);
    }

    @Test
    public void isCompoundConstant() {
        assertFalse(const1.isCompound());
    }

    @Test
    public void isCompoundVariable() {
        assertFalse(varX.isCompound());
    }

    @Test
    public void isCompoundSum() {
        Sum s = new Sum(const2, const3);
        assertTrue(s.isCompound());
    }

    @Test
    public void isCompoundNegation() {
        Negation n = new Negation(const2);
        assertTrue(n.isCompound());
    }

    @Test
    public void evalConstant() {
        assertEquals(1, const1.eval(emptyEnv));
    }


    @Test(expected= Environment.UndefinedVariableException.class)
    public void evalVariableNotFound() throws Environment.UndefinedVariableException{
        int v = varX.eval(emptyEnv);
    }

    @Test
    public void evalVariable() throws Environment.UndefinedVariableException {
        assertEquals(5, varX.eval(env));
    }

    @Test
    public void evalSumOfConsts() throws Environment.UndefinedVariableException {
        Sum s = new Sum(const2, const3);
        assertEquals(5, s.eval(emptyEnv));
    }

    @Test
    public void evalSumOfVars() throws Environment.UndefinedVariableException {
        Sum s = new Sum(varX, varX);
        assertEquals(10, s.eval(env));
    }

    @Test
    public void evalSimpleNegation() throws Environment.UndefinedVariableException {
        Negation n = new Negation(const3);
        assertEquals(-3, n.eval(emptyEnv));
    }

    @Test
    public void evalNegationOfSum() throws Environment.UndefinedVariableException {
        Negation n = new Negation(new Sum(const2, const3));
        assertEquals(-5, n.eval(emptyEnv));
    }

    @Test
    public void evalSubtraction() throws Environment.UndefinedVariableException {
        Sum s = Sum.makeSubtraction(const2, const3);
        assertEquals(-1, s.eval(emptyEnv));
    }

    @Test
    public void variableGetVarname() {
        assertEquals("x", varX.getVariableName());
    }

    @Test
    public void toStringConst() {
        assertEquals("1", const1.toString());
    }

    @Test
    public void toStringVariable() {
        assertEquals("x", varX.toString());
    }

    @Test
    public void toStringSumSimple() {
        Sum s = new Sum(const2, const3);
        assertEquals("2 + 3", s.toString());
    }

    @Test
    public void toStringSumWithVar() {
        Sum s = new Sum(const2, varX);
        assertEquals("2 + x", s.toString());
    }

    @Test
    public void toStringSumCompound() {
        Sum s = new Sum(const2, varX);
        assertEquals("2 + x + 2 + x", new Sum(s, s).toString());
    }

    @Test
    public void toStringNegationOfConstant() {
        Negation n = new Negation(const1);
        assertEquals("-1", n.toString());
    }

    @Test
    public void toStringNegationOfVariable() {
        Negation n = new Negation(varX);
        assertEquals("-x", n.toString());
    }

    @Test
    public void toStringNegationOfSum() {
        Negation n = new Negation(new Sum(const2, const3));
        assertEquals("-(2 + 3)", n.toString());
    }

}
