package com.gmail.volodymyrdotsenko.ml.libs.functions;

import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

/**
 *
 */
public interface IGradient {
    Matrix getValue(IFunction function, Matrix input);
}
