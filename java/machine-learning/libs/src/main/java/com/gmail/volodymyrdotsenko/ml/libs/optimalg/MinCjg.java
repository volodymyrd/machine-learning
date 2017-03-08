package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.functions.IGradient;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

import java.util.Map;

/**
 * A class determining the minimum of a function in n-dimensional space by the method of conjugate directions
 */
public final class MinCjg implements IOptimizationAlgorithm {

    private final static double EPSDEF = 1.E-12, T = 1.E-18;
    private final static int MAXSTP = 1000;

    private double a, fmin, fminl, epsilon, gamma, test, s;
    private int imax, ired, istep, lstep, n, nstep;
    private boolean converged;
    private Matrix x0, d, g, gr, h, hfull;
    private IFunction function;
    private IGradient gradient;

    public MinCjg() {
    }

    @Override
    public void compute(Map<ParameterType, Object> parameters) {
        this.function = (IFunction) parameters.get(ParameterType.FUNCTION);
        this.gradient = (IGradient) parameters.get(ParameterType.GRADIENT);
        this.x0 = (Matrix) parameters.get(ParameterType.INITIAL_POINT);
        this.nstep = (int) parameters.get(ParameterType.MAXIMAL_STEP_NUMBER);
        this.epsilon = (double) parameters.get(ParameterType.EPSILON_ACCURACY);

        if (epsilon <= 0.) epsilon = EPSDEF;
        if (nstep <= 0) nstep = MAXSTP;

        n = (int) this.x0.rows();

        compute();
    }

    @Override
    public boolean hasConverged() {
        return converged;
    }

    @Override
    public int getSteps() {
        return nstep;
    }

    private void compute() {
        fmin = function.getValue(x0);
        // Initially directions g and h are identical to gradient at x0
        g = gradient.getValue(function, x0);
        g = g.multiply(-1.);
        h = g.copy();
        // iteration
        for (istep = 1; istep <= nstep; istep++) {
            lstep = istep;
            if (istep > 1) {
                if (Math.abs(fminl - fmin) < epsilon * Math.abs(fmin) + T) {
                    converged = true;
                    break;
                }
                fminl = fmin;
            }
            hfull = h.copy();
            if (hfull.transpose().multiply(hfull).get(0, 0) < epsilon) {
                converged = true;
                break;
            }
            MinDir md = new MinDir(x0, hfull, nst, eps, muf);
            if (!md.hasConverged()) {
                converged = false;
                break;
            }
            // x0 is position of minimum along direction h
            x0 = md.getMinPosition();
            fmin = md.getMinimum();
            // gr is gradient at x0
            gr = gradient.getValue(function, x0);
            // compute next set of directions g and h
            gr = gr.multiply(-1.);
            d = gr.subtract(g);
            s = d.transpose().multiply(gr).get(0, 0);
            a = g.transpose().multiply(g).get(0, 0);
            if (a < epsilon) {
                converged = true;
                break;
            }
            gamma = s / a;
            h = h.multiply(gamma);
            h = h.add(gr);
            g = gr.copy();
        }
        if (istep == nstep + 1 || !converged) {
            nstep = -1;
        } else {
            nstep = istep;
        }
    }
}
