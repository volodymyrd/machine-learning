package com.gmail.volodymyrdotsenko.ml.nnw;

import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Volodymyr Dotsenko on 2/12/17.
 */
public class NeuralNetworkTest {

    @Test
    public void simpleTest() {
        FeedForwardNeuralNetwork nnw =
                new FeedForwardNeuralNetwork
                        .Builder()
                        .layer(new FeedForwardNeuralNetwork.Builder.LayerBuilder().name("input").neurons(2).build())
                        .layer(new FeedForwardNeuralNetwork.Builder.LayerBuilder().name("hidden").neurons(4).build())
                        .layer(new FeedForwardNeuralNetwork.Builder.LayerBuilder().name("hidden").neurons(1).build())
                        .build();

        assertEquals(3, nnw.layersNumber());

        Matrix input = Matrix.parse("0 0; 0 1; 1 0; 1 1");
        Matrix output = Matrix.parse("1; 0; 0; 1");
        nnw.train(input, output);
    }

}