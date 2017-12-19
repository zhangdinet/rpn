package com.zhangdi.rpn;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * BigDecimal开跟运算类
 */
public class BigDecimalMath {

    //精度
    private static final int EXPECTED_INITIAL_PRECISION = 16;

    private static BigDecimal TWO = BigDecimal.ONE.add(BigDecimal.ONE);

    public static BigDecimal sqrt(BigDecimal x){
        return sqrt(x,MathContext.DECIMAL128);
    }

    public static BigDecimal sqrt(BigDecimal x, MathContext mathContext) {
        switch (x.signum()) {
            case 0:
                return BigDecimal.ZERO;
            case -1:
                throw new ArithmeticException("Illegal sqrt(x) for x < 0: x = " + x);
        }

        int maxPrecision = mathContext.getPrecision() + 4;
        BigDecimal acceptableError = BigDecimal.ONE.movePointLeft(mathContext.getPrecision() + 1);

        BigDecimal result;
        if (isDoubleValue(x)) {
            result = BigDecimal.valueOf(Math.sqrt(x.doubleValue()));
        } else {
            result = x.divide(TWO, mathContext);
        }

        if (result.multiply(result, mathContext).compareTo(x) == 0) {
            return result.round(mathContext);
        }

        int adaptivePrecision = EXPECTED_INITIAL_PRECISION;
        BigDecimal last;
        do {
            last = result;
            adaptivePrecision = adaptivePrecision * 2;
            if (adaptivePrecision > maxPrecision) {
                adaptivePrecision = maxPrecision;
            }
            MathContext mc = new MathContext(adaptivePrecision, mathContext.getRoundingMode());
            result = x.divide(result, mc).add(last, mc).divide(TWO, mc);
        } while (adaptivePrecision < maxPrecision || result.subtract(last).abs().compareTo(acceptableError) > 0);

        return result.round(mathContext);
    }

    public static boolean isDoubleValue(BigDecimal value) {
        if (value.compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) > 0) {
            return false;
        }
        if (value.compareTo(BigDecimal.valueOf(Double.MAX_VALUE).negate()) < 0) {
            return false;
        }
        return true;
    }
}
