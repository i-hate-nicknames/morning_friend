package com.domain.nvm.morningfriend.features.puzzle.equation.data;

public class Sum implements Expression {

    public Sum(Expression a, Expression b) {

    }

    @Override
    public int eval(Environment env) {
        return 0;
    }

    @Override
    public boolean isCompound() {
        return false;
    }
}
