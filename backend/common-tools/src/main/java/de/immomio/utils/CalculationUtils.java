package de.immomio.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculationUtils {
    private final static int DECIMALS_AFTER_POINT = 2;

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static BigDecimal divide(double numerator, double denominator) {
        if(denominator == 0d) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(numerator).divide(BigDecimal.valueOf(denominator), DECIMALS_AFTER_POINT, RoundingMode.HALF_UP);
    }
}
