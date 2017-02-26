package com.gmail.volodymyrdotsenko.ml.nnw;

import com.gmail.volodymyrdotsenko.ml.libs.activation.Activation;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class NeuralNetwork {

    private static Logger logger = LoggerFactory.getLogger(NeuralNetwork.class);

    protected List<Layer> layers = new ArrayList<>();
    protected Activation activation;

    public int layersNumber() {
        return layers.size();
    }

    public abstract static class Builder<N extends NeuralNetwork> {
        protected final N nnw;

        protected Builder(N nnw) {
            this.nnw = nnw;
        }

        public N build() {
            return nnw;
        }

        public Builder<? extends NeuralNetwork> activation(Activation activation) {
            nnw.activation = activation;

            return this;
        }

        public <L extends Layer> Builder<? extends NeuralNetwork> layer(L layer) {
            nnw.layers.add(layer);

            return this;
        }

        public abstract static class LayerBuilder<L extends Layer> {
            protected final L layer;

            protected LayerBuilder(L layer) {
                this.layer = layer;
            }

            LayerBuilder<? extends Layer> name(String name) {
                layer.name = name;

                return this;
            }

            LayerBuilder<? extends Layer> activation(Activation activation) {
                layer.activation = activation;

                return this;
            }

            L build() {
                return layer;
            }
        }
    }

    NeuralNetwork() {
    }

    protected static class Layer {
        protected String name;
        protected Activation activation;
    }

    /**
     * Start training neural network
     *
     * @param input  the MxNI matrix of the input features, where M - the number of training examples,
     *               NI - the number of features or the number of neurons in the input layer
     * @param output the MxNO matrix of the output, where M - the number of training examples,
     *               NO - the number of neurons in the output layer
     */
    public void train(Matrix input, Matrix output) {
        logger.info("Start training the neural network...");

        if (input == null)
            throw new IllegalArgumentException("The parameter 'input' must be set");
        if (output == null)
            throw new IllegalArgumentException("The parameter 'output' must be set");
        if (input.rows() != output.rows())
            throw new IllegalArgumentException("The number of rows in the input matrix must be the same " +
                    " as the number rows in output matrix");
    }
}
