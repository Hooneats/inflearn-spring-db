package org.example.dsl.code;

public class Tax {

    public static double regional(double value) {
        return value * 1.1;
    }

    public static double general(double value) {
        return value * 1.3;
    }

    public static double surcharge(double value) {
        return value * 1.5;
    }

    public static double calculate(Order order, boolean useRegional, boolean useGeneral,
        boolean useSurcharge) {
        double value = order.getValue();
        if (useRegional) value = Tax.regional(value);
        if (useGeneral) value = Tax.regional(value);
        if (useSurcharge) value = Tax.regional(value);
        return value;
    }
}
