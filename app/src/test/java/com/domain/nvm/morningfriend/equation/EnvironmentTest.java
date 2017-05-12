package com.domain.nvm.morningfriend.equation;

import com.domain.nvm.morningfriend.features.puzzle.equation.data.Environment;
import com.domain.nvm.morningfriend.features.puzzle.equation.data.Variable;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnvironmentTest {

    private Environment env;

    @Before
    public void setUp() {
        env = new Environment();
    }

    @Test
    public void has() {
        env.set(new Variable("x"), 5);
        assertFalse("Environment shouldn't have variable y", env.has(new Variable("y")));
        assertTrue("Environment should have variable x", env.has(new Variable("x")));
    }

    @Test
    public void get() throws Environment.UndefinedVariableException {
        env.set(new Variable("x"), 5);
        assertEquals(5, env.get(new Variable("x")));
    }
}
