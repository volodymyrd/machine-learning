package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import java.util.Map;

/**
 * A class determining the minimum of a function in n-dimensional space by the method of conjugate directions
 */
public final class MinCjg implements IOptimizationAlgorithm {

    @Override
    public void compute(Map<ParameterType, Object> parameters) {

    }

    @Override
    public boolean hasConverged() {
        return false;
    }

    @Override
    public int getSteps() {
        return 0;
    }
}
