package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.gradients.Gradient;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.gmail.volodymyrdotsenko.ml.libs.optimalg.SetOfTestFunctions.function1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class MinCjgTest {

    @Test
    public void test() throws Exception {
        Map<IOptimizationAlgorithm.ParameterType, Object> params = new HashMap<>();
        params.put(IOptimizationAlgorithm.ParameterType.INITIAL_POINT, Matrix.parse("5; 6"));
        params.put(IOptimizationAlgorithm.ParameterType.FUNCTION, function1);
        params.put(IOptimizationAlgorithm.ParameterType.GRADIENT, new Gradient());
        params.put(IOptimizationAlgorithm.ParameterType.MAXIMAL_STEP_NUMBER, 0);
        params.put(IOptimizationAlgorithm.ParameterType.EPSILON_ACCURACY, 0.);
        MinCjg minCjg = new MinCjg();
        minCjg.compute(params);
        assertTrue(minCjg.hasConverged());
        assertTrue(minCjg.getSteps() < 100);
        System.out.println(minCjg.getSteps());
        assertEquals(0,
                Matrix.parse("1;0.5").subtract(minCjg.getMinPosition()).pow(2).sumAllElements(), 0.00000001);
        assertEquals(4, minCjg.getMinimum(), 0.00000001);
    }
}