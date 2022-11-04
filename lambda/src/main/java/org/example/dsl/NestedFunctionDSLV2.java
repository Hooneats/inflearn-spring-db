package org.example.dsl;

import org.example.dsl.code.Order;
import org.example.dsl.code.nested.NestedFunctionOrderBuilder;

public class NestedFunctionDSLV2 {

    public static void main(String[] args) {
        Order order = NestedFunctionOrderBuilder.order(
            "BigBank",
            NestedFunctionOrderBuilder.buy(
                80,
                NestedFunctionOrderBuilder.stock(
                    "IBM",
                    NestedFunctionOrderBuilder.on("NYSE")
                ),
                NestedFunctionOrderBuilder.at(125.00)
            ),
            NestedFunctionOrderBuilder.sell(
                50,
                NestedFunctionOrderBuilder.stock(
                    "GOOGLE",
                    NestedFunctionOrderBuilder.on("NASDAQ")
                ),
                NestedFunctionOrderBuilder.at(375.00)
            )
        );
    }

}
