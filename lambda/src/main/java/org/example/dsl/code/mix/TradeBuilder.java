package org.example.dsl.code.mix;

import org.example.dsl.code.Trade;
import org.example.dsl.code.nested.NestedFunctionOrderBuilder;

public class TradeBuilder {

    public Trade trade = new Trade();

    public TradeBuilder quantity(int quantity) {
        trade.setQuantity(quantity);
        return this;
    }

    public TradeBuilder at(double price) {
        trade.setPrice(price);
        return this;
    }

    public StockBuilder stock(String symbol) {
        return new StockBuilder(this, trade, symbol);
    }
}
