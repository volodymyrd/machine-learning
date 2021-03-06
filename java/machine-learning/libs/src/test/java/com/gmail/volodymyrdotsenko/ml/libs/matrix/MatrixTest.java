package com.gmail.volodymyrdotsenko.ml.libs.matrix;

import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testCopy() {
        Matrix m = Matrix.parse("1 2 3;4 5 6;7 8 9");
        Matrix copy = m.copy();
        assertEquals(m, copy);
    }

    @Test
    public void testRows() {
        assertEquals(4, Matrix.parse("8 3 4; 2 6 1.5; 3 2 5; 1 5 7").rows());
    }

    @Test
    public void testColumns() {
        assertEquals(3, Matrix.parse("8 3 4; 2 6 1.5; 3 2 5; 1 5 7").columns());
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
    public void testTranspose() {
        assertEquals(Matrix.parse("8 2 3 1; 3 6 2 5; 4 1.5 5 7"),
                Matrix.parse("8 3 4; 2 6 1.5; 3 2 5; 1 5 7").transpose());
    }

    @Test
    public void mergeRows() {
        assertEquals(Matrix.parse("1 8 3 4;1 2 6 1.5;1 3 2 5;1 1 5 7"),
                Matrix.ones(4, 1).mergeRows(Matrix.parse("8 3 4; 2 6 1.5; 3 2 5; 1 5 7")));
    }

    @Test
    public void testInvertElements() {
        assertEquals(Matrix.parse("0.5 0.5 0.5; 0.5 0.5 0.5"), Matrix.ones(2, 3).multiply(2).invertElements());
    }

    @Test
    public void testExp() {
        assertEquals(Matrix.parse("1 1 1;1 1 1;1 1 1;1 1 1;1 1 1"), Matrix.zero(5, 3).exp());
    }

    @Test
    public void testPow() {
        assertEquals(Matrix.parse("8 8 8;8 8 8;8 8 8;8 8 8;8 8 8"), Matrix.ones(5, 3).multiply(2).pow(3));
    }

    @Test
    public void testGetRowsRange() {
        assertEquals(Matrix.parse("1 2 6 1.5;1 3 2 5;1 1 5 7"),
                Matrix.parse("1 8 3 4;1 2 6 1.5;1 3 2 5;1 1 5 7").getRowsRange(1, 4));
    }

    @Test
    public void testRandomDouble() {
        int rows = 200;
        int columns = 500;
        Matrix m = Matrix.randomDouble(rows, columns);

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < columns; c++)
                assertTrue(m.get(r, c) > 0 && m.get(r, c) < 1);
    }

    @Test
    public void testRandomInt() {
        int rows = 200;
        int columns = 500;
        int min = -50;
        int max = 50;
        Matrix m = Matrix.randomInt(rows, columns, min, max);

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < columns; c++) {
                assertTrue(m.get(r, c) >= min && m.get(r, c) <= max);
            }
    }

    @Test
    public void testRandomDoubleInRange() {
        int rows = 200;
        int columns = 500;
        int min = -1;
        int max = 1;
        Matrix m = Matrix.randomDoubleInRange(rows, columns, min, max);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < columns; c++) {
                assertTrue(m.get(r, c) >= min && m.get(r, c) <= max);
                assertNotEquals(0, m.get(r, c));
            }
    }

    @Test
    public void testSumAllElements() {
        assertEquals(45, Matrix.parse("1 2 3; 4 5 6; 7 8 9").sumAllElements(), 0.0);
    }

    @Test
    public void testVector() {
        assertEquals(Matrix.parse("1;2;3;4;5"), Matrix.vector(new double[]{1, 2, 3, 4, 5}));
    }

    @Test
    public void testFrom2Array() {
        assertEquals(Matrix.parse("1 2 3 4;5 6 7 8"), Matrix.from2Array(new double[][]{{1, 2, 3, 4}, {5, 6, 7, 8}}));
    }

    @Test
    public void testTo2Array() {
        assertArrayEquals(new double[][]{{1, 2, 3, 4}, {5, 6, 7, 8}}, Matrix.parse("1 2 3 4;5 6 7 8").to2Array());
    }

    @Test
    public void testToArrayByRow() {
        assertArrayEquals(new double[]{1, 2, 3, 4, 5, 6, 7, 8},
                Matrix.parse("1 2 3 4;5 6 7 8").toArrayByRow(), 0);
    }

    @Test
    public void testToArrayByColumn() {
        assertArrayEquals(new double[]{1, 5, 2, 6, 3, 7, 4, 8},
                Matrix.parse("1 2 3 4;5 6 7 8").toArrayByColumn(), 0);
    }

    @Test
    public void testExtractColumn() {
        assertArrayEquals(new double[]{1, 2, 3, 4, 5},
                Matrix.parse("1 2 1 1.5 4;1 3 2 5 7;1 2 3 4 5;2 1 4 5 7;6 8 5 2 9")
                        .extractColumn(2), 0);
    }
}