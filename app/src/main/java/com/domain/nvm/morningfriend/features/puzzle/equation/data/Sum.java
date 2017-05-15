package com.domain.nvm.morningfriend.features.puzzle.equation.data;

public class Sum implements Expression {

    private Expression a, b;

    public static Sum makeSubtraction(Expression a, Expression b) {
        return new Sum(a, new Negation(b));
    }

    public Sum(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public int eval(Environment env) throws Environment.UndefinedVariableException {
        return a.eval(env) + b.eval(env);
    }

    @Override
    public boolean isCompound() {
        return true;
    }

    @Override
    public String toString() {
        // we don't have pattern matching in java that's why we do ugly things
        if (b instanceof Negation) {
            return a.toString() + " - " + ((Negation)b).getNegand();
        }
        else {
            return a.toString() + " + " + b.toString();
        }
    }
}
