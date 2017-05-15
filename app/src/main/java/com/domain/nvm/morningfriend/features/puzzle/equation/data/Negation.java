package com.domain.nvm.morningfriend.features.puzzle.equation.data;

public class Negation implements Expression {

    private Expression negand;

    public Negation(Expression e) {
        negand = e;
    }

    public Expression getNegand() {
        return negand;
    }

    @Override
    public int eval(Environment env) throws Environment.UndefinedVariableException {
        return -1 * negand.eval(env);
    }

    @Override
    public boolean isCompound() {
        return true;
    }

    @Override
    public String toString() {
        String negandStr = negand.toString();
        if (negand.isCompound()) {
            return "-(" + negandStr + ")";
        }
        else {
            return "-" + negandStr;
        }
    }
}
