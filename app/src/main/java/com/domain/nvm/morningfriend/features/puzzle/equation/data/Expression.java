package com.domain.nvm.morningfriend.features.puzzle.equation.data;

public interface Expression {
    int eval(Environment env);
    boolean isCompound();
}
