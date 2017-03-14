package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.gradients.Gradient;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.gmail.volodymyrdotsenko.ml.libs.optimalg.SetOfTestFunctions.function1;
import static com.gmail.volodymyrdotsenko.ml.libs.optimalg.SetOfTestFunctions.function2;
import static com.gmail.volodymyrdotsenko.ml.libs.optimalg.SetOfTestFunctions.function3;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class MinCjgTest {

    private final Map<IOptimizationAlgorithm.ParameterType, Object> params = new HashMap<>();
    private final MinCjg minCjg = new MinCjg();

    @Before
    public void setUp() {
        params.clear();
        params.put(IOptimizationAlgorithm.ParameterType.GRADIENT, new Gradient());
        params.put(IOptimizationAlgorithm.ParameterType.MAXIMAL_STEP_NUMBER, 0);
        params.put(IOptimizationAlgorithm.ParameterType.EPSILON_ACCURACY, 0.);
    }

    @Test
    public void testFunction1() throws Exception {
        params.put(IOptimizationAlgorithm.ParameterType.INITIAL_POINT, Matrix.parse("5; 6"));
        params.put(IOptimizationAlgorithm.ParameterType.FUNCTION, function1);
        minCjg.compute(params);
        assertTrue(minCjg.hasConverged());
        assertTrue(minCjg.getSteps() < 100);
        System.out.println(minCjg.getSteps());
        assertEquals(0,
                Matrix.parse("1;0.5").subtract(minCjg.getMinPosition()).pow(2).sumAllElements(), 0.00000001);
        assertEquals(4, minCjg.getMinimum(), 0.00000001);
    }

    @Test
    public void testFunction2() throws Exception {
        params.put(IOptimizationAlgorithm.ParameterType.INITIAL_POINT, Matrix.parse("-1000; 1000.9"));
        params.put(IOptimizationAlgorithm.ParameterType.FUNCTION, function2);
        minCjg.compute(params);
        assertTrue(minCjg.hasConverged());
        assertTrue(minCjg.getSteps() < 200);
        System.out.println(minCjg.getSteps());
        assertEquals(0,
                Matrix.parse("3;9").subtract(minCjg.getMinPosition()).pow(2).sumAllElements(), 0.00000001);
        assertEquals(0, minCjg.getMinimum(), 0.00000001);
    }

    @Test
    public void testFunction3() throws Exception {
        params.put(IOptimizationAlgorithm.ParameterType.INITIAL_POINT, Matrix.parse("0.2;-0.5;-0.7"));
        params.put(IOptimizationAlgorithm.ParameterType.FUNCTION, function3);
        minCjg.compute(params);
        assertTrue(minCjg.hasConverged());
        assertTrue(minCjg.getSteps() < 200);
        assertEquals(68.7, minCjg.getMinimum(), 0.1);
    }
}