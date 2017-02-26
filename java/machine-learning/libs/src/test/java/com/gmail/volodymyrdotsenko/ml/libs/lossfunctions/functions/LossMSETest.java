package com.gmail.volodymyrdotsenko.ml.libs.lossfunctions.functions;

import com.gmail.volodymyrdotsenko.ml.libs.matrix.Matrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Volodymyr Dotsenko on 2/22/17.
 */
public class LossMSETest {
    @Test
    public void testExecuteWithEqualsRows() throws Exception {
        assertEquals(1.375, new LossMSE()
                .execute(Matrix.parse("1 0 1 1;0 1 1 0;0 0 1 0;1 0 1 1"),
                        Matrix.parse("0 1 1 0;0 1 0 1;1 0 0 1;1 1 0 0")), 0.001);
    }

    @Test
    public void testExecuteWithOneRowInOutputMatrix() throws Exception {
        assertEquals(0.875, new LossMSE()
                .execute(Matrix.parse("1 0 1 1;0 1 1 0;0 0 1 0;1 0 1 1"),
                        Matrix.parse("0 1 1 0")), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteWithDifferentNumbersOfRows() throws Exception {
        new LossMSE().execute(Matrix.parse("1 0 1 1;0 1 1 0;0 0 1 0;1 0 1 1"),
                Matrix.parse("0 1 1 0;0 1 1 0;"));
    }
}