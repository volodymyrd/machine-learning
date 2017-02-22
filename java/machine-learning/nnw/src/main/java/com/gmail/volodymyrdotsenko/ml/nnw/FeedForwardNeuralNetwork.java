package com.gmail.volodymyrdotsenko.ml.nnw;

import com.gmail.volodymyrdotsenko.ml.libs.activation.Activation;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        public FeedForwardNeuralNetwork build() {
            return super.build();
        }

        @Override
        public <L extends NeuralNetwork.Layer> Builder layer(L layer) {
            Layer l = (Layer) layer;

            return (Builder) super.layer(layer);
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
        }
    }

    static class Layer extends NeuralNetwork.Layer {
        private int neurons;
        private Matrix weights;
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

        Matrix calculatedOutput = forwardPropagation(inputWithBias);
    }

    private void init() {
        int ln = layersNumber();
        for (int i = 1; i < ln; i++) {
            Layer previousLayer = (Layer) layers.get(i - 1);
            int numberNeuronsInPreviousLayer = previousLayer.neurons;
            int numberNeuronsInCurrentLayer = ((Layer) layers.get(i)).neurons;
            if (previousLayer.weights == null) {
                previousLayer.weights = Matrix.ones(numberNeuronsInPreviousLayer + 1, numberNeuronsInCurrentLayer);
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
        for (int i = 0; i < ln; i++) {
            Layer layer = (Layer) layers.get(i);
            Matrix output = activationInput(layer.weights, input);
            output = layer.activation.getActivationFunction().execute(output);
            input = output;
        }

        return input;
    }
}
