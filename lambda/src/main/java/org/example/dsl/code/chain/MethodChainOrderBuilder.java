package org.example.dsl.code.chain;

import org.example.dsl.code.Order;
import org.example.dsl.code.Trade;
import org.example.dsl.code.Trade.Type;

public class MethodChainOrderBuilder {

    public final Order order = new Order();

    private MethodChainOrderBuilder(String customer) {
        order.setCustomer(customer);
    }

    public static MethodChainOrderBuilder forCustomer(String customer) {
        return new MethodChainOrderBuilder(customer);
    }

    public TradeBuilder buy(int quantity) {
        return new TradeBuilder(this, Type.BYY, quantity);
    }

    public TradeBuilder sell(int quantity) {
        return new TradeBuilder(this, Type.SELL, quantity);
    }

    public MethodChainOrderBuilder addTrade(Trade trade) {
        order.addTrade(trade);
        return this;
    }

    public Order end() {
        return order;
    }
}
