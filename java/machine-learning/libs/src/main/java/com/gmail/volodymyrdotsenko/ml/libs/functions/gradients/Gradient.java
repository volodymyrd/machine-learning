package com.gmail.volodymyrdotsenko.ml.libs.functions.gradients;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.functions.IGradient;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

/**
 * A class computing the gradient vector of a function of n variables
 */
public class Gradient implements IGradient {
    private static double DELTA = 1.E-11, CUT = 1.E-9;

    @Override
    public Matrix getValue(IFunction function, Matrix input) {
        int vectorLength = (int) input.rows();
        double[] gradient = new double[vectorLength];
        for (int i = 0; i < vectorLength; i++) {
            double[] x = input.extractColumn(0);
            double arg = Math.abs(x[i]);
            if (arg < CUT) arg = CUT;
            double del = DELTA * arg;
            double sav = x[i];
            x[i] = sav + del;
            double fp = function.getValue(Matrix.vector(x));
            x[i] = sav - del;
            double fm = function.getValue(Matrix.vector(x));
            gradient[i] = (fp - fm) / (del + del);
        }

        return Matrix.vector(gradient);
    }
}
