package com.gmail.volodymyrdotsenko.ml.libs.matrix;

import org.ojalgo.access.ElementView2D;
import org.ojalgo.function.NullaryFunction;
import org.ojalgo.function.PrimitiveFunction;
import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.BigMatrix;
import org.ojalgo.matrix.MatrixUtils;
import org.ojalgo.matrix.PrimitiveMatrix;

import java.util.Random;

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

    public static Matrix randomInt(long rows, long columns, int min, int max) {
        Random random = new Random();

        double[][] values = new double[(int) rows][(int) columns];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                values[i][j] = random.nextInt((max - min) + 1) + min;
            }

        return new Matrix(getFactory(false).rows(values));
    }

    public static Matrix randomDouble(long rows, long columns) {
        Random random = new Random();

        double[][] values = new double[(int) rows][(int) columns];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                double v = 0;
                while ((v = random.nextDouble()) <= 0) ;
                values[i][j] = v;
            }


        return new Matrix(getFactory(false).rows(values));
    }

    public static Matrix randomDoubleInRange(long rows, long columns, int min, int max) {
        Random random = new Random();

        double[][] values = new double[(int) rows][(int) columns];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                double v = 0;
                while ((v = min + (max - min) * random.nextDouble()) == 0) ;
                values[i][j] = v;
            }


        return new Matrix(getFactory(false).rows(values));
    }

    public Matrix copy() {
        return new Matrix(matrix.copy().build());
    }

    public double get(long row, long col) {
        return matrix.get(row, col).doubleValue();
    }

    public static Matrix vector(double[] values) {
        return new Matrix(getFactory(false).columns(values));
    }

    public static Matrix from2Array(double[][] values) {
        return new Matrix(getFactory(false).rows(values));
    }

    public double[][] to2Array() {
        return matrix.toRawCopy2D();
    }

    public double[] toArrayByColumn() {
        return matrix.toRawCopy1D();
    }

    public double[] toArrayByRow() {
        final double[] retVal = new double[(int) matrix.count()];

        int k = 0;
        for (int i = 0; i < matrix.countRows(); i++) {
            for (int j = 0; j < matrix.countColumns(); j++)
                retVal[k++] = matrix.doubleValue(i, j);
        }

        return retVal;
    }

    public Matrix getRowsRange(int first, int limit) {
        return new Matrix(matrix.getRowsRange(first, limit));
    }

    public Matrix getColumnsRange(int first, int limit) {
        return new Matrix(matrix.getColumnsRange(first, limit));
    }

    public double[] extractColumn(long columnNumber) {
        double[] column = new double[(int) rows()];
        for (int i = 0; i < column.length; i++) {
            column[i] = get(i, columnNumber);
        }

        return column;
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

    public Matrix mergeRows(Matrix rightColumns) {
        return new Matrix(matrix.mergeRows(rightColumns.matrix));
    }

    public Matrix exp() {
        return new Matrix(matrix.modify(PrimitiveFunction.EXP));
    }

    public Matrix ln() {
        return new Matrix(matrix.modify(PrimitiveFunction.LOG));
    }

    public Matrix invertElements() {
        return new Matrix(matrix.modify(PrimitiveFunction.INVERT));
    }

    public Matrix pow(double p) {
        return new Matrix(matrix.modify(PrimitiveFunction.POW.second(p)));
    }

    public double sumAllElements() {
        double sum = 0;
        ElementView2D<Number, ?> elements = matrix.elements();
        while (elements.hasNext()) {
            sum += elements.next().doubleValue();
        }
        return sum;
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