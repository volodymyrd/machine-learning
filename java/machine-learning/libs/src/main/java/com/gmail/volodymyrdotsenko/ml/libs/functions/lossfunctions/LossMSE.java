package com.gmail.volodymyrdotsenko.ml.libs.functions.lossfunctions;

import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

/**
 * Mean Squared Error loss function: L = 1/(2N) sum_i (actual_i - predicted)^2
 *
 * @author Volodymyr Dotsenko
 */
public class LossMSE implements ILossFunction {
    @Override
    public double getValue(Matrix predictedOutput, Matrix output) {
        long nOutPutRows = output.rows();
        long nPredictedRows = predictedOutput.rows();

        double s = 0;

        if (nOutPutRows == 1) {
            for (int i = 1; i <= nPredictedRows; i++) {
                Matrix predictedOutputRow = predictedOutput.getRowsRange(i - 1, i);
                Matrix diff = predictedOutputRow.subtract(output);
                s += diff.multiply(diff.transpose()).get(0, 0);
            }
        } else if (nOutPutRows == nPredictedRows) {
            Matrix diff = predictedOutput.subtract(output);
            for (int i = 1; i <= nPredictedRows; i++) {
                Matrix r = diff.getRowsRange(i - 1, i);
                s += r.multiply(r.transpose()).get(0, 0);
            }
        } else {
            throw new IllegalArgumentException("The numbers of the rows in the 'output' matrix " +
                    "must be equal 1 or the same as in the 'predictedOutput' matrix");
        }

        return s / (2.0 * nPredictedRows);
    }
}