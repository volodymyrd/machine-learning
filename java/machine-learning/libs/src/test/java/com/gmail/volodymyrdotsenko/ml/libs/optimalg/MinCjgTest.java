package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.IFunction;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MinCjgTest {

    private MinCjg minCjg = new MinCjg();
    private IFunction function = input -> input.pow(3).multiply(2)
            .add(input.pow(2).multiply(3).subtract(input.multiply(5))
                    .add(10))
            .get(0, 0);

    private Map<IOptimizationAlgorithm.ParameterType, Object> parameters =
            new HashMap<IOptimizationAlgorithm.ParameterType, Object>() {{

                put(IOptimizationAlgorithm.ParameterType.INITIAL_POINT, Matrix.ones(1, 1));
                put(IOptimizationAlgorithm.ParameterType.MAXIMAL_STEP_NUMBER, 100);
                put(IOptimizationAlgorithm.ParameterType.EPSILON_ACCURACY, 1e-5);
            }};

    @Before
    public void setUp() {

    }

    @Test
    public void testFunction() {
        Assert.assertEquals(28, function.getValue(Matrix.parse("2")), 0);
    }

    @Test
    public void testCompute() throws Exception {
        minCjg.compute(function, parameters);
    }

}