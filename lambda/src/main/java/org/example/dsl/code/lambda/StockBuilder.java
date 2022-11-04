package org.example.dsl.code.lambda;

import org.example.dsl.code.Stock;

public class StockBuilder {

    public Stock stock = new Stock();

    public void symbol(String symbol) {
        stock.setSymbol(symbol);
    }

    public void market(String market) {
        stock.setMarket(market);
    }
}
