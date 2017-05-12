package com.domain.nvm.morningfriend.features.puzzle.equation.data;

public class Variable implements Expression {

    private String varName;

    public Variable(String varName) {
        this.varName = varName;
    }

    @Override
    public int eval(Environment env) throws Environment.UndefinedVariableException {
        return env.get(this);
    }

    @Override
    public boolean isCompound() {
        return false;
    }

    public String getVariableName() {
        return varName;
    }

}
