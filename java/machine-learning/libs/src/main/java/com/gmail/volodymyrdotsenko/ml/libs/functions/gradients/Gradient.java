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
        double arg, del, fm, fp, sav;
        int vectorLength = (int) input.rows();
        double[] gradient = new double[vectorLength];
        double[] x = input.extractColumn(0);
        for (int i = 0; i < vectorLength; i++) {
            arg = Math.abs(x[i]);
            if (arg < CUT) arg = CUT;
            del = DELTA * arg;
            sav = x[i];
            x[i] = sav + del;
            fp = function.getValue(Matrix.vector(x));
            x[i] = sav - del;
            fm = function.getValue(Matrix.vector(x));
            gradient[i] = (fp - fm) / (del + del);
        }

        return Matrix.vector(gradient);
    }
}
