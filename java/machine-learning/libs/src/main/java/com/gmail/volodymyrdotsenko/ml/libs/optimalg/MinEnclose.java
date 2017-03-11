package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import com.gmail.volodymyrdotsenko.ml.libs.optimalg.utils.FunctionOnLine;
import com.gmail.volodymyrdotsenko.ml.libs.optimalg.utils.MinParab;

import java.util.Map;

/**
 * A class trying to find an interval along a straight line in n-dimensional space which contains
 * the minimum on that line of a function in n-space.
 */
public final class MinEnclose implements IOptimizationAlgorithm {
    private final static double ZERO = 0., GSPAR = 1.618034, FACMAG = 10.;
    private final static int MAXSTP = 1000;

    private boolean converged;
    private int nstep;

    private double xa, xb, xc, ya, yb, yc;
    private FunctionOnLine fol;

    /**
     * @param parameters {@link Map} of parameters:
     *                   <p>{@link ParameterType#LEFT_INTERVAL_BOUNDARY} {@link Double} value corresponding
     *                   to a point defining one end the interval comprising the minimum.
     *                   <p>{@link ParameterType#RIGHT_INTERVAL_BOUNDARY} {@link Double} value corresponding
     *                   to a point defining the other end the interval comprising the minimum.
     *                   <p> {@link ParameterType#INITIAL_POINT} {@link Matrix} point in n-space
     *                   <p> {@link ParameterType#DIRECTION} {@link Matrix} direction in n-space.
     *                   Straight line is given by x = x0 + a * xdir, where a is a running scalar variable.
     *                   <p> {@link ParameterType#MAXIMAL_STEP_NUMBER} {@link Integer} maximal step number;
     *                   if a value <= 0 is used, then 1000 is taken instead.
     *                   <p> {@link ParameterType#FUNCTION} user function which must implement {@link IFunction}.
     */
    @Override
    public void compute(Map<ParameterType, Object> parameters) {
        xa = (double) parameters.get(ParameterType.LEFT_INTERVAL_BOUNDARY);
        xb = (double) parameters.get(ParameterType.RIGHT_INTERVAL_BOUNDARY);
        Matrix x0 = (Matrix) parameters.get(ParameterType.INITIAL_POINT);
        Matrix xdir = (Matrix) parameters.get(ParameterType.DIRECTION);
        int nst = (int) parameters.get(ParameterType.MAXIMAL_STEP_NUMBER);
        IFunction function = (IFunction) parameters.get(ParameterType.FUNCTION);

        fol = new FunctionOnLine(x0, xdir, function);
        ya = fol.getFunctionOnLine(xa);
        yb = fol.getFunctionOnLine(xb);
        if (nst <= 0) nst = MAXSTP;
        if (yb > ya) {
            // exchange a and b
            double buf = xa;
            xa = xb;
            xb = buf;
            buf = ya;
            ya = yb;
            yb = buf;
        }
        xc = xb + GSPAR * (xb - xa);
        yc = fol.getFunctionOnLine(xc);
        int istep = 0;
        while (yb >= yc && istep < nst) {
            istep++;
            // step was still downwards
            double xend = xb + FACMAG * (xc - xb);
            double xm = MinParab.getPositionOfExtremum(xa, ya, xb, yb, xc, yc);
            double ym = fol.getFunctionOnLine(xm);
            if ((xm - xb) * (xc - xm) > ZERO) {
                // case (a): xm is between xb and xc
                if (ym < yc) {
                    // case (a1): minimum is between xb and xc
                    xa = xb;
                    ya = yb;
                    xb = xm;
                    yb = ym;
                } else if (ym > yb) {
                    // case (a2): minimum is between xa and xm
                    xc = xm;
                    yc = ym;
                } else {
                    // case (a3): there was no minimum, go on
                    xm = xc + GSPAR * (xc - xb);
                    extend(xm);
                }
            } else if ((xc - xm) * (xm - xend) > ZERO) {
                // case (b): xm is between xc and xend
                if (ym < yc) {
                    // case (b2): there was no minimum, go on
                    xm = xc + GSPAR * (xc - xb);
                    extend(xm);
                } else {
                    // case (b1): minimum is between xb and xm
                    xa = xb;
                    ya = yb;
                    xb = xc;
                    yb = yc;
                    xc = xm;
                    yc = ym;
                }
            } else if ((xm - xend) * (xend - xc) >= ZERO) {
                // case (c): xm is beyond xend
                xm = xend;
                extend(xm);
            } else {
                // case (d): normally should not happen, go on
                xm = xc + GSPAR * (xc - xb);
                extend(xm);
            }
        }
        if (istep == nst) {
            this.nstep = -1;
            converged = false;
        } else {
            this.nstep = istep;
            converged = true;
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
     * @return 3 values (xa, xb, xc) of the variable a determining the interval containing the minimum.
     */
    public double[] getXValues() {
        return new double[]{xa, xb, xc};
    }

    /**
     * @return the  function values (ya, yb, yc) of at the points given by (xa, xb, xc).
     */
    public double[] getYValues() {
        return new double[]{ya, yb, yc};
    }

    private void extend(double xt) {
        xa = xb;
        ya = yb;
        xb = xc;
        yb = yc;
        xc = xt;
        yc = fol.getFunctionOnLine(xc);
    }
}