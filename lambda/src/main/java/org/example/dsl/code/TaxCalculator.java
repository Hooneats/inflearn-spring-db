package org.example.dsl.code;

import java.util.function.DoubleUnaryOperator;

public class TaxCalculator {

    public DoubleUnaryOperator taxFunction = d -> d;

    public TaxCalculator with(DoubleUnaryOperator f) {
        taxFunction = taxFunction.andThen(f);
        return this;
    }

    public double calculator(Order order) {
        return taxFunction.applyAsDouble(order.getValue());
    }

}
