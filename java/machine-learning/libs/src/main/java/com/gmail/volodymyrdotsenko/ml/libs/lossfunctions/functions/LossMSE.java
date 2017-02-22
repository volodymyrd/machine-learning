package com.gmail.volodymyrdotsenko.ml.libs.lossfunctions.functions;

import com.gmail.volodymyrdotsenko.ml.libs.lossfunctions.ILossFunction;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

/**
 * Mean Squared Error loss function: L = 1/(2N) sum_i (actual_i - predicted)^2
 *
 * @author Volodymyr Dotsenko
 */
public class LossMSE implements ILossFunction {
    @Override
    public double execute(Matrix predictedOutput, Matrix output) {
        Matrix diff = predictedOutput.subtract(output);
        long m = output.rows();
        double s = 0;
        for (int i = 1; i <= m; i++) {
            Matrix r = diff.getRowsRange(i - 1, i);
            s += r.multiply(r.transpose()).get(0, 0);
        }
        return s / (2.0 * m);
    }
}