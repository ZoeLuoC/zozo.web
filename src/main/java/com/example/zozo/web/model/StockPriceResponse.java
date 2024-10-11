package com.example.zozo.web.model;

public class StockPriceResponse {

    private String symbol;
    private int quantity;
    private double currentPrice;
    private double totalValue;

    // Constructors, getters, and setters
    public StockPriceResponse() {}

    public StockPriceResponse(String symbol, int quantity, double currentPrice, double totalValue) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.currentPrice = currentPrice;
        this.totalValue = totalValue;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }
}
