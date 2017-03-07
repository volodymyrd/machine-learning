package com.gmail.volodymyrdotsenko.ml.libs.functions.gradients;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class GradientTest {
    //y=exp(x)+x^2+5*x+lnx+1/x
    //y'=exp(x)+2*x+5+1/x-1/x^2
    IFunction function = new IFunction() {
        @Override
        public double getValue(Matrix input) {
            return input.exp()
                    .add(input.pow(2))
                    .add(input.multiply(5))
                    .add(input.ln())
                    .add(input.invertElements())
                    .get(0, 0);
        }
    };

    @Test
    public void testFunction() throws Exception {
        assertEquals(45.517,
                function.getValue(Matrix.parse("3")), 0.001);
    }

    @Test
    public void testGetValue() throws Exception {
        assertEquals(31.308,
                new Gradient().getValue(function, Matrix.parse("3")).get(0, 0), 0.001);
    }
}