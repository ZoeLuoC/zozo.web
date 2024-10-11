package com.example.zozo.web.controller;

import com.example.zozo.web.model.StockPriceResponse;
import com.example.zozo.web.model.StockHolding;
import com.example.zozo.web.model.User;
import com.example.zozo.web.model.UserStockHolding;
import com.example.zozo.web.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
                                      @RequestParam String updateTime) {
        StockHolding stockHolding = stockService.addStockHolding(Id, stockSymbol, quantity, updateTime);
        return ResponseEntity.ok(stockHolding);
    }

    @PostMapping("/asset-value")
    public ResponseEntity<?> getAssetValue(@RequestParam Long Id) {
        double totalValue = stockService.calculateAssetValue(Id);
        return ResponseEntity.ok("Total asset value: $" + totalValue);
    }

}
