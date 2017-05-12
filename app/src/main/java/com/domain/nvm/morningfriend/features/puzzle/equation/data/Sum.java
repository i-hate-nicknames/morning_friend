package com.domain.nvm.morningfriend.features.puzzle.equation.data;

public class Sum implements Expression {

    private Expression a, b;

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
}
