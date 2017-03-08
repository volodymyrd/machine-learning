package com.gmail.volodymyrdotsenko.ml.libs.optimalg.utils;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

/**
 * A class computing the value of a function at a given point on straight line in n-dimensional space.
 */
public final class FunctionOnLine {
    private final IFunction function;
    private final Matrix x0, xdir;

    /**
     * @param x0       point in n-space
     * @param xdir     direction in n-space. Straight line is given by x = x0 + a * xdir,
     *                 where a is a running scalar variable.
     * @param function user function which must be implement {@link IFunction}.
     */
    public FunctionOnLine(Matrix x0, Matrix xdir, IFunction function) {
        this.function = function;
        this.x0 = x0;
        this.xdir = xdir;
    }

    /**
     * @return the function value for a given value of the parameter a.
     */
    public double getFunctionOnLine(double a) {
        return function.getValue(x0.add(xdir.multiply(a)));
    }
}
