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
    }
}
