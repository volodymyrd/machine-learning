package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.functions.IGradient;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

import java.util.HashMap;
import java.util.Map;

/**
 * A class determining the minimum of a function in n-dimensional space by the method of conjugate directions
 */
public final class MinCjg implements IOptimizationAlgorithm {

    private final static double EPSDEF = 1.E-12, T = 1.E-18;
    private final static int MAXSTP = 1000;

    private boolean converged;
    private int nst;
    private Matrix x0;
    private double fmin;

    @Override
    public void compute(Map<ParameterType, Object> parameters) {
        x0 = (Matrix) parameters.get(ParameterType.INITIAL_POINT);
        double eps = (double) parameters.get(ParameterType.EPSILON_ACCURACY);
        nst = (int) parameters.get(ParameterType.MAXIMAL_STEP_NUMBER);
        IFunction function = (IFunction) parameters.get(ParameterType.FUNCTION);
        IGradient gradient = (IGradient) parameters.get(ParameterType.GRADIENT);

        if (eps <= 0.) eps = EPSDEF;
        if (nst <= 0) nst = MAXSTP;

        long n = x0.rows();

        fmin = function.getValue(x0);
        // Initially directions g and h are identical to gradient at x0
        Matrix g = gradient.getValue(function, x0);
        g = g.multiply(-1.);
        Matrix h = g.copy();
        int istep = 1;
        double fminl = 0.;
        for (; istep <= nst; istep++) {
            int lstep = istep;
            if (istep > 1) {
                if (Math.abs(fminl - fmin) < eps * Math.abs(fmin) + T) {
                    converged = true;
                    break;
                }
                fminl = fmin;
            }
            Matrix hfull = h.copy();
            if (hfull.transpose().multiply(hfull).get(0, 0) < eps) {
                converged = true;
                break;
            }

            Map<IOptimizationAlgorithm.ParameterType, Object> params = new HashMap<>();
            params.put(IOptimizationAlgorithm.ParameterType.INITIAL_POINT, x0);
            params.put(IOptimizationAlgorithm.ParameterType.DIRECTION, hfull);
            params.put(IOptimizationAlgorithm.ParameterType.FUNCTION, function);
            params.put(IOptimizationAlgorithm.ParameterType.MAXIMAL_STEP_NUMBER, nst);
            params.put(IOptimizationAlgorithm.ParameterType.EPSILON_ACCURACY, eps);
            MinDir md = new MinDir();
            md.compute(params);
            if (!md.hasConverged()) {
                converged = false;
                break;
            }
            // x0 is position of minimum along direction h
            x0 = md.getMinPosition();
            fmin = md.getMinimum();
            // gr is gradient at x0
            Matrix gr = gradient.getValue(function, x0);
            // compute next set of directions g and h
            gr = gr.multiply(-1.);
            Matrix d = gr.subtract(g);
            double s = d.transpose().multiply(gr).get(0, 0);
            double a = g.transpose().multiply(g).get(0, 0);
            if (a < eps) {
                converged = true;
                break;
            }
            double gamma = s / a;
            h = h.multiply(gamma);
            h = h.add(gr);
            g = gr.copy();
        }
        if (istep == nst + 1 || !converged) {
            nst = -1;
        } else {
            nst = istep;
        }
    }

    @Override
    public boolean hasConverged() {
        return converged;
    }

    @Override
    public int getSteps() {
        return nst;
    }

    /**
     * @return the postion of the minimum.
     */
    public Matrix getMinPosition() {
        return x0;
    }

    /**
     * @return the function value at the minimum.
     */
    public double getMinimum() {
        return fmin;
    }
}
