import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * Helper class for the RSA assignment. This class provides the method 'cbrt'
 * which returns the cube root of a BigInteger.
 *
 * The code is taken from
 * "A Java Math.BigDecimal Implementation of Core Mathematical Functions"
 * Available at: http://arxiv.org/abs/0908.3030
 */
public class CubicRoot {

    /**
     * Returns the cube root for big integers.
     *
     * @param val
     *            Value to compute the cube root of.
     * @return (Rounded down) cube root of argument. That is, a value x such
     *         that x*x*x = val.
     */
    public BigInteger cbrt(BigInteger val) {
        return root(3, new BigDecimal(val)).toBigInteger();
    }

    /**
     * The integer root.
     *
     * @param n
     *            the positive argument.
     * @param x
     *            the non-negative argument.
     * @return The n-th root of the BigDecimal rounded to the precision implied
     *         by x, x^(1/n).
     */
   public BigDecimal root(final int n, final BigDecimal x) {
        if (x.compareTo(BigDecimal.ZERO) < 0) {
            throw new ArithmeticException("negative argument " + x.toString()
                    + " of root");
        }
        if (n <= 0) {
            throw new ArithmeticException("negative power " + n + " of root");
        }
        if (n == 1) {
            return x;
        }
        /* start the computation from a double precision estimate */
        BigDecimal s = new BigDecimal(Math.pow(x.doubleValue(), 1.0 / n));
        /*
         * this creates nth with nominal precision of 1 digit
         */
        final BigDecimal nth = new BigDecimal(n);
        /*
         * Specify an internal accuracy within the loop which is slightly larger
         * than what is demanded by Ã‚epsÃ‚ below.
         */
        final BigDecimal xhighpr = scalePrec(x, 2);
        MathContext mc = new MathContext(2 + x.precision());
        /*
         * Relative accuracy of the result is eps.
         */
        final double eps = x.ulp().doubleValue() / (2 * n * x.doubleValue());
        for (;;) {
            /*
             * s = s -(s/n-x/n/s^(n-1)) = s-(s-x/s^(n-1))/n; test correction
             * s/n-x/s for being smaller than the precision requested. The
             * relative correction is (1-x/s^n)/n,
             */
            BigDecimal c = xhighpr.divide(s.pow(n - 1), mc);
            c = s.subtract(c);
            MathContext locmc = new MathContext(c.precision());
            c = c.divide(nth, locmc);
            s = s.subtract(c);
            if (Math.abs(c.doubleValue() / s.doubleValue()) < eps) {
                break;
            }
        }
        return s.round(new MathContext(err2prec(eps)));
    } /* BigDecimalMath.root */

    /**
     * Append decimal zeros to the value. This returns a value which appears to
     * have a higher precision than the input.
     *
     * @param x
     *            The input value
     * @param d
     *            The (positive) value of zeros to be added as least significant
     *            digits.
     * @return The same value as the input but with increased (pseudo)
     *         precision.
     */
    static private BigDecimal scalePrec(final BigDecimal x, int d) {
        return x.setScale(d + x.scale());
    }

    /**
     * Convert a relative error to a precision.
     *
     * @param xerr
     *            The relative error in the variable. The value returned depends
     *            only on the absolute value, not on the sign.
     * @return The number of valid digits in x. The value is rounded down, and
     *         on the pessimistic side for that reason.
     */
    static private int err2prec(double xerr) {
        /*
         * Example: an error of xerr=+-0.5 a precision of 1 (digit), an error of
         * +-0.05 a precision of 2 (digits)
         */
        return 1 + (int) (Math.log10(Math.abs(0.5 / xerr)));
    }

}

