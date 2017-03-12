package com.gmail.volodymyrdotsenko.ml.libs.functions.hessian;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.functions.IHessian;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

/**
 * A class computing the Hessian matrix of second derivatives of a function of n variables
 */
public final class Hesse implements IHessian {
    private final static double DELTA = 1.E-5, CUT = 1.E-9;

    @Override
    public Matrix getValue(IFunction function, Matrix input) {
        int n = (int) input.rows();

        double[] x = input.toArrayByColumn();
        double[][] hesse = new double[n][n];

        int il = -1;
        for (int i = 0; i < n; i++) {
            int kl = -1;
            il++;
            for (int k = 0; k < n; k++) {
                kl++;
                if (kl < il) {
                    hesse[il][kl] = hesse[kl][il];
                } else {
                    double e = Math.abs(x[i]);
                    if (e < CUT) e = CUT;
                    double deli = DELTA * e;
                    double savi = x[i];
                    double h, fp, fm;
                    if (k == i) {
                        double f = function.getValue(Matrix.vector(x));
                        x[i] = savi + deli;
                        fp = function.getValue(Matrix.vector(x));
                        x[i] = savi - deli;
                        fm = function.getValue(Matrix.vector(x));
                        x[i] = savi;
                        h = ((fp - f) / deli - (f - fm) / deli) / deli;
                        hesse[il][il] = h;
                    } else {
                        e = Math.abs(x[k]);
                        if (e < CUT) e = CUT;
                        double delk = DELTA * e;
                        double savk = x[k];
                        //f = function.getValue(Matrix.vector(x));
                        x[k] = savk + delk;
                        x[i] = savi + deli;
                        fp = function.getValue(Matrix.vector(x));
                        x[i] = savi - deli;
                        fm = function.getValue(Matrix.vector(x));
                        double dfdxp = (fp - fm) / (deli + deli);
                        x[k] = savk - delk;
                        x[i] = savi + deli;
                        fp = function.getValue(Matrix.vector(x));
                        x[i] = savi - deli;
                        fm = function.getValue(Matrix.vector(x));
                        double dfdxm = (fp - fm) / (deli + deli);
                        h = (dfdxp - dfdxm) / (delk + delk);
                        hesse[il][kl] = h;
                    }
                }

            }
        }

        return Matrix.from2Array(hesse);
    }
}
