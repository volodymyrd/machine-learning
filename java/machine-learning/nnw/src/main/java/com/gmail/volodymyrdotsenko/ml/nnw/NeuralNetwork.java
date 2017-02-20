package com.gmail.volodymyrdotsenko.ml.nnw;

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

            L build() {
                return layer;
            }
        }
    }

    protected NeuralNetwork() {
    }

    protected static class Layer {
        protected String name;
    }

    public abstract void train(Matrix input, Matrix output);
}
