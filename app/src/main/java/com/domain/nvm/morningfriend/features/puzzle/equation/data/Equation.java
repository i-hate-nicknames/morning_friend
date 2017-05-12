package com.domain.nvm.morningfriend.features.puzzle.equation.data;

public class Equation {

    private static final String UNKNOWN_NAME = "x";

    private static Variable unknown;

    private Expression left, right;
    private Environment env;

    public static Variable getUnknown() {
        if (unknown == null) {
            unknown = new Variable(UNKNOWN_NAME);
        }
        return unknown;
    }

    public Equation(Expression left, Expression right, int unknownValue) {
        this.left = left;
        this.right = right;
        this.env = new Environment();
        this.env.set(getUnknown(), unknownValue);
        assert(holdsInvariant());
    }

    public Equation(Expression left, Expression right, Environment env) {
        this.left = left;
        this.right = right;
        this.env = env;
        assert(holdsInvariant());
    }

    private boolean holdsInvariant() {
        try {
            return left.eval(env) == right.eval(env);
        }
        catch (Environment.UndefinedVariableException ex) {
            return false;
        }
    }

    public boolean checkSolution(int val) {
        try {
            return env.get(getUnknown()) == val;
        }
        catch (Environment.UndefinedVariableException ex) {
            throw new IllegalStateException("Unknown is not present in the equation environment");
        }
    }

    @Override
    public String toString() {
        return left.toString() + " = " + right.toString();
    }
}
