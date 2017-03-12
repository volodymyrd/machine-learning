package com.gmail.volodymyrdotsenko.ml.libs.optimalg;

import com.gmail.volodymyrdotsenko.ml.libs.functions.gradients.Gradient;
import com.gmail.volodymyrdotsenko.ml.libs.functions.hessian.Hesse;
import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.gmail.volodymyrdotsenko.ml.libs.optimalg.SetOfTestFunctions.function1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Volodymyr Dotsenko on 3/11/17.
 */
public class MinMarTest {
    @Test
    public void test() throws Exception {
        Map<IOptimizationAlgorithm.ParameterType, Object> params = new HashMap<>();
        params.put(IOptimizationAlgorithm.ParameterType.INITIAL_POINT, Matrix.parse("1000; 2000"));
        params.put(IOptimizationAlgorithm.ParameterType.FUNCTION, function1);
        params.put(IOptimizationAlgorithm.ParameterType.GRADIENT, new Gradient());
        params.put(IOptimizationAlgorithm.ParameterType.HESSIAN, new Hesse());
        params.put(IOptimizationAlgorithm.ParameterType.MAXIMAL_STEP_NUMBER, 0);
        params.put(IOptimizationAlgorithm.ParameterType.EPSILON_ACCURACY, 0.0000000001);
        MinMar minMar = new MinMar();
        minMar.compute(params);
        assertTrue(minMar.hasConverged());
        assertTrue(minMar.getSteps() < 100);
        System.out.println(minMar.getSteps() + "  " + minMar.getMinPosition() + ": " + minMar.getMinimum());
        assertEquals(0,
                Matrix.parse("1;0.5").subtract(minMar.getMinPosition()).pow(2).sumAllElements(), 0.00000001);
        assertEquals(4, minMar.getMinimum(), 0.00000001);
    }
}