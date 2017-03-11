package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.functions.IGradient;
import com.gmail.volodymyrdotsenko.ml.libs.functions.gradients.Gradient;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class MinCjgTest {
    //z=x^3+8*y^3-6*x*y+5
    public IFunction function = new IFunction() {
        @Override
        public double getValue(Matrix input) {
            //Matrix input3 = input.pow(3);
            double v = Math.pow(input.get(0, 0), 3) + 8 * Math.pow(input.get(1, 0), 3)
                    - 6 * input.get(0, 0) * input.get(1, 0) + 5;
            return v;
        }
    };

    private IGradient gradient = new Gradient();

    @Test
    public void testFunction() {
        assertEquals(4, function.getValue(Matrix.parse("1;0.5")), 0.0);
    }

    @Test
    public void test() throws Exception {
        Map<IOptimizationAlgorithm.ParameterType, Object> params = new HashMap<>();
        params.put(IOptimizationAlgorithm.ParameterType.INITIAL_POINT, Matrix.parse("5; 6"));
        params.put(IOptimizationAlgorithm.ParameterType.FUNCTION, function);
        params.put(IOptimizationAlgorithm.ParameterType.GRADIENT, gradient);
        params.put(IOptimizationAlgorithm.ParameterType.MAXIMAL_STEP_NUMBER, 0);
        params.put(IOptimizationAlgorithm.ParameterType.EPSILON_ACCURACY, 0.);
        MinCjg minCjg = new MinCjg();
        minCjg.compute(params);
        assertTrue(minCjg.hasConverged());
        assertTrue(minCjg.getSteps() < 100);
        assertEquals(0,
                Matrix.parse("1;0.5").subtract(minCjg.getMinPosition()).pow(2).sumAllElements(), 0.00000001);
        assertEquals(4, minCjg.getMinimum(), 0.00000001);
    }
}