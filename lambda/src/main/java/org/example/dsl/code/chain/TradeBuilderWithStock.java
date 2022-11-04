package org.example.dsl.code.chain;

import org.example.dsl.code.Trade;

public class TradeBuilderWithStock {

    private final MethodChainOrderBuilder builder;
    private final Trade trade;

    public TradeBuilderWithStock(MethodChainOrderBuilder builder, Trade trade) {
        this.builder = builder;
        this.trade = trade;
    }

    public MethodChainOrderBuilder at(double price) {
        trade.setPrice(price);
        return builder.addTrade(trade);
    }
}
