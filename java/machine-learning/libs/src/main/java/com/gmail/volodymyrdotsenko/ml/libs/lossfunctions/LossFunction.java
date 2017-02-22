package com.gmail.volodymyrdotsenko.ml.libs.lossfunctions;

import com.gmail.volodymyrdotsenko.ml.libs.lossfunctions.functions.LossMSE;

/**
 * Created by Volodymyr Dotsenko on 2/22/17.
 */
public enum LossFunction {
    MSE,
    L1,
    XENT,
    MCXENT,
    SQUARED_LOSS,
    RECONSTRUCTION_CROSSENTROPY,
    NEGATIVELOGLIKELIHOOD,
    COSINE_PROXIMITY,
    HINGE,
    SQUARED_HINGE,
    KL_DIVERGENCE,
    MEAN_ABSOLUTE_ERROR,
    L2,
    MEAN_ABSOLUTE_PERCENTAGE_ERROR,
    MEAN_SQUARED_LOGARITHMIC_ERROR,
    POISSON;

    public ILossFunction getILossFunction() {
        switch (this) {
            case MSE:
            case SQUARED_LOSS:
                return new LossMSE();
            case L1:
                //return new LossL1();
            case XENT:
                //return new LossBinaryXENT();
            case MCXENT:
                //return new LossMCXENT();
            case KL_DIVERGENCE:
            case RECONSTRUCTION_CROSSENTROPY:
                //return new LossKLD();
            case NEGATIVELOGLIKELIHOOD:
                //return new LossNegativeLogLikelihood();
            case COSINE_PROXIMITY:
                //return new LossCosineProximity();
            case HINGE:
                //return new LossHinge();
            case SQUARED_HINGE:
                //return new LossSquaredHinge();
            case MEAN_ABSOLUTE_ERROR:
                //return new LossMAE();
            case L2:
                //return new LossL2();
            case MEAN_ABSOLUTE_PERCENTAGE_ERROR:
                //return new LossMAPE();
            case MEAN_SQUARED_LOGARITHMIC_ERROR:
                //return new LossMSLE();
            case POISSON:
                //return new LossPoisson();
            default:
                //Custom, RMSE_XENT
                throw new UnsupportedOperationException("Unknown or not supported loss function: " + this);
        }
    }
}
