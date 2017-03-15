package com.gmail.volodymyrdotsenko.ml.nnw;

import com.gmail.volodymyrdotsenko.ml.libs.activation.Activation;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import com.gmail.volodymyrdotsenko.ml.libs.optimalg.OptimizationAlgorithm;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 */
public class FeedForwardNeuralNetworkTest {

    private FeedForwardNeuralNetwork nnw;

    @Before
    public void setUp() {
        nnw = new FeedForwardNeuralNetwork
                .Builder()
                .layer(new FeedForwardNeuralNetwork.Builder.LayerBuilder()
                        .name("input")
                        .neurons(2)
                        .weights(Matrix.parse("5 -10 1;-10 5 1;-5 10 -1"))
                        .build())
                .layer(new FeedForwardNeuralNetwork.Builder.LayerBuilder()
                        .name("hidden")
                        .neurons(3)
                        .weights(Matrix.parse("5;10;-50;20"))
                        .build())
                .layer(new FeedForwardNeuralNetwork.Builder.LayerBuilder()
                        .name("output")
                        .neurons(1)
                        .build())
                .algorithm(new FeedForwardNeuralNetwork.Builder.AlgorithmBuilder()
                        .algorithm(OptimizationAlgorithm.CONJUGATE_GRADIENT)
                        .accuracy(1e-3)
                        .maxStepNumber(30)
                        .build())
                .activation(Activation.SIGMOID)
                .build();
    }

    @Test
    public void simpleTest() {
        assertEquals(3, nnw.layersNumber());

        //Matrix input = Matrix.parse("0 0; 0 1; 1 0; 1 1");
        //Matrix output = Matrix.parse("1; 0; 0; 1");
        //nnw.train(input, output);
    }

    @Test
    public void testActivationInput() throws Exception {
        Matrix activation = Matrix.parse("1 0 0;1 1 1;1 0 1;1 1 0");
        Matrix weights = Matrix.parse("-10 5 10; -5 10 1; -1 10 5");
        assertEquals(Matrix.parse("-10 5 10;-16 25 16; -11 15 15; -15 15 11"),
                new FeedForwardNeuralNetwork().activationInput(weights, activation));
    }

    @Test
    public void testInit() throws Exception {
        nnw.init();
        for (NeuralNetwork.Layer l : nnw.layers) {
            switch (l.name) {
                case "input":
                    Matrix weights = ((FeedForwardNeuralNetwork.Layer) l).getWeights();
                    assertNotNull(weights);
                    assertEquals(3, weights.columns());
                    assertEquals(3, weights.rows());
                    break;
                case "hidden":
                    weights = ((FeedForwardNeuralNetwork.Layer) l).getWeights();
                    assertNotNull(weights);
                    assertEquals(1, weights.columns());
                    assertEquals(4, weights.rows());
                    break;
            }
        }
    }

    @Test
    public void testForwardPropagation() throws Exception {
        nnw.init();
        Matrix input = Matrix.parse("1 0");
        assertEquals(1.0, nnw.forwardPropagation(input).get(0, 0), 0.001);
    }
}