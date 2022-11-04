package org.example.dsl.code.chain;

import org.example.dsl.code.Stock;
import org.example.dsl.code.Trade;

public class StockBuilder {

    private final MethodChainOrderBuilder builder;
    private final Trade trade;
    private final Stock stock = new Stock();

    public StockBuilder(MethodChainOrderBuilder builder, Trade trade, String symbol) {
        this.builder = builder;
        this.trade = trade;
        stock.setSymbol(symbol);
    }

    public TradeBuilderWithStock on(String market) {
        stock.setMarket(market);
        trade.setStock(stock);
        return new TradeBuilderWithStock(builder, trade);
    }
}
