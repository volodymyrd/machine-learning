package com.gmail.volodymyrdotsenko.ml.libs.optimalg.utils;

/**
 * A class determining the extremum of a parabola through three points (xa,ya), (xb,yb),(xb,yc).
 */
public class MinParab {
    private final static double EPSILONPARAB = 1.E-10;

    /**
     * gets position of extremum
     */
    public static double getPositionOfExtremum(double xa, double ya, double xb, double yb, double xc, double yc) {
        double xba, xbc, f1, f2, anum, den, xmp;
        xba = xb - xa;
        xbc = xb - xc;
        f1 = xba * (yb - yc);
        f2 = xbc * (yb - ya);
        anum = xba * f1 - xbc * f2;
        den = f1 - f2;
        den = Math.signum(den) * Math.max(Math.abs(den), EPSILONPARAB);
        xmp = xb - anum / (2. * den);
        return xmp;
    }
}
