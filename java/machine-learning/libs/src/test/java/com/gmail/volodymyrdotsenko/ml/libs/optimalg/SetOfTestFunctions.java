package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Volodymyr Dotsenko on 3/11/17.
 */
public class SetOfTestFunctions {
    //z=x^3+8*y^3-6*x*y+5
    public static IFunction function1 = new IFunction() {
        @Override
        public double getValue(Matrix input) {
            //Matrix input3 = input.pow(3);
            double v = Math.pow(input.get(0, 0), 3) + 8 * Math.pow(input.get(1, 0), 3)
                    - 6 * input.get(0, 0) * input.get(1, 0) + 5;
            return v;
        }
    };

    @Test
    public void testFunction() {
        assertEquals(4, function1.getValue(Matrix.parse("1;0.5")), 0.0);
    }
}
