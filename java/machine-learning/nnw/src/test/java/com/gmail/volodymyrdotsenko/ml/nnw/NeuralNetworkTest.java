package com.gmail.volodymyrdotsenko.ml.nnw;

import org.junit.Test;

/**
 * Created by Volodymyr Dotsenko on 2/12/17.
 */
public class NeuralNetworkTest {

    @Test
    public void simpleTest() {
        FeedForwardNeuralNetwork nnw  =
                new FeedForwardNeuralNetwork
                        .Builder()
                        .layer(new FeedForwardNeuralNetwork.Builder.LayerBuilder().name("input").neurons(2).build())
                        .build();
    }

}