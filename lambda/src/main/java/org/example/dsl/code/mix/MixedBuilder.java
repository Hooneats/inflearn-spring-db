package org.example.dsl.code.mix;

import java.util.function.Consumer;
import java.util.stream.Stream;
import org.example.dsl.code.Order;
import org.example.dsl.code.Trade;
import org.example.dsl.code.Trade.Type;

public class MixedBuilder {

    public static TradeBuilder buy(Consumer<TradeBuilder> consumer) {
        return builderTrade(consumer, Type.BYY);
    }

    public static TradeBuilder sell(Consumer<TradeBuilder> consumer) {
        return builderTrade(consumer, Type.SELL);
    }

    private static TradeBuilder builderTrade(Consumer<TradeBuilder> consumer, Type type) {
        TradeBuilder builder = new TradeBuilder();
        builder.trade.setType(type);
        consumer.accept(builder);
        return builder;
    }

    public static Order forCustomer(String customer, TradeBuilder... builders) {
        Order order = new Order();
        order.setCustomer(customer);
        Stream.of(builders).forEach(b -> order.addTrade(b.trade));
        return order;
    }
}
