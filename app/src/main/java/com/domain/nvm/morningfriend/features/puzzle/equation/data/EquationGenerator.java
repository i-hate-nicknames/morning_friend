package com.domain.nvm.morningfriend.features.puzzle.equation.data;

import com.domain.nvm.morningfriend.features.puzzle.Puzzle;

import java.util.Random;

public class EquationGenerator {

    // max value of generated constants
    private static final int MAX_CONSTANT = 15;
    // min value will be dependent on difficulty, roughly base + step * difficulty
    private static final int MIN_CONSTANT_BASE = 1;
    private static final int MIN_CONSTANT_STEP = 2;

    // the side size will roughly be base + step * difficulty
    private static final int LEFT_SIZE_BASE = 4;
    private static final int LEFT_SIZE_STEP = 1;
    private static final int RIGHT_SIZE_BASE = 1;
    private static final int RIGHT_SIZE_STEP = 2;

    private Random rand;
    private int diffValue;

    public EquationGenerator() {
        rand = new Random();
    }

    public Equation generate(Puzzle.Difficulty diff) {
        // generate unknown to be some random number
        diffValue = diff.ordinal();
        int x = generateConstant();
        Environment env = new Environment();
        env.set(Equation.getUnknown(), x);
        try {
            // expand left side of the equation with random addends
            // subtract 2: one for initial x and one for balancing constant at the end
            Expression left = extendWithAddends(Equation.getUnknown(), getLeftSideLength()-2);
            int leftValue = left.eval(env);
            Expression right = new Constant(generateConstant());
            // expand right side with random addends, subtract one for initial constant
            right = extendWithAddends(right, getRightSideLength()-1);
            int rightValue = right.eval(env);
            // now we have generated two sides of equation that are most likely not equal
            // so balance it by finding the difference and adding/subtracting it from the left side
            if (leftValue > rightValue) {
                left = Sum.makeSubtraction(left, new Constant(leftValue-rightValue));
            }
            else if (leftValue < rightValue) {
                left = new Sum(left, new Constant(rightValue-leftValue));
            }
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
            Expression ext = new Constant(generateConstant());
            if (rand.nextBoolean()) {
                ext = new Negation(ext);
            }
            result = new Sum(result, ext);
        }
        return result;
    }

    /**
     * Get number of subexpressions in the left side of equation depending on difficulty
     * @return
     */
    private int getLeftSideLength() {
        return LEFT_SIZE_BASE + LEFT_SIZE_STEP * diffValue;
    }

    /**
     * Get number of subexpressions in the right side of equation depending on difficulty
     * @return
     */
    private int getRightSideLength() {
        return RIGHT_SIZE_BASE + RIGHT_SIZE_STEP * diffValue;
    }

    private int generateConstant() {
        int min = MIN_CONSTANT_BASE + MIN_CONSTANT_STEP * diffValue;
        return rand.nextInt(MAX_CONSTANT - min) + min;
    }
}
