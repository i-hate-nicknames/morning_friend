package com.domain.nvm.morningfriend.features.puzzle.equation.data;

import com.domain.nvm.morningfriend.features.puzzle.Puzzle;

import java.util.Random;

public class EquationGenerator {

    private static final int MAX_CONSTANT = 15;
    private static final int LEFT_SIZE = 5;

    private Random rand;

    public EquationGenerator() {
        rand = new Random();
    }

    public Equation generate(Puzzle.Difficulty difficulty) {
        int x = rand.nextInt(MAX_CONSTANT) + 1;
        Environment env = new Environment();
        env.set(Equation.getUnknown(), x);
        Expression left = extendWithAddends(Equation.getUnknown(), LEFT_SIZE);
        try {
            int leftValue = left.eval(env);
            Expression right = new Constant(leftValue);
            return new Equation(left, right, env);
        }
        catch (Environment.UndefinedVariableException ex) {
            throw new IllegalStateException("Expression generated has unbound variables");
        }
    }

    /**
     * Extend given expression by n random addends
     * @param initial
     * @param n
     * @return
     */
    private Expression extendWithAddends(Expression initial, int n) {
        Expression result = initial;
        for (int i = 0; i < n; i++) {
            Expression ext = new Constant(rand.nextInt(MAX_CONSTANT)+1);
            if (rand.nextBoolean()) {
                ext = new Negation(ext);
            }
            result = new Sum(result, ext);
        }
        return result;
    }
}
