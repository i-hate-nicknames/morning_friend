package com.domain.nvm.morningfriend.features.puzzle.equation.data;

public class Constant implements Expression {

    private int value;

    public Constant(int value) {
        this.value = value;
    }

    @Override
    public int eval(Environment env) {
        return value;
    }

    @Override
    public boolean isCompound() {
        return false;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
