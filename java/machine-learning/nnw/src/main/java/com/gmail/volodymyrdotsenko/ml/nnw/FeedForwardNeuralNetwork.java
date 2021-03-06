package com.gmail.volodymyrdotsenko.ml.nnw;

import com.gmail.volodymyrdotsenko.ml.libs.activation.Activation;
import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.functions.IGradient;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import com.gmail.volodymyrdotsenko.ml.libs.optimalg.IOptimizationAlgorithm;
import com.gmail.volodymyrdotsenko.ml.libs.optimalg.MinCjg;
import com.gmail.volodymyrdotsenko.ml.libs.optimalg.OptimizationAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class FeedForwardNeuralNetwork extends NeuralNetwork {

    private static Logger logger = LoggerFactory.getLogger(FeedForwardNeuralNetwork.class);

    public static class Builder extends NeuralNetwork.Builder<FeedForwardNeuralNetwork> {

        public Builder() {
            super(new FeedForwardNeuralNetwork());
        }

        @Override
        public Builder activation(Activation activation) {
            return (Builder) super.activation(activation);
        }

        @Override
        public FeedForwardNeuralNetwork build() {
            return super.build();
        }

        @Override
        public <L extends NeuralNetwork.Layer> Builder layer(L layer) {
            return (Builder) super.layer(layer);
        }

        @Override
        public <A extends NeuralNetwork.Algorithm> Builder algorithm(A algorithm) {
            return (Builder) super.algorithm(algorithm);
        }

        public static class AlgorithmBuilder extends NeuralNetwork.Builder.AlgorithmBuilder {

            public AlgorithmBuilder() {
                super(new Algorithm());
            }

            @Override
            AlgorithmBuilder algorithm(OptimizationAlgorithm algorithm) {
                return (AlgorithmBuilder) super.algorithm(algorithm);
            }

            @Override
            AlgorithmBuilder accuracy(double accuracy) {
                return (AlgorithmBuilder) super.accuracy(accuracy);
            }

            @Override
            AlgorithmBuilder maxStepNumber(int maxStepNumber) {
                return (AlgorithmBuilder) super.maxStepNumber(maxStepNumber);
            }
        }

        public static class LayerBuilder extends NeuralNetwork.Builder.LayerBuilder {

            public LayerBuilder() {
                super(new Layer());
            }

            @Override
            LayerBuilder name(String name) {
                return (LayerBuilder) super.name(name);
            }

            @Override
            LayerBuilder activation(Activation activation) {
                return (LayerBuilder) super.activation(activation);
            }

            /**
             * Set the number of neurons in the current layer
             *
             * @param neurons the number of neurons
             * @return LayerBuilder
             */
            LayerBuilder neurons(int neurons) {
                ((Layer) layer).neurons = neurons;
                return this;
            }

            /**
             * Initialize weights of the current layer
             *
             * @param weights the (NI+1)xNO matrix of initial weights, where
             *                NI - the number of the neurons in the previous layer plus 1
             *                NO - the number of the neurons in the current layer
             * @return LayerBuilder
             */
            LayerBuilder weights(Matrix weights) {
                ((Layer) layer).weights = weights;
                return this;
            }

            LayerBuilder randomInitWeights(RandomInit randomInit) {
                ((Layer) layer).randomInit = randomInit;
                return this;
            }
        }
    }

    public final static class RandomInit {
        private final int min;
        private final int max;
        private final boolean isDouble;

        public RandomInit() {
            this.min = -1;
            this.max = 1;
            this.isDouble = true;
        }

        public RandomInit(int min, int max) {
            this.min = min;
            this.max = max;
            this.isDouble = false;
        }
    }

    static class Layer extends NeuralNetwork.Layer {
        private int neurons;
        private Matrix weights;
        private RandomInit randomInit;

        Matrix getWeights() {
            return weights;
        }
    }

    FeedForwardNeuralNetwork() {
    }

    @Override
    public void train(Matrix input, Matrix output) {
        super.train(input, output);

        if (layersNumber() < 2) {
            throw new IllegalStateException("Number of layers must be at least two!");
        }

        Matrix inputWithBias = Matrix.ones(input.rows(), 1).mergeRows(input);

        init();


        switch (algorithm.algorithm) {
            case CONJUGATE_GRADIENT:
                runConjugateGradient();
                break;
            default:
                throw new RuntimeException("Unsupported optimization algorithm");
        }
        Matrix calculatedOutput = forwardPropagation(inputWithBias);
    }

    void init() {
        int ln = layersNumber();
        boolean isGlobalActivation = false;
        if (activation != null)
            isGlobalActivation = true;

        for (int i = 1; i < ln; i++) {
            Layer previousLayer = (Layer) layers.get(i - 1);
            Layer currentLayer = (Layer) layers.get(i);

            if (!isGlobalActivation && currentLayer.activation == null)
                throw new IllegalStateException("The network layer" + currentLayer.name
                        + " does not have an activation function. " +
                        "You must set the activation function either for the whole network or " +
                        "for every layer, except the first layer");

            int numberNeuronsInPreviousLayer = previousLayer.neurons;
            int numberNeuronsInCurrentLayer = currentLayer.neurons;
            if (previousLayer.weights == null) {
                if (previousLayer.randomInit == null)
                    throw new IllegalStateException("You must set the initialization way for the weights");
                if (previousLayer.randomInit.isDouble) {
                    previousLayer.weights =
                            Matrix.randomDoubleInRange(numberNeuronsInPreviousLayer + 1,
                                    numberNeuronsInCurrentLayer, previousLayer.randomInit.min,
                                    previousLayer.randomInit.max);
                } else {
                    previousLayer.weights = Matrix.randomInt(numberNeuronsInPreviousLayer + 1,
                            numberNeuronsInCurrentLayer, previousLayer.randomInit.min,
                            previousLayer.randomInit.max);
                }
            } else {
                if (previousLayer.weights.rows() != (numberNeuronsInPreviousLayer + 1) ||
                        previousLayer.weights.columns() != numberNeuronsInCurrentLayer)
                    throw new IllegalStateException("The weights matrix in the layer "
                            + previousLayer.name + " has a wrong size, must be "
                            + (numberNeuronsInPreviousLayer + 1) + "x" + numberNeuronsInCurrentLayer);
            }
        }
    }

    Matrix activationInput(Matrix weights, Matrix activationInput) {
        return activationInput.multiply(weights);
    }

    Matrix forwardPropagation(Matrix input) {
        int ln = layersNumber();
        for (int i = 0; i < ln - 1; i++) {
            input = Matrix.ones(input.rows(), 1).mergeRows(input);
            Layer layer = (Layer) layers.get(i);
            Matrix output = activationInput(layer.weights, input);
            if (layer.activation != null)
                output = layer.activation.getActivationFunction().execute(output);
            else
                output = activation.getActivationFunction().execute(output);
            input = output;
        }

        return input;
    }

    private void runConjugateGradient() {
        Map<IOptimizationAlgorithm.ParameterType, Object> params = new HashMap<>();
        params.put(IOptimizationAlgorithm.ParameterType.GRADIENT, new backPropagationGradient());
        params.put(IOptimizationAlgorithm.ParameterType.MAXIMAL_STEP_NUMBER, 0);
        params.put(IOptimizationAlgorithm.ParameterType.EPSILON_ACCURACY, 0.);
        params.put(IOptimizationAlgorithm.ParameterType.INITIAL_POINT, Matrix.parse("5; 6"));
        params.put(IOptimizationAlgorithm.ParameterType.FUNCTION, new forwardPropagationFunction());
        MinCjg cjg = new MinCjg();
        cjg.compute(params);
    }

    class forwardPropagationFunction implements IFunction {

        @Override
        public double getValue(Matrix input) {
            return 0;
        }
    }

    class backPropagationGradient implements IGradient {

        @Override
        public Matrix getValue(IFunction function, Matrix input) {
            return null;
        }
    }
}
