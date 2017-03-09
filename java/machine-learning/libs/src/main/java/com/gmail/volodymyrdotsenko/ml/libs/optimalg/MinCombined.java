package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import com.gmail.volodymyrdotsenko.ml.libs.optimalg.utils.FunctionOnLine;

import java.util.Map;

/**
 * A class determining the minimum of a function along a straight line in n-dimensional space
 * according to combination the method of the golden section and of quadratic interpolation
 */
public final class MinCombined implements IOptimizationAlgorithm {
    private final static double C = 0.381966, EPSDEF = 1.E-12, TT = 1.E-15;
    private final static int MAXSTP = 1000;

    private int nst;
    private boolean converged;
    private double x, fx;

    /**
     * @param parameters {@link Map} of parameters:
     *                   <p> {@link ParameterType#INITIAL_POINT} {@link Matrix} point in n-space
     *                   <p> {@link ParameterType#DIRECTION} {@link Matrix} direction in n-space.
     *                   Straight line is given by x = x0 + a * xdir, where a is a running scalar variable.
     *                   <p>{@link ParameterType#LEFT_INTERVAL_BOUNDARY} {@link Double} value corresponding
     *                   to a point defining one end the interval comprising the minimum.
     *                   <p>{@link ParameterType#RIGHT_INTERVAL_BOUNDARY} {@link Double} value corresponding
     *                   to a point defining the other end the interval comprising the minimum.
     *                   <p> {@link ParameterType#MAXIMAL_STEP_NUMBER}       maximal step number;
     *                   if a value <= 0 is used, then 1000 is taken instead.
     *                   <p>{@link ParameterType#EPSILON_ACCURACY} {@link Double} accuracy;
     *                   if a value <= 0. is used, then 1,E-8 is taken instead.
     *                   <p> {@link ParameterType#FUNCTION} user function which must implement {@link IFunction}.
     */
    @Override
    public void compute(Map<ParameterType, Object> parameters) {
        double a = (double) parameters.get(ParameterType.LEFT_INTERVAL_BOUNDARY);
        double b = (double) parameters.get(ParameterType.RIGHT_INTERVAL_BOUNDARY);
        double eps = (double) parameters.get(ParameterType.EPSILON_ACCURACY);
        Matrix x0 = (Matrix) parameters.get(ParameterType.INITIAL_POINT);
        Matrix xdir = (Matrix) parameters.get(ParameterType.DIRECTION);
        nst = (int) parameters.get(ParameterType.MAXIMAL_STEP_NUMBER);
        IFunction function = (IFunction) parameters.get(ParameterType.FUNCTION);
        if (eps <= 0.) eps = EPSDEF;
        if (nst <= 0) nst = MAXSTP;
        FunctionOnLine fol = new FunctionOnLine(x0, xdir, function);
        // initialize x at goldensection position between a and b
        x = a + C * (b - a);
        // initialize e, v, w, fx, fv, fw
        double e = 0.;
        double v = x;
        double w = x;
        fx = fol.getFunctionOnLine(x);
        double fv = fx;
        double fw = fx;
        double xm = 0.5 * (a + b);
        double tol = eps * Math.abs(x) + TT;
        double t2 = 2. * tol;
        int istep = 0;
        double d = 0.;
        double u;
        while (Math.abs(x - xm) > t2 - 0.5 * (b - a) && istep < nst) {
            istep++;
            double p;
            double q;
            double r;
            boolean parstep = false;
            if (Math.abs(e) > tol) {
                // fit parabola
                r = (x - w) * (fx - fv);
                q = (x - v) * (fx - fw);
                p = (x - v) * q - (x - w) * r;
                q = 2. * (q - r);
                if (q > 0.) {
                    p = -p;
                } else {
                    q = -q;
                }
                r = e;
                e = d;
                if (Math.abs(p) < Math.abs(0.5 * q * r) && p > q * (a - x) && p < q * (b - x)) {
                    // use result of paraqbolic fit
                    d = p / q;
                    u = x + d;
                    if ((u - a) < t2 || (b - u) < t2) {
                        // make sure u is not too near a and b
                        d = -tol;
                        if (x < xm) d = tol;
                    }
                    parstep = true;
                }
            }
            if (!parstep) {
                // perform golden section step
                if (x < xm) {
                    e = b - x;
                } else {
                    e = a - x;
                }
                d = C * e;
            }
            // determine point u where function is to be computed,
            // making sure it is not too close to x
            if (Math.abs(d) >= tol) {
                u = x + d;
            } else if (d > 0.) {
                u = x + tol;
            } else {
                u = x - tol;
            }
            double fu = fol.getFunctionOnLine(u);
            // update a, b, v, w, x
            if (fu <= fx) {
                if (u < x) {
                    b = x;
                } else {
                    a = x;
                }
                v = w;
                fv = fw;
                w = x;
                fw = fx;
                x = u;
                fx = fu;
            } else {
                if (u < x) {
                    a = u;
                } else {
                    b = u;
                }
                if (fu <= fw || w == x) {
                    v = w;
                    fv = fw;
                    w = u;
                    fw = fu;
                } else if (fu <= fv || v == x || v == w) {
                    v = u;
                    fv = fu;
                }
            }
            xm = 0.5 * (a + b);
            tol = eps * Math.abs(x) + TT;
            t2 = 2. * tol;
        }
        if (istep == nst) {
            nst = -1;
            converged = false;
        } else {
            nst = istep;
            converged = true;
        }
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
        return converged;
    }

    @Override
    public int getSteps() {
        return nst;
    }
}
