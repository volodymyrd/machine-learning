package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.functions.IGradient;
import com.gmail.volodymyrdotsenko.ml.libs.functions.IHessian;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.MutableMatrix;

import java.util.Map;

/**
 * A class determining the minimum of a function in n-dimensional space by Marquardt's method of.
 */
public final class MinMar implements IOptimizationAlgorithm {
    private final static double EPSDEF = 1.E-12, LAMBDAS = 1.D - 3, T = 1.E-18;
    private final static int MAXSTP = 1000;

    private Matrix x0;
    private boolean converged;
    private int nst;
    private double fmin;

    /**
     * @param parameters {@link Map} of parameters:
     *                   <p> {@link ParameterType#INITIAL_POINT} {@link Matrix} initial point in n-space.
     *                   <p> {@link ParameterType#MAXIMAL_STEP_NUMBER} {@link Integer} maximal step number.
     *                   Default: if a value <= 0 is used, then 1000 is taken instead.
     *                   <p> {@link ParameterType#EPSILON_ACCURACY} {@link Double} accuracy.
     *                   Default if a value <= 0. is used, then 1,E-8 is taken instead.
     *                   <p> {@link ParameterType#FUNCTION} user function which must implement {@link IFunction}.
     */
    @Override
    public void compute(Map<ParameterType, Object> parameters) {
        converged = true;
        x0 = (Matrix) parameters.get(ParameterType.INITIAL_POINT);
        double eps = (double) parameters.get(ParameterType.EPSILON_ACCURACY);
        nst = (int) parameters.get(ParameterType.MAXIMAL_STEP_NUMBER);
        IFunction function = (IFunction) parameters.get(ParameterType.FUNCTION);
        IGradient gradient = (IGradient) parameters.get(ParameterType.GRADIENT);
        IHessian hessian = (IHessian) parameters.get(ParameterType.HESSIAN);

        if (eps <= 0.) eps = EPSDEF;
        if (nst <= 0) nst = MAXSTP;

        // initial value of minimum function
        double fminl = function.getValue(x0);

        int n = (int) x0.rows();

        // iteration
        int istep = 1;
        double lambda = LAMBDAS;
        for (; istep <= nst; istep++) {
            // compute minimum function for 2 values of lambda
            Matrix g = gradient.getValue(function, x0);
            MutableMatrix hesse = new MutableMatrix(hessian.getValue(function, x0));
            MutableMatrix x1red = new MutableMatrix(n, 1);
            MutableMatrix x2red = new MutableMatrix(n, 1);
            boolean[] ok = {false};
            hesse.marquardt(g, lambda, x1red, x2red, 0., ok);
            if (!ok[0]) {
                converged = false;
                break;
            }
            Matrix x1 = x1red.toMatrix();
            Matrix x2 = x2red.toMatrix();
            x1 = x0.subtract(x1);
            x2 = x0.subtract(x2);
            double f1 = function.getValue(x1);
            double f2 = function.getValue(x2);
            // evaluate results
            boolean newpoint;
            if (f2 <= fminl) {
                // reduce lambda and accept new point
                lambda = 0.1 * lambda;
                x0 = x2;
                fmin = f2;
                newpoint = true;
            } else if (f2 > fminl + T && f1 <= fminl + T) {
                // keep current value of lambda and accept new point
                x0 = x1;
                fmin = f1;
                newpoint = true;
            } else {
                // increase lambda and reject new point
                lambda = 10. * lambda;
                newpoint = false;
            }
            if (newpoint) {
                // test for break=off criterion
                if (Math.abs(fminl - fmin) < eps * Math.abs(fmin) + T) {
                    converged = true;
                    break;
                }
            }
            fminl = fmin;
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

    @Override
    public Matrix getMinPosition() {
        return x0;
    }

    @Override
    public double getMinimum() {
        return fmin;
    }
}
