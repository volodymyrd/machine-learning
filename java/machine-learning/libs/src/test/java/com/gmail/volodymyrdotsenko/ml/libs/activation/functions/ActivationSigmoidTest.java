package com.gmail.volodymyrdotsenko.ml.libs.activation.functions;

import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ActivationSigmoidTest {

    @Test
    public void testSigmoid() {
        assertEquals(Matrix.parse("0.5 0.5; 0.5 0.5; 0.5 0.5"),
                new ActivationSigmoid().execute(Matrix.zero(3, 2)));
    }
}