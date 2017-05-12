package com.domain.nvm.morningfriend.features.puzzle.equation.data;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private Map<String, Integer> vars;

    public Environment() {
        vars = new HashMap<>();
    }

    public void set(Variable x, int value) {
        vars.put(x.getVariableName(), value);
    }

    public int get(Variable x) throws UndefinedVariableException {
        if (!has(x)) {
            throw new UndefinedVariableException();
        }
        return vars.get(x.getVariableName());
    }

    public boolean has(Variable x) {
        return vars.containsKey(x.getVariableName());
    }

    public static class UndefinedVariableException extends Exception {}
}
