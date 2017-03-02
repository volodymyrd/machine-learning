package com.gmail.volodymyrdotsenko.ml.libs.functions.lossfunctions;

import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

/**
 * Created by Volodymyr Dotsenko on 2/22/17.
 */
public interface ILossFunction {
    double execute(Matrix calculatedOutput, Matrix output);
}
