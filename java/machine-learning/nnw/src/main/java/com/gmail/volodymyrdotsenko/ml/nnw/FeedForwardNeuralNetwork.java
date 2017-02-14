package com.gmail.volodymyrdotsenko.ml.nnw;

/**
 * Created by Volodymyr Dotsenko on 2/13/17.
 */
public final class FeedForwardNeuralNetwork extends NeuralNetwork {

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
                return this;
            }
        }
    }

    static class Layer extends NeuralNetwork.Layer {
        private int neurons;
    }

    private FeedForwardNeuralNetwork() {
    }
}
