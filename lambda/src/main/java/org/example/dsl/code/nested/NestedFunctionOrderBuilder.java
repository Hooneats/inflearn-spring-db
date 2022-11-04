package org.example.dsl.code.nested;

import java.util.stream.Stream;
import org.example.dsl.code.Order;
import org.example.dsl.code.Stock;
import org.example.dsl.code.Trade;
import org.example.dsl.code.Trade.Type;

public class NestedFunctionOrderBuilder {

    public static Stock stock(String symbol, String market) {
        Stock stock = new Stock();
        stock.setSymbol(symbol);
        stock.setMarket(market);
        return stock;
    }

    public static double at(double price) {
        return price;
    }

    public static String on(String market) {
        return market;
    }

    public static Trade sell(int quantity, Stock stock, double price) {
        return buildTrade(quantity, stock, price, Type.SELL);
    }

    public static Trade buy(int quantity, Stock stock, double price) {
        return buildTrade(quantity, stock, price, Type.BYY);
    }

    private static Trade buildTrade(int quantity, Stock stock, double price, Type type) {
        Trade trade = new Trade();
        trade.setQuantity(quantity);
        trade.setStock(stock);
        trade.setPrice(price);
        trade.setType(type);
        return trade;
    }

    public static Order order(String customer, Trade... trades) {
        Order order = new Order();
        order.setCustomer(customer);
        Stream.of(trades).forEach(order::addTrade);
        return order;
    }
}
