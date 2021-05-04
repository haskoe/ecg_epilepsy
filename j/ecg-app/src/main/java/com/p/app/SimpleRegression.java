package com.p.app;

import java.util.ArrayList;
import java.util.List;

public class SimpleRegression {
    /** sum of x values */
    private double sumX;

    /** total variation in x (sum of squared deviations from xbar) */
    private double sumXX;

    /** sum of y values */
    private double sumY;

    /** total variation in y (sum of squared deviations from ybar) */
    private double sumYY;

    /** sum of products */
    private double sumXY;

    /** number of observations */
    private long n;

    /** mean of accumulated x values, used in updating formulas */
    private double xbar;

    /** mean of accumulated y values, used in updating formulas */
    private double ybar;

    /** include an intercept or not */
    private static final boolean hasIntercept = true;

    List<Double> xl = new ArrayList<Double>();
    List<Double> yl = new ArrayList<Double>();

    // ---------------------Public methods--------------------------------------

    /**
     * Create an empty SimpleRegression instance
     */
    public SimpleRegression() {
    }

    /**
     * Adds the observation (x,y) to the regression data set.
     * <p>
     * Uses updating formulas for means and sums of squares defined in "Algorithms
     * for Computing the Sample Variance: Analysis and Recommendations", Chan, T.F.,
     * Golub, G.H., and LeVeque, R.J. 1983, American Statistician, vol. 37, pp.
     * 242-247, referenced in Weisberg, S. "Applied Linear Regression". 2nd Ed.
     * 1985.
     * </p>
     *
     *
     * @param x independent variable value
     * @param y dependent variable value
     */
    public void addData(final double x, final double y) {
        xl.add(x);
        yl.add(y);
        if (n == 0) {
            xbar = x;
            ybar = y;
        } else {
            if (hasIntercept) {
                final double fact1 = 1.0 + n;
                final double fact2 = n / (1.0 + n);
                final double dx = x - xbar;
                final double dy = y - ybar;
                sumXX += dx * dx * fact2;
                sumYY += dy * dy * fact2;
                sumXY += dx * dy * fact2;
                xbar += dx / fact1;
                ybar += dy / fact1;
            }
        }
        if (!hasIntercept) {
            sumXX += x * x;
            sumYY += y * y;
            sumXY += x * y;
        }
        sumX += x;
        sumY += y;
        n++;
    }

    /**
     * Removes the observation (x,y) from the regression data set.
     * <p>
     * Mirrors the addData method. This method permits the use of SimpleRegression
     * instances in streaming mode where the regression is applied to a sliding
     * "window" of observations, however the caller is responsible for maintaining
     * the set of observations in the window.
     * </p>
     *
     * The method has no effect if there are no points of data (i.e. n=0)
     *
     * @param x independent variable value
     * @param y dependent variable value
     */
    public void popFirst() {
        removeData(xl.get(0), yl.get(0));
        xl.remove(0);
        yl.remove(0);
    }

    public void removeData(final double x, final double y) {
        if (n > 0) {
            if (hasIntercept) {
                final double fact1 = n - 1.0;
                final double fact2 = n / (n - 1.0);
                final double dx = x - xbar;
                final double dy = y - ybar;
                sumXX -= dx * dx * fact2;
                sumYY -= dy * dy * fact2;
                sumXY -= dx * dy * fact2;
                xbar -= dx / fact1;
                ybar -= dy / fact1;
            } else {
                final double fact1 = n - 1.0;
                sumXX -= x * x;
                sumYY -= y * y;
                sumXY -= x * y;
                xbar -= x / fact1;
                ybar -= y / fact1;
            }
            sumX -= x;
            sumY -= y;
            n--;
        }
    }

    /**
     * Returns the number of observations that have been added to the model.
     *
     * @return n number of observations that have been added.
     */
    public long getN() {
        return n;
    }

    /**
     * Returns the "predicted" <code>y</code> value associated with the supplied
     * <code>x</code> value, based on the data that has been added to the model when
     * this method is activated.
     * <p>
     * <code> predict(x) = intercept + slope * x </code>
     * </p>
     * <p>
     * <strong>Preconditions</strong>:
     * <ul>
     * <li>At least two observations (with at least two different x values) must
     * have been added before invoking this method. If this method is invoked before
     * a model can be estimated, <code>Double,NaN</code> is returned.</li>
     * </ul>
     *
     * @param x input <code>x</code> value
     * @return predicted <code>y</code> value
     */
    public double predict(final double x) {
        final double b1 = getSlope();
        if (hasIntercept) {
            return getIntercept(b1) + b1 * x;
        }
        return b1 * x;
    }

    /**
     * Returns the intercept of the estimated regression line, if
     * {@link #hasIntercept()} is true; otherwise 0.
     * <p>
     * The least squares estimate of the intercept is computed using the
     * <a href="http://www.xycoon.com/estimation4.htm">normal equations</a>. The
     * intercept is sometimes denoted b0.
     * </p>
     * <p>
     * <strong>Preconditions</strong>:
     * <ul>
     * <li>At least two observations (with at least two different x values) must
     * have been added before invoking this method. If this method is invoked before
     * a model can be estimated, <code>Double,NaN</code> is returned.</li>
     * </ul>
     *
     * @return the intercept of the regression line if the model includes an
     *         intercept; 0 otherwise
     * @see #SimpleRegression(boolean)
     */
    public double getIntercept() {
        return hasIntercept ? getIntercept(getSlope()) : 0.0;
    }

    /**
     * Returns true if the model includes an intercept term.
     *
     * @return true if the regression includes an intercept; false otherwise
     * @see #SimpleRegression(boolean)
     */
    public boolean hasIntercept() {
        return hasIntercept;
    }

    /**
     * Returns the slope of the estimated regression line.
     * <p>
     * The least squares estimate of the slope is computed using the
     * <a href="http://www.xycoon.com/estimation4.htm">normal equations</a>. The
     * slope is sometimes denoted b1.
     * </p>
     * <p>
     * <strong>Preconditions</strong>:
     * <ul>
     * <li>At least two observations (with at least two different x values) must
     * have been added before invoking this method. If this method is invoked before
     * a model can be estimated, <code>Double.NaN</code> is returned.</li>
     * </ul>
     *
     * @return the slope of the regression line
     */
    public double getSlope() {
        if (n < 2) {
            return Double.NaN; // not enough data
        }
        if (Math.abs(sumXX) < 10 * Double.MIN_VALUE) {
            return Double.NaN; // not enough variation in x
        }
        return sumXY / sumXX;
    }

    /**
     * Returns the intercept of the estimated regression line, given the slope.
     * <p>
     * Will return <code>NaN</code> if slope is <code>NaN</code>.
     * </p>
     *
     * @param slope current slope
     * @return the intercept of the regression line
     */
    private double getIntercept(final double slope) {
        if (hasIntercept) {
            return (sumY - slope * sumX) / n;
        }
        return 0.0;
    }
}
