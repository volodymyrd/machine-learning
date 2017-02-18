package com.gmail.volodymyrdotsenko.ml.libs.matrix;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class MatrixTest {

    @Test
    public void testEquals() {
        assertEquals(Matrix.ones(5, 3, true), Matrix.ones(5, 3, false));
    }

    @Test
    public void testParse() {
        Matrix m = Matrix.parse(" 1 1  1 ;  1 1      1 ; 1 1 1");
        assertEquals(Matrix.ones(3, 3, false), m);
    }

    @Test
    public void testBigOnes() {
        assertEquals(Matrix.ones(5, 3, true), Matrix.parse("1 1 1;1 1 1;1 1 1;1 1 1;1 1 1"));
    }

    @Test
    public void testOnes() {
        assertEquals(Matrix.ones(5, 3), Matrix.parse("1 1 1;1 1 1;1 1 1;1 1 1;1 1 1"));
    }

    @Test
    public void testZero() {
        assertEquals(Matrix.zero(5, 3), Matrix.parse("0 0 0;0 0 0;0 0 0;0 0 0;0 0 0"));
    }

    @Test
    public void testExp() {
        assertEquals(Matrix.zero(5, 3).exp(), Matrix.parse("1 1 1;1 1 1;1 1 1;1 1 1;1 1 1"));
    }

    @Test
    public void testMultiply() {
        assertEquals(Matrix.ones(5, 3).multiply(5), Matrix.parse("5 5 5;5 5 5;5 5 5;5 5 5;5 5 5"));
    }
}