package com.gmail.volodymyrdotsenko.ml.libs.activation;

import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;

import java.io.Serializable;

/**
 * Interface for implementing custom activation functions
 */
public interface IActivation extends Serializable {
    Matrix execute(Matrix input);
}
