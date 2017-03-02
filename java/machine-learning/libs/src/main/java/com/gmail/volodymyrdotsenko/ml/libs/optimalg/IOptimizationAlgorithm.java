package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;

import java.util.Map;

/**
 *
 */
public interface IOptimizationAlgorithm {
    public enum ParameterType {
        INITIAL_POINT,
        MAXIMAL_STEP_NUMBER,
        EPSILON_ACCURACY

    }

    void compute(IFunction function, Map<ParameterType, Object> parameters);
}
