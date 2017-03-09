package com.gmail.volodymyrdotsenko.ml.libs.optimalg.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class MinParabTest {

    @Test
    public void test() throws Exception {
        assertEquals(-1,
                MinParab.getPositionOfExtremum(7, f(7), 5, f(5), 2, f(2)), 0.0);
        assertEquals(-1,
                MinParab.getPositionOfExtremum(25.5, f(25.5),
                        -20.23, f(-20.23), -123.78, f(-123.78)), 0.00001);
    }

    //y=x^2+2*x-1 with min in x=-1
    private double f(double x) {
        return x * x + 2 * x - 1;
    }
}