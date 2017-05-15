package com.domain.nvm.morningfriend.features.puzzle.equation.data;

public class Negation implements Expression {

    public Negation(Expression e) {

    }

    @Override
    public int eval(Environment env) throws Environment.UndefinedVariableException {
        return 0;
    }

    @Override
    public boolean isCompound() {
        return false;
    }
}
