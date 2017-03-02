package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

import java.util.Map;

/**
 * A class determining the minimum of a function in n-dimensional space by the method of conjugate directions
 */
public final class MinCjg implements IOptimizationAlgorithm {

    private static double EPSDEF = 1.E-12, T = 1.E-18;
    private static int MAXSTP = 1000;
    private double a, fmin, fminl, epsilon, gamma, test, s;
    private int imax, ired, istep, lstep, n, nstep, nred;
    private boolean converged;
    private int[] list;
    private Matrix x0, d, g, gr, h, hfull;
    private IFunction function;
    //private MinDir md;
    //private AuxGrad grad;

    public MinCjg() {
    }

    @Override
    public void compute(IFunction function, Map<ParameterType, Object> parameters) {
        this.function = function;
        this.x0 = (Matrix) parameters.get(ParameterType.INITIAL_POINT);
        this.nstep = (int) parameters.get(ParameterType.MAXIMAL_STEP_NUMBER);
        this.epsilon = (double) parameters.get(ParameterType.EPSILON_ACCURACY);

        n = (int) this.x0.rows();
        nred = n;

        compute();
    }

    private void compute() {
        fmin = function.execute(x0);
        // Initially directions g and h are identical to gradient at x0
        grad = new AuxGrad(muf, list);
        g = grad.getGradient(x0);
        g = g.multiply(-1.);
        h = new DatanVector(g);
        // iteration
        loop:
        for (istep = 1; istep <= nst; istep++) {
            lstep = istep;
            if (istep > 1) {
                if (Math.abs(fminl - fmin) < eps * Math.abs(fmin) + T) {
                    nstep = istep;
                    converged = true;
                    break loop;
                }
                fminl = fmin;
            }
            hfull = new DatanVector(n);
            hfull.putSubvector(h, list);
            if (hfull.dot(hfull) < eps) {
                converged = true;
                break loop;
            }
            md = new MinDir(x0, hfull, nst, eps, muf);
            if (!md.hasConverged()) {
                converged = false;
                break loop;
            }
// x0 is position of minimum along direction h
            x0 = md.getMinPosition();
            fmin = md.getMinimum();
// gr is gradient at x0
            gr = grad.getGradient(x0);
// compute next set of directions g and h
            gr = gr.multiply(-1.);
            d = gr.sub(g);
            s = d.dot(gr);
            a = g.dot(g);
            if (a < eps) {
                converged = true;
                break loop;
            }
            gamma = s / a;
            h = h.multiply(gamma);
            h = h.add(gr);
            g = new DatanVector(gr);
        }
        if (istep == nst + 1 || !converged) {
            nst = -1;
        } else {
            nst = istep;
        }
    }
}
