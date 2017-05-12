package com.domain.nvm.morningfriend.features.puzzle.equation.data;

public class Constant implements Expression {

    public Constant(int value) {

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
