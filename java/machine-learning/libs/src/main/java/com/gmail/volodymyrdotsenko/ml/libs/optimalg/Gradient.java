package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

/**
 * A class computing the gradient vector of a function of n variables
 */
public class Gradient {
    private static double DELTA = 1.E-11, CUT = 1.E-9;

    final IFunction function;
    final int[] list;
    Matrix grad, x;
    int il, n, nred;
    double arg, del, fm, fp, sav;

    public Gradient(IFunction function, int[] list) {
        this.function = function;
        this.list = list;
        this.n = list.length;
        this.nred = 0;
        for(int i = 0; i < this.n; i++){
            if(list[i] == 1) this.nred++;
        }
        grad = Matrix.zero(nred, 1);
    }
}
