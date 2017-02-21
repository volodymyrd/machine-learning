package com.gmail.volodymyrdotsenko.ml.nnw;

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

            LayerBuilder neurons(int neurons) {
                ((Layer) layer).neurons = neurons;
                return this;
            }

            LayerBuilder weights(Matrix weights) {
                ((Layer) layer).weights = weights;
                return this;
            }
        }
    }

    static class Layer extends NeuralNetwork.Layer {
        private int neurons;
        private Matrix weights;
        private Matrix tWeights;
    }

    private FeedForwardNeuralNetwork() {
    }

    @Override
    public void train(Matrix input, Matrix output) {
        if (layersNumber() < 2) {
            throw new IllegalStateException("Number layers must be at least two!");
        }

        init();
    }

    private void init() {
        int ln = layersNumber();
        for (int i = 1; i < ln; i++) {
            Layer l = (Layer) layers.get(i - 1);
            int in = l.neurons;
            int on = ((Layer) layers.get(i)).neurons;
            if (l.weights == null) {
                l.weights = Matrix.ones(on, in + 1);
            } else {
                if (l.weights.rows() != on || l.weights.columns() != (in + 1))
                    throw new IllegalStateException("The weights matrix in the layer "
                            + l.name + " has a wrong size, must be "
                            + on + "x" + (in + 1));
            }

            l.tWeights = l.weights.transpose();
        }
    }

    public void forwardPropagation() {

    }
}
