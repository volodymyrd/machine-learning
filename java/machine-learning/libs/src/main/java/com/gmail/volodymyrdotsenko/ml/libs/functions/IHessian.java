package com.gmail.volodymyrdotsenko.ml.libs.functions;

import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;


public interface IHessian {
    /**
     * @param function user function
     * @param input    position in n-space, for which Hessian is to be computed.
     * @return Hessian matrix of second derivatives.
     */
    Matrix getValue(IFunction function, Matrix input);
}
