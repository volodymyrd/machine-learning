package com.gmail.volodymyrdotsenko.ml.libs.activation.functions;

import com.gmail.volodymyrdotsenko.ml.libs.activation.BaseActivationFunction;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

/**
 * Created by Volodymyr Dotsenko on 2/15/17.
 */
public class ActivationSigmoid extends BaseActivationFunction {
    @Override
    public Matrix execute(Matrix input) {
        Matrix val = input.multiply(-1).exp().add(1).invertElements();
        return val;
    }
}