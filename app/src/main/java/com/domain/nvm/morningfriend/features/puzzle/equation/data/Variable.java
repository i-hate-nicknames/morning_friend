package com.domain.nvm.morningfriend.features.puzzle.equation.data;

public class Variable implements Expression {

    public Variable(String varName) {

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
