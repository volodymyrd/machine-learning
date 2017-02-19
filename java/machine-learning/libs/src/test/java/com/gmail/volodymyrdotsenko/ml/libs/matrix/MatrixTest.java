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
        assertEquals(Matrix.parse("1 1 1;1 1 1;1 1 1;1 1 1;1 1 1"), Matrix.ones(5, 3, true));
    }

    @Test
    public void testOnes() {
        assertEquals(Matrix.parse("1 1 1;1 1 1;1 1 1;1 1 1;1 1 1"), Matrix.ones(5, 3));
    }

    @Test
    public void testZero() {
        assertEquals(Matrix.parse("0 0 0;0 0 0;0 0 0;0 0 0;0 0 0"), Matrix.zero(5, 3));
    }

    @Test
    public void testScalarAdd() {
        assertEquals(Matrix.parse("2 2 2;2 2 2;2 2 2;2 2 2;2 2 2"), Matrix.ones(5, 3).add(1));
    }

    @Test
    public void testMatrixAdd() {
        assertEquals(Matrix.parse("8 6 16; 7 16 11; 4 8 8"),
                Matrix.parse("1 2 4; 5 7 4; 3 4 5").add(Matrix.parse("7 4 12; 2 9 7; 1 4 3")));
    }

    @Test
    public void testScalarSubtract() {
        assertEquals(Matrix.zero(5, 3), Matrix.ones(5, 3).subtract(1));
    }

    @Test
    public void testSubtractAdd() {
        assertEquals(Matrix.parse("-6 -2 -8; 3 -2 -3; 2 0 2"),
                Matrix.parse("1 2 4; 5 7 4; 3 4 5").subtract(Matrix.parse("7 4 12; 2 9 7; 1 4 3")));
    }

    @Test
    public void testDivide() {
        assertEquals(Matrix.parse("2 2 2;2 2 2;2 2 2;2 2 2;2 2 2"),
                Matrix.parse("4 4 4;4 4 4;4 4 4;4 4 4;4 4 4").divide(2));
    }

    @Test
    public void testMatrixDivide() {
        assertEquals(Matrix.parse("8 3 4; 2 6 1.5; 3 2 5"),
                Matrix.parse("8 6 16; 10 18 3; 9 8 25").divide(Matrix.parse("1 2 4; 5 3 2; 3 4 5")));
    }

    @Test
    public void testScalarMultiply() {
        assertEquals(Matrix.parse("5 5 5;5 5 5;5 5 5;5 5 5;5 5 5"), Matrix.ones(5, 3).multiply(5));
    }

    @Test
    public void testMatrixMultiply() {
        assertEquals(Matrix.parse("15 38; 58 119"),
                Matrix.parse("1 2 4; 5 7 9").multiply(Matrix.parse("7 4; 2 9; 1 4")));
    }

    @Test
    public void testMatrixMultiplyElements() {
        assertEquals(Matrix.parse("7 8;10 63;3 20"),
                Matrix.parse("1 2; 5 7; 3 5").multiplyElements(Matrix.parse("7 4; 2 9; 1 4")));
    }

    @Test
    public void testInvertElements() {
        assertEquals(Matrix.parse("0.5 0.5 0.5; 0.5 0.5 0.5"), Matrix.ones(2, 3).multiply(2).invertElements());
    }

    @Test
    public void testExp() {
        assertEquals(Matrix.parse("1 1 1;1 1 1;1 1 1;1 1 1;1 1 1"), Matrix.zero(5, 3).exp());
    }
}