package com.example.zozo.web.controller;

import com.example.zozo.web.model.StockHolding;
import com.example.zozo.web.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/price/{symbol}")
    public ResponseEntity<String> getStockPrice(@PathVariable String symbol) {
        try{
            double price = stockService.getStockPrice(symbol);
            return ResponseEntity.ok("The stock price of" + symbol + " is " + price);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching stock price");
        }
    }

    @GetMapping("/add-stock")
    public ResponseEntity<?> addStock(@RequestParam Long Id,
                                      @RequestParam String stockSymbol,
                                      @RequestParam int quantity,
                                      @RequestParam String updateTime) {//2024-05-25 17:00:10
        StockHolding stockHolding = stockService.addStockHolding(Id, stockSymbol, quantity, updateTime);
        return ResponseEntity.ok(stockHolding);
    }

    @GetMapping("/asset-value")
    public ResponseEntity<?> getAssetValue(@RequestParam Long id) {
        double totalValue = stockService.calculateAssetValue(id);
        return ResponseEntity.ok("Total asset value: $" + totalValue);
    }

}
