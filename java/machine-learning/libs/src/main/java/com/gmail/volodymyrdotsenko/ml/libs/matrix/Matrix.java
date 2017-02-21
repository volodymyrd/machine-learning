package com.gmail.volodymyrdotsenko.ml.libs.matrix;

import org.ojalgo.function.NullaryFunction;
import org.ojalgo.function.PrimitiveFunction;
import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.BigMatrix;
import org.ojalgo.matrix.MatrixUtils;
import org.ojalgo.matrix.PrimitiveMatrix;

/**
 *
 */
public final class Matrix {

    private final BasicMatrix matrix;

    private static BasicMatrix.Factory<?> getFactory(boolean big) {
        return big ? BigMatrix.FACTORY : PrimitiveMatrix.FACTORY;
    }

    private Matrix(BasicMatrix matrix) {
        this.matrix = matrix;
    }

    public static Matrix parse(String matrixString) {
        String[] rows = matrixString.split(";");
        double[][] values = new double[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] columns = rows[i].trim().split("\\s+");
            values[i] = new double[columns.length];
            for (int j = 0; j < columns.length; j++) {
                values[i][j] = Double.valueOf(columns[j].trim());
            }
        }

        return new Matrix(getFactory(false).rows(values));
    }

    public long rows() {
        return matrix.countRows();
    }

    public long columns() {
        return matrix.countColumns();
    }

    public static Matrix zero(long rows, long columns) {
        return zero(rows, columns, false);
    }

    public static Matrix zero(long rows, long columns, boolean big) {
        return new Matrix(getFactory(big).makeZero(rows, columns));
    }

    public static Matrix ones(long rows, long columns) {
        return ones(rows, columns, false);
    }

    public static Matrix ones(long rows, long columns, boolean big) {
        return new Matrix(getFactory(big).makeFilled(rows, columns, new NullaryFunction() {
            @Override
            public double doubleValue() {
                return 1;
            }

            @Override
            public Number invoke() {
                return 1;
            }
        }));
    }

    public static Matrix eye(long rows, long columns) {
        return eye(rows, columns, false);
    }

    public static Matrix eye(long rows, long columns, boolean big) {
        return new Matrix(getFactory(big).makeEye(rows, columns));
    }

    public Matrix add(double scalarAddend) {
        return new Matrix(matrix.add(scalarAddend));
    }

    public Matrix add(Matrix addend) {
        return new Matrix(matrix.add(addend.matrix));
    }

    public Matrix subtract(double scalarSubtrahend) {
        return new Matrix(matrix.subtract(scalarSubtrahend));
    }

    public Matrix subtract(Matrix subtrahend) {
        return new Matrix(matrix.subtract(subtrahend.matrix));
    }

    public Matrix divide(double scalarDivisor) {
        return new Matrix(matrix.divide(scalarDivisor));
    }

    public Matrix divide(Matrix divisor) {
        return new Matrix(matrix.divideElements(divisor.matrix));
    }

    public Matrix multiply(Matrix multiplicand) {
        return new Matrix(matrix.multiply(multiplicand.matrix));
    }

    public Matrix multiply(double scalarMultiplicand) {
        return new Matrix(matrix.multiply(scalarMultiplicand));
    }

    public Matrix multiplyElements(Matrix multiplicand) {
        return new Matrix(matrix.multiplyElements(multiplicand.matrix));
    }

    public Matrix transpose() {
        return new Matrix(matrix.transpose());
    }

    public Matrix exp() {
        return new Matrix(matrix.modify(PrimitiveFunction.EXP));
    }

    public Matrix invertElements() {
        return new Matrix(matrix.modify(PrimitiveFunction.INVERT));
    }

    @Override
    public String toString() {
        return MatrixUtils.toString(matrix);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix matrix1 = (Matrix) o;

        return matrix.equals(matrix1.matrix);
    }

    @Override
    public int hashCode() {
        return matrix.hashCode();
    }
}