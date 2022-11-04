package org.example.dsl;

import org.example.dsl.code.Order;
import org.example.dsl.code.mix.MixedBuilder;

public class MixedDSLV4 {

    public static void main(String[] args) {
        Order order = MixedBuilder.forCustomer(
            "BigBank",
            MixedBuilder.buy(
                t -> t.quantity(80)
                    .stock("IBM")
                    .on("NYSE")
                    .at(125.00)
            ),
            MixedBuilder.sell(
                t -> t.quantity(50)
                    .stock("GOOGLE")
                    .on("NASDAQ")
                    .at(125.00)
            )
        );
    }

}
