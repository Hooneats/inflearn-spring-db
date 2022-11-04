package org.example.dsl.code.lambda;

import java.util.function.Consumer;
import org.example.dsl.code.Order;
import org.example.dsl.code.Trade;
import org.example.dsl.code.Trade.Type;

public class LambdaOrderBuilder {

    private Order order = new Order();

    public static Order order(Consumer<LambdaOrderBuilder> consumer) {
        LambdaOrderBuilder builder = new LambdaOrderBuilder();
        consumer.accept(builder);
        return builder.order;
    }

    public void forCustomer(String customer) {
        order.setCustomer(customer);
    }

    public void buy(Consumer<TradeBuilder> consumer) {
        trade(consumer, Type.BYY);
    }

    public void sell(Consumer<TradeBuilder> consumer) {
        trade(consumer, Type.SELL);
    }

    private void trade(Consumer<TradeBuilder> consumer, Type type) {
        TradeBuilder builder = new TradeBuilder();
        builder.trade.setType(type);
        consumer.accept(builder);
        order.addTrade(builder.trade);
    }
}

