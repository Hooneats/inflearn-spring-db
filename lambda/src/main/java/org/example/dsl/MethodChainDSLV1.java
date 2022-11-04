package org.example.dsl;

import org.example.dsl.code.Order;
import org.example.dsl.code.chain.MethodChainOrderBuilder;

public class MethodChainDSLV1 {

    public static void main(String[] args) {
        Order order = MethodChainOrderBuilder.forCustomer("BigBank")
            .buy(80)
            .stock("IBM")
            .on("NYSE")
            .at(125.00)
            .sell(50)
            .stock("GOOGLE")
            .on("NASDAQ")
            .at(375.00)
            .end();
    }

}
