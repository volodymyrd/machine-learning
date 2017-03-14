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

    //z=100*(y - x^2)^2 + (3-x)^2
    public static IFunction function2 = new IFunction() {
        @Override
        public double getValue(Matrix input) {
            double x = input.get(0, 0);
            double y = input.get(1, 0);
            return 100 * Math.pow((y - Math.pow(x, 2)), 2) + Math.pow((3 - x), 2);
        }
    };

    //f1=x^3+2*y+log(x)+y^4+z^2+x*y*z-25
    // f=(1 + f1)^2 + (3 + f1)^2 + (12 + f1)^2
    public static IFunction function3 = new IFunction() {
        @Override
        public double getValue(Matrix input) {
            double x = input.get(0, 0);
            double y = input.get(1, 0);
            double z = input.get(2, 0);
            double f1 = Math.pow(x, 3) + 2 * y + Math.log(x) + Math.pow(y, 4) + Math.pow(z, 2) + x * y * z - 25;
            return Math.pow((1 + f1), 2) + Math.pow((3 + f1), 2) + Math.pow((12 + f1), 2);
        }
    };

    @Test
    public void testFunction1() {
        assertEquals(4, function1.getValue(Matrix.parse("1;0.5")), 0.0);
    }

    @Test
    public void testFunction2() {
        assertEquals(2042.42, function2.getValue(Matrix.parse("0.1;-4.5")), 0.0);
    }

    @Test
    public void testFunction3() {
        assertEquals(85.005, function3.getValue(Matrix.parse("1.2051;-1.6926;5.0549")), 0.001);
    }
}
