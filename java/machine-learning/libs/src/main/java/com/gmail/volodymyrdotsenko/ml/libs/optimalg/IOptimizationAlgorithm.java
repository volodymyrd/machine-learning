package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import java.util.Map;

/**
 *
 */
public interface IOptimizationAlgorithm {
    public enum ParameterType {
        INITIAL_POINT,
        LEFT_INTERVAL_BOUNDARY,
        RIGHT_INTERVAL_BOUNDARY,
        MAXIMAL_STEP_NUMBER,
        EPSILON_ACCURACY,
        FUNCTION,
        GRADIENT,
        DIRECTION
    }

    /**
     * Compute algorithm
     *
     * @param parameters list of parameters specific for each algorithm implementation
     */
    void compute(Map<ParameterType, Object> parameters);

    /**
     * @return true if iteration successful, false otherwise.
     */
    boolean hasConverged();

    /**
     * @return the number of iteration steps needed, -1 if iteration failed.
     */
    int getSteps();
}
