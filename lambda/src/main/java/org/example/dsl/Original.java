package org.example.dsl;

import org.example.dsl.code.Order;
import org.example.dsl.code.Stock;
import org.example.dsl.code.Tax;
import org.example.dsl.code.TaxCalculator;
import org.example.dsl.code.Trade;
import org.example.dsl.code.Trade.Type;
import org.example.dsl.code.function.FunctionUtils;

public class Original {

    public static void main(String[] args) {
        // TODO : 1) MethodChain , 2) NestedFunction , 3) Lambda , 4) Mixed Chain,Nested,Lambda
        Order order = new Order();
        order.setCustomer("BigBank");

        Trade trade1 = new Trade();
        trade1.setType(Type.BYY);

        Stock stock1 = new Stock();
        stock1.setSymbol("IBM");
        stock1.setMarket("NYSE");

        trade1.setStock(stock1);
        trade1.setPrice(125.00);
        trade1.setQuantity(80);
        order.addTrade(trade1);

        Trade trade2 = new Trade();
        trade2.setType(Type.BYY);

        Stock stock2 = new Stock();
        stock1.setSymbol("GOOGLE");
        stock1.setMarket("NASDAQ");

        trade2.setStock(stock2);
        trade2.setPrice(375.00);
        trade2.setQuantity(50);
        order.addTrade(trade2);

        // TODO : Tax example
        double value = Tax.calculate(order, true, false, true);

        // TODO : Tax advanced
        double advancedValue =
            new TaxCalculator()
                .with(Tax::regional)
                .with(Tax::surcharge)
                .calculator(order);

        Object end = FunctionUtils.start(advancedValue)
            .consume(System.out::println)
            .of(() -> 9)
            .end();
        System.out.println("end = " + end);
    }

}
