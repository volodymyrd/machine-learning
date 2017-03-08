package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import java.util.Map;

/**
 * A class determining the minimum of a function along a straight line in n-dimensional space
 * according to combination the method of the golden section and of quadratic interpolation
 */
public final class MinCombined implements IOptimizationAlgorithm {
    @Override
    public void compute(Map<ParameterType, Object> parameters) {

    }

    /**
     * @return the postion of the minimum.
     */
    public double getX() {
        return x;
    }

    /**
     * @return the function value at the minimum.
     */
    public double getY() {
        return fx;
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
