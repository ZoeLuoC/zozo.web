package com.example.zozo.web.model;

import jakarta.persistence.*;

@Entity
@Table(name = "StockHolding")
public class StockHolding {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String stockSymbol;

    @Column
    private int quantity;

    @Column
    private String updateTime;

    private Long userId;

    public StockHolding() {}

    StockHolding(String symbol, int quantity, String updateTime, Long userId) {
        this.userId = userId;
        this.stockSymbol = symbol;
        this.quantity = quantity;
        this.updateTime = updateTime;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }
    public void setStockSymbol(String symbol) {
        this.stockSymbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
