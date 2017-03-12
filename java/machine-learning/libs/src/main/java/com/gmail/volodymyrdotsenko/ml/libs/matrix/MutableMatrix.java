package com.gmail.volodymyrdotsenko.ml.libs.matrix;

/**
 *
 */
public class MutableMatrix {
    /**
     * The matrix elements.
     */
    double a[][];
    /**
     * Number of rows.
     */
    int m;
    /**
     * Number of columns.
     */
    int n;

    public MutableMatrix(Matrix matrix) {
        this(matrix.to2Array());
    }

    /**
     * Creates a matrix which is a copy of b.
     */
    public MutableMatrix(MutableMatrix b) {
        m = b.getNumberOfRows();
        n = b.getNumberOfColumns();
        a = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = b.getElement(i, j);
            }
        }
    }

    /**
     * Creates an (n x n) unit matrix.
     */
    public MutableMatrix(int n) {
        this.n = n;
        this.m = n;
        this.a = new double[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    a[i][j] = 1.;
                } else {
                    a[i][j] = 0.;
                }
            }
        }
    }

    /**
     * Creates an (m x n) matrix all elements of which contain a zero (null matrix).
     */
    public MutableMatrix(int m, int n) {
        this.n = n;
        this.m = m;
        this.a = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = 0.;
            }
        }
    }


    /**
     * Creates a matrix the elements of which are given by a.
     */
    public MutableMatrix(double a[][]) {
        m = a.length;
        n = a[0].length;
        this.a = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                this.a[i][j] = a[i][j];
            }
        }
    }

    /**
     * Creates a matrix with only one column the elements of which are given by a.
     */
    public MutableMatrix(double a[]) {
        m = a.length;
        n = 1;
        this.a = new double[m][n];
        for (int i = 0; i < m; i++) {
            this.a[i][0] = a[i];
        }
    }

    /**
     * Returns number of rows.
     */
    public int getNumberOfRows() {
        return m;
    }

    /**
     * Returns number of columns.
     */
    public int getNumberOfColumns() {
        return n;
    }

    /**
     * Returns element (i,j).
     */
    public double getElement(int i, int j) {
        return a[i][j];
    }

    /**
     * Returns vector element (i).
     */
    public double getElement(int i) {
        return getElement(i, 0);
    }

    /**
     * sets element (i,j) ti in.
     */
    public void setElement(int i, int j, double in) {
        a[i][j] = in;
    }

    /**
     * sets vector element (i)
     */
    public void setElement(int i, double in) {
        setElement(i, 0, in);
    }

    /**
     * Returns the column vector with column index k.
     *
     * @param k index of column.
     * @return the desired column vector.
     */
    public MutableMatrix getColumn(int k) {
        MutableMatrix c = new MutableMatrix(m, 1);
        for (int i = 0; i < m; i++) {
            c.setElement(i, getElement(i, k));
        }
        return c;

    }

    /**
     * Replaces the column vector with column index k by (column) vector b.
     *
     * @param k number of column.
     * @param b vector replacing that column.
     */
    public void putColumn(int k, MutableMatrix b) {
        for (int i = 0; i < m; i++) {
            setElement(i, k, b.getElement(i));
        }
    }

    /**
     * Returns the row vector with row index k.
     *
     * @param k number of row.
     * @return the desired row vector.
     */
    public MutableMatrix getRow(int k) {
        MutableMatrix c = new MutableMatrix(n, 1);
        for (int i = 0; i < n; i++) {
            c.setElement(i, getElement(k, i));
        }
        return c;

    }

    /**
     * Replaces the row vector with column index k by (row) vector b.
     *
     * @param k number of row.
     * @param b vector replacing that row.
     */
    public void putRow(int k, MutableMatrix b) {
        for (int i = 0; i < n; i++) {
            setElement(k, i, b.getElement(i));
        }
    }

    public Matrix toMatrix() {
        return Matrix.from2Array(a);
    }

    /**
     * Yields the solution vectors (x1 and x2) appearing in Marquardt's method to solve the matrix equation a x = b,
     * where a is this matrix.
     *
     * @param bvec   vector b
     * @param frac   parameter f needed in singular value decomposition. If frac = 0, then it is assumed to be 1.E-15.
     * @param lambda Marquardt's parameter
     * @param x1     solution vector x1  (available AFTER completion of the method)
     * @param x2     solution vector x2  (available AFTER completion of the method)
     * @param ok     ok[0] = true if method has converged, = false otherwise (available AFTER completion of the method)
     */
    public void marquardt(Matrix bvec, double lambda, MutableMatrix x1, MutableMatrix x2, double frac, boolean[] ok) {
        MutableMatrix b = new MutableMatrix(bvec);
        // step 1: bidiagonalization of a
        MutableMatrix d = new MutableMatrix(m, 1);
        MutableMatrix e = new MutableMatrix(m, 1);
        sv1(b, d, e);
        // step 2: diagonalization of bidiagonal matrix
        ok[0] = sv2(b, d, e);
        // step 3: order singular values and perform permutation
        sv3(b, d);
        // step 4: singular value analysis
        svm(b, d, lambda, x1, x2, frac);
    }

    /**
     * Performs first step of singular value decomposition.
     */
    private void sv1(MutableMatrix b, MutableMatrix d, MutableMatrix e) {
        int l;
        MutableMatrix v, s;
        l = b.getNumberOfColumns();
        double ub[] = new double[2];
        double ubs[][] = new double[2][n];
        for (int i = 0; i < n; i++) {
            // set up Householder transformation Q(i)
            if (i < n - 1 || m > n) {
                v = getColumn(i);
                defineHouseholderTransformation(v, i, i + 1, ub);
                // apply Q(i) to a
                for (int j = i; j < n; j++) {
                    s = getColumn(j);
                    applyHouseholderTransformation(v, s, i, i + 1, ub);
                    putColumn(j, s);
                }
                // apply Q(i) to b
                for (int k = 0; k < l; k++) {
                    s = b.getColumn(k);
                    int nev = v.getNumberOfRows();
                    int nes = s.getNumberOfRows();
                    applyHouseholderTransformation(v, s, i, i + 1, ub);
                    b.putColumn(k, s);
                }
            }
            if (i < n - 2) {
                // set up Householder transformation H(i)
                v = getRow(i);
                defineHouseholderTransformation(v, i + 1, i + 2, ub);
                // save H(i)
                ubs[0][i] = ub[0];
                ubs[1][i] = ub[1];
                // apply H(i) to a
                for (int j = i; j < m; j++) {
                    s = getRow(j);
                    applyHouseholderTransformation(v, s, i + 1, i + 2, ub);
                    // save elements i+2, ... in row j of matrix a
                    if (j == i) {
                        for (int k = i + 2; k < n; k++) {
                            s.setElement(k, v.getElement(k));
                        }
                    }
                    putRow(j, s);
                }
            }
        }
        // copy diagonal of transformed matrix a to d and upper parallel to e
        if (n > 1) {
            for (int i = 1; i < n; i++) {
                d.setElement(i, getElement(i, i));
                e.setElement(i, getElement(i - 1, i));
            }
        }
        d.setElement(0, getElement(0, 0));
        e.setElement(0, 0.);
        // construct matrix H = H(1) * H(2) * ... * H(n-1), H(n-1) = I
        for (int i = n - 1; i >= 0; i--) {
            v = getRow(i);
            for (int k = 0; k < n; k++) {
                setElement(i, k, 0.);
            }
            setElement(i, i, 1.);
            if (i < n - 2) {
                for (int k = i; k < n; k++) {
                    s = getColumn(k);
                    ub[0] = ubs[0][i];
                    ub[1] = ubs[1][i];
                    applyHouseholderTransformation(v, s, i + 1, i + 2, ub);
                    putColumn(k, s);
                }
            }
        }
    }

    /**
     * Performs second step of singular value decomposition.
     */
    private boolean sv2(MutableMatrix b, MutableMatrix d, MutableMatrix e) {
        int l, ll;
        MutableMatrix v, s;
        l = b.getNumberOfColumns();
        double bmx;
        int niter, nitermax;
        boolean ok, elzero, normalend;
        ok = true;
        elzero = true;
        nitermax = 10 * n;
        niter = 0;
        bmx = d.getElement(0);
        if (n > 1) {
            for (int i = 1; i < n; i++) {
                bmx = Math.max(Math.abs(d.getElement(i)) + Math.abs(e.getElement(i)), bmx);
            }
        }
        for (int k = n - 1; k >= 0; k--) {
            int helpcount = 0;
            normalend = false;
            iterate:
            while (k != 0 && !normalend && helpcount < 100) {
                helpcount++;
                if ((bmx + d.getElement(k)) - bmx == 0.) {
                    // since d(k) == 0, perform Givens transform with result e(k) = 0.
                    s21(d, e, k);
                }
                // find ll (1 <= ll <= k) so that either e(ll) = 0 or d(ll-1 ) = 0.)
                // In the latter case transform e(ll) to zero. In both cases the
                // matrix splits and the bottom right minor begins with row ll.
                // If no such ll is found, set ll = 0.
                int iflag = 1;
                ll = 0;

                loop:

                for (int lll = k; lll >= 0; lll--) {
                    ll = lll;
                    if (ll == 0) {
                        elzero = false;
                        iflag = 1;
                        break loop;
                    } else if ((bmx - e.getElement(ll)) - bmx == 0.) {
                        elzero = true;
                        iflag = 2;
                        break loop;
                    } else if ((bmx + d.getElement(ll - 1)) - bmx == 0.) {
                        elzero = false;
                        iflag = 3;
                    }
                }
                if ((ll > 0) && !elzero) {
                    s22(b, d, e, k, ll);
                }
                if (ll != k) {
                    // one more QR pass with order k
                    s23(b, d, e, k, ll);
                    niter++;
                    // set flag indicating non-convergence
                    if (niter <= nitermax) {
                        continue iterate;
                    }
                    ok = false;
                }
                normalend = true;
            }
            if (d.getElement(k) < 0.) {
                // for negative singular values perform change of sign
                d.setElement(k, -d.getElement(k));
                for (int j = 0; j < n; j++) {
                    setElement(j, k, -getElement(j, k));
                }
            }
            // order is decreased by one in next pass
        }
        return ok;
    }

    /**
     * Method only used by sv2, treats specal case 1.
     */
    private void s21(MutableMatrix d, MutableMatrix e, int k) {
        double v[] = new double[2];
        double cs[] = new double[2];
        double h = 0.;
        for (int i = k - 1; i >= 0; i--) {
            if (i == k - 1) {
                v[0] = d.getElement(i);
                v[1] = e.getElement(i + 1);
                defineAndApplyGivensTransformation(v, cs);
                d.setElement(i, v[0]);
                e.setElement(i + 1, v[1]);
            } else {
                v[0] = d.getElement(i);
                v[1] = h;
                defineAndApplyGivensTransformation(v, cs);
                d.setElement(i, v[0]);
                h = v[1];
            }
            if (i > 0) {
                v[0] = e.getElement(i);
                v[1] = 0.;
                applyGivensTransformation(v, cs);
                e.setElement(i, v[0]);
                h = v[1];
            }
            for (int j = 0; j < n; j++) {
                v[0] = getElement(j, i);
                v[1] = getElement(j, k);
                applyGivensTransformation(v, cs);
                setElement(j, i, v[0]);
                setElement(j, k, v[1]);
            }
        }
    }

    /**
     * Method only used by sv2, treats specal case 2.
     */
    private void s22(MutableMatrix b, MutableMatrix d, MutableMatrix e, int k, int ll) {
        int l;
        l = b.getNumberOfColumns();
        double v[] = new double[2];
        double cs[] = new double[2];
        double h = 0.;
        for (int i = ll; i <= k; i++) {
            if (i == ll) {
                v[0] = d.getElement(i);
                v[1] = e.getElement(i);
                defineAndApplyGivensTransformation(v, cs);
                d.setElement(i, v[0]);
                e.setElement(i, v[1]);
            } else {
                v[0] = d.getElement(i);
                v[1] = h;
                defineAndApplyGivensTransformation(v, cs);
                d.setElement(i, v[0]);
                h = v[1];
            }
            if (i < k) {
                v[0] = e.getElement(i + 1);
                v[1] = 0.;
                // *****************************
                applyGivensTransformation(v, cs);
                e.setElement(i + 1, v[0]);
                h = v[1];
            }
            for (int j = 0; j < l; j++) {
                v[0] = b.getElement(i, j);
                v[1] = b.getElement(l - 1, j);
                applyGivensTransformation(v, cs);
                b.setElement(i, j, v[0]);
                b.setElement(l - 1, j, v[1]);
            }
        }
    }

    /**
     * Method only used by sv2, performs QR algorithm.
     */
    private void s23(MutableMatrix b, MutableMatrix d, MutableMatrix e, int k, int ll) {
        int l;
        l = b.getNumberOfColumns();
        double v[] = new double[2];
        double cs[] = new double[2];
        double h = 0.;
        double g, f, t;
        f = ((d.getElement(k - 1) - d.getElement(k)) * (d.getElement(k - 1) + d.getElement(k))
                + (e.getElement(k - 1) - e.getElement(k)) * (e.getElement(k - 1) + e.getElement(k)))
                / (2. * e.getElement(k) * d.getElement(k - 1));
        if (Math.abs(f) > 1.E10) {
            g = Math.abs(f);
        } else {
            g = Math.sqrt(1. + f * f);
        }
        if (f >= 0.) {
            t = f + g;
        } else {
            t = f - g;
        }
        f = ((d.getElement(ll) - d.getElement(k)) * (d.getElement(ll) + d.getElement(k))
                + e.getElement(k) * (d.getElement(k - 1) / t - e.getElement(k))) / d.getElement(ll);
        for (int i = ll; i < k; i++) {
            if (i == ll) {
                // define R(ll)
                v[0] = f;
                v[1] = e.getElement(i + 1);
                defineGivensTransformation(v, cs);
            } else {
                // define R(i), i != ll
                v[0] = e.getElement(i);
                v[1] = h;
                defineAndApplyGivensTransformation(v, cs);
                e.setElement(i, v[0]);
                h = v[1];
            }
            v[0] = d.getElement(i);
            v[1] = e.getElement(i + 1);
            applyGivensTransformation(v, cs);
            d.setElement(i, v[0]);
            e.setElement(i + 1, v[1]);
            v[0] = 0.;
            v[1] = d.getElement(i + 1);
            applyGivensTransformation(v, cs);
            h = v[0];
            d.setElement(i + 1, v[1]);
            for (int j = 0; j < n; j++) {
                v[0] = getElement(j, i);
                v[1] = getElement(j, i + 1);
                applyGivensTransformation(v, cs);
                setElement(j, i, v[0]);
                setElement(j, i + 1, v[1]);
            }
            // define T(i)
            v[0] = d.getElement(i);
            v[1] = h;
            defineAndApplyGivensTransformation(v, cs);
            d.setElement(i, v[0]);
            h = v[1];
            v[0] = e.getElement(i + 1);
            v[1] = d.getElement(i + 1);
            applyGivensTransformation(v, cs);
            e.setElement(i + 1, v[0]);
            d.setElement(i + 1, v[1]);
            if (i < k - 1) {
                v[0] = 0.;
                v[1] = e.getElement(i + 2);
                applyGivensTransformation(v, cs);
                e.setElement(i + 2, v[1]);
                h = v[0];
            }
            for (int j = 0; j < l; j++) {
                v[0] = b.getElement(i, j);
                v[1] = b.getElement(i + 1, j);
                applyGivensTransformation(v, cs);
                b.setElement(i, j, v[0]);
                b.setElement(i + 1, j, v[1]);
            }
        }
    }

    /**
     * Performs third step of singular value decomposition.
     */
    private void sv3(MutableMatrix b, MutableMatrix d) {
        double t;
        int l, k;
        l = b.getNumberOfColumns();
        boolean proceed = false;
        if (n > 1) {
            loop:
            for (int i = 1; i < n; i++) {
                if (d.getElement(i) > d.getElement(i - 1)) {
                    proceed = true;
                    break loop;
                }
            }
            if (proceed) {
                for (int i = 1; i < n; i++) {
                    t = d.getElement(i - 1);
                    k = i - 1;
                    for (int j = i; j < n; j++) {
                        if (t < d.getElement(j)) {
                            t = d.getElement(j);
                            k = j;
                        }
                    }
                    if (k != i - 1) {
                        // perform permutation on singular values
                        d.setElement(k, d.getElement(i - 1));
                        d.setElement(i - 1, t);
                        // perform permutation on matrix a
                        for (int j = 0; j < n; j++) {
                            t = getElement(j, k);
                            setElement(j, k, getElement(j, i - 1));
                            setElement(j, i - 1, t);
                        }
                        // perform permutation on matrix b
                        for (int j = 0; j < l; j++) {
                            t = b.getElement(k, j);
                            b.setElement(k, j, b.getElement(i - 1, j));
                            b.setElement(i - 1, j, t);
                        }
                    }
                }
            }
        }
    }

    /**
     * Performs singular value analysis and application of Marquardt method.
     */
    public void svm(MutableMatrix b, MutableMatrix d, double lambda, MutableMatrix x1, MutableMatrix x2, double frac) {
        MutableMatrix p1, p2;
        int l, kk;
        double lambda2, lambdap, lambdap2, den1, den2, fract, g, help, sinmin, sinmax;
        final double EPSILON = 1.E-15;
        l = b.getNumberOfColumns();
        fract = Math.abs(frac);
        sinmax = 0.;
        lambda2 = lambda * lambda;
        lambdap = lambda * 0.1;
        lambdap2 = lambdap * lambdap;
        for (int i = 0; i < n; i++) {
            sinmax = Math.max(sinmax, d.getElement(i));
        }
        sinmin = sinmax * fract;
        kk = n;
        p1 = new MutableMatrix(n, 1);
        p2 = new MutableMatrix(n, 1);
        loop:
        for (int i = 0; i < n; i++) {
            if (d.getElement(i) <= sinmin) {
                kk = i - 1;
                break loop;
            }
        }
        for (int i = 0; i < m; i++) {
            g = b.getElement(i, 0);
            if (i < kk) {
                help = Math.pow(d.getElement(i), 2.);
                den1 = 1. / (help + lambda2);
                den2 = 1. / (help + lambdap2);
                p1.setElement(i, d.getElement(i) * g * den1);
                p2.setElement(i, d.getElement(i) * g * den2);
            }
        }
        for (int i = 0; i < n; i++) {
            x1.setElement(i, 0.);
            x2.setElement(i, 0.);
            for (int k = 0; k < n; k++) {
                x1.setElement(i, x1.getElement(i) + getElement(i, k) * p1.getElement(k));
                x2.setElement(i, x2.getElement(i) + getElement(i, k) * p2.getElement(k));
            }
        }
    }

    /**
     * Defines Givens transformation.
     *
     * @param v  vector elements v1, v2.
     * @param cs contains (after application of method) the two parameters cs[0] = c and cs[1] = s.
     */

    public void defineGivensTransformation(double v[], double cs[]) {
        double w, q;
        if (Math.abs(v[0]) > Math.abs(v[1])) {
            w = v[1] / v[0];
            q = Math.sqrt(1. + w * w);
            cs[0] = 1. / q;
            if (v[0] < 0) {
                cs[0] = -cs[0];
            }
            cs[1] = cs[0] * w;
        } else {
            if (v[1] != 0.) {
                w = v[0] / v[1];
                q = Math.sqrt(1. + w * w);
                cs[1] = 1. / q;
                if (v[1] < 0) {
                    cs[1] = -cs[1];
                }
                cs[0] = cs[1] * w;
            } else {
                cs[0] = 1.;
                cs[1] = 0.;
            }
        }
    }

    /**
     * Defines Givens transformation and applies it to the defining vector.
     *
     * @param v  vector elements v1, v2; after application of method it contains the transformed elements.
     * @param cs contains (after application of method) the two parameters cs[0] = c and cs[1] = s.
     */

    public void defineAndApplyGivensTransformation(double v[], double cs[]) {
        double w, q;
        if (Math.abs(v[0]) > Math.abs(v[1])) {
            w = v[1] / v[0];
            q = Math.sqrt(1. + w * w);
            cs[0] = 1. / q;
            if (v[0] < 0) {
                cs[0] = -cs[0];
            }
            cs[1] = cs[0] * w;
            v[0] = Math.abs(v[0]) * q;
            v[1] = 0.;
        } else {
            if (v[1] != 0.) {
                w = v[0] / v[1];
                q = Math.sqrt(1. + w * w);
                cs[1] = 1. / q;
                if (v[1] < 0) {
                    cs[1] = -cs[1];
                }
                cs[0] = cs[1] * w;
                v[0] = Math.abs(v[1]) * q;
                v[1] = 0.;
            } else {
                cs[0] = 1.;
                cs[1] = 0.;
            }
        }
    }

    /**
     * Applies Givens Transformation to the two vector components  z.
     *
     * @param z  vector elements z1, z2; after application of method it contains the transformed elements.
     * @param cs the two parameters cs[0] = c and cs[1] = s defining the transformation.
     */
    public void applyGivensTransformation(double z[], double cs[]) {
        double w;
        w = z[0] * cs[0] + z[1] * cs[1];
        z[1] = -z[0] * cs[1] + z[1] * cs[0];
        z[0] = w;
    }


    /**
     * Defines a Householder Transformation.
     *
     * @param v     vector.
     * @param p     vector index.
     * @param l     vector index.
     * @param param parameters of transformation (after application of method): param[0] = up, param[1] = b.
     */
    public void defineHouseholderTransformation(MutableMatrix v, int p, int l, double param[]) {
        int ne = v.getNumberOfRows();
        double c = Math.abs(v.getElement(p));
        for (int i = l; i < ne; i++) {
            c = Math.max(Math.abs(v.getElement(i)), c);
        }
        if (c > 0.) {
            double c1 = 1. / c;
            double vpprim = Math.pow(v.getElement(p) * c1, 2.);
            for (int i = l; i < ne; i++) {
                vpprim = vpprim + Math.pow(v.getElement(i) * c1, 2.);
            }
            vpprim = c * Math.sqrt(Math.abs(vpprim));
            if (v.getElement(p) > 0.) {
                vpprim = -vpprim;
            }
            param[0] = v.getElement(p) - vpprim;
            param[1] = 1. / (vpprim * param[0]);
        }
    }

    /**
     * Applies Householder Transformation to Vector c.
     *
     * @param v     transformation defining vector.
     * @param c     vector to be transformed (contains transformed vector after application).
     * @param p     vector index.
     * @param l     vector index.
     * @param param parameters of transformation: param[0] = up, param[1] = b.
     */
    public void applyHouseholderTransformation(MutableMatrix v, MutableMatrix c, int p, int l, double param[]) {
        int ne = v.getNumberOfRows();
        double s = c.getElement(p) * param[0];
        for (int i = l; i < ne; i++) {
            s = s + c.getElement(i) * v.getElement(i);
        }
        s = s * param[1];
        c.setElement(p, c.getElement(p) + s * param[0]);
        for (int i = l; i < ne; i++) {
            c.setElement(i, c.getElement(i) + s * v.getElement(i));
        }
    }
}
