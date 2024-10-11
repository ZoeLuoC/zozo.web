package com.example.zozo.web.model;

import jakarta.persistence.*;

public class UserStockHolding {

    private User user;

    private StockHolding stockHolding;

    public UserStockHolding(User user, StockHolding stockholding) {
        this.user = user;
        this.stockHolding = stockholding;

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StockHolding getStockHolding() {
        return stockHolding;
    }

    public void setStockHolding(StockHolding stockHolding) {
        this.stockHolding = stockHolding;
    }
}
