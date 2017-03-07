package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import java.util.Map;

/**
 *
 */
public interface IOptimizationAlgorithm {
    public enum ParameterType {
        INITIAL_POINT,
        MAXIMAL_STEP_NUMBER,
        EPSILON_ACCURACY,
        FUNCTION,
        GRADIENT

    }

    void compute(Map<ParameterType, Object> parameters);
}
