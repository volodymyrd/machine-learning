package com.gmail.volodymyrdotsenko.ml.libs.functions.lossfunctions;

import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

/**
 *
 */
public interface ILossFunction {
    double getValue(Matrix calculatedOutput, Matrix output);
}
