package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import com.gmail.volodymyrdotsenko.ml.libs.optimalg.utils.FunctionOnLine;

import java.util.HashMap;
import java.util.Map;

/**
 * A class determining the minimum of a function along a straight line in n-dimensional space.
 * The minimum is first enclosed and then found according to combination the method of the golden
 * section and of quadratic interpolation.
 */
public final class MinDir implements IOptimizationAlgorithm {
    private final static double DELTAX = 1.E-3, CUT = 0.1, EPSDEF = 1.E-18;
    private final static int MAXSTP = 1000;

    private boolean converged;
    private int nstep;
    private Matrix xmin;
    private double fc;

    /**
     * @param parameters {@link Map} of parameters:
     *                   <p> {@link ParameterType#INITIAL_POINT} {@link Matrix} point in n-space
     *                   <p> {@link ParameterType#DIRECTION} {@link Matrix} direction in n-space.
     *                   Straight line is given by x = x0 + a * xdir, where a is a running scalar variable.
     *                   <p> {@link ParameterType#MAXIMAL_STEP_NUMBER} {@link Integer} maximal step number;
     *                   if a value <= 0 is used, then 1000 is taken instead.
     *                   <p> {@link ParameterType#EPSILON_ACCURACY} {@link Double} accuracy; if a value <= 0.
     *                   is used, then 1,E-8 is taken instead.
     *                   <p> {@link ParameterType#FUNCTION} user function which must implement {@link IFunction}.
     */
    @Override
    public void compute(Map<ParameterType, Object> parameters) {
        Matrix x0 = (Matrix) parameters.get(ParameterType.INITIAL_POINT);
        Matrix xdir = (Matrix) parameters.get(ParameterType.DIRECTION);
        double eps = (double) parameters.get(ParameterType.EPSILON_ACCURACY);
        int nst = (int) parameters.get(ParameterType.MAXIMAL_STEP_NUMBER);
        IFunction function = (IFunction) parameters.get(ParameterType.FUNCTION);
        if (eps <= 0.) eps = EPSDEF;
        if (nst <= 0) nst = MAXSTP;
        FunctionOnLine fol = new FunctionOnLine(x0, xdir, function);
        double xa = 0.;
        double xb = CUT * DELTAX;

        Map<ParameterType, Object> params = new HashMap<>();
        params.put(ParameterType.LEFT_INTERVAL_BOUNDARY, xa);
        params.put(ParameterType.RIGHT_INTERVAL_BOUNDARY, xb);
        params.put(ParameterType.INITIAL_POINT, x0);
        params.put(ParameterType.DIRECTION, xdir);
        params.put(ParameterType.FUNCTION, function);
        params.put(ParameterType.MAXIMAL_STEP_NUMBER, nst);
        MinEnclose me = new MinEnclose();
        me.compute(params);
        params = null;

        if (me.hasConverged()) {
            double[] xValues = me.getXValues();
            if (xValues[2] < xValues[0]) {
                double dum = xValues[2];
                xValues[2] = xValues[0];
                xValues[0] = dum;
            }

            params = new HashMap<>();
            params.put(ParameterType.LEFT_INTERVAL_BOUNDARY, xValues[0]);
            params.put(ParameterType.RIGHT_INTERVAL_BOUNDARY, xValues[2]);
            params.put(ParameterType.INITIAL_POINT, x0);
            params.put(ParameterType.DIRECTION, xdir);
            params.put(ParameterType.FUNCTION, function);
            params.put(ParameterType.EPSILON_ACCURACY, eps);
            params.put(ParameterType.MAXIMAL_STEP_NUMBER, nst);
            MinCombined mc = new MinCombined();
            mc.compute(params);
            params = null;

            if (mc.hasConverged()) {
                double c = mc.getX();
                fc = mc.getMinimum();
                xmin = x0.add(xdir.multiply(c));
                converged = true;
            } else {
                converged = false;
                xmin = Matrix.zero(x0.rows(), 1);
                fc = 0.;
            }

            nstep = me.getSteps() + mc.getSteps();
        } else {
            converged = false;
            nstep = -1;
        }
    }

    @Override
    public boolean hasConverged() {
        return converged;
    }

    @Override
    public int getSteps() {
        return nstep;
    }

    /**
     * @return the postion of the minimum.
     */
    public Matrix getMinPosition() {
        return xmin;
    }

    /**
     * @return the function value at the minimum.
     */
    public double getMinimum() {
        return fc;
    }
}
