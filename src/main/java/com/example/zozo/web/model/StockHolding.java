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
    private int quantity;

    @Column(name = "lastUpdated", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private String timestamp;

    public StockHolding() {}

    StockHolding(Long id, String symbol, int quantity, String timestamp) {
        this.id = id;
        this.stockSymbol = symbol;
        this.quantity = quantity;
        this.timestamp = timestamp;
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

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
