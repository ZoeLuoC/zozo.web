package com.example.zozo.web.controller;

import com.example.zozo.web.model.StockHolding;
import com.example.zozo.web.model.exception.BizException;
import com.example.zozo.web.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/price/{symbol}")
    public ResponseEntity<String> getStockPrice(@PathVariable String symbol) {
        try{
            // Add a redis cache to cache the price for this symbol 60s
            // Next time when this API is being called, check redis cache first
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
        StockHolding stockHolding;
        try {
            stockHolding = stockService.addStockHolding(Id, stockSymbol, quantity, updateTime);
        } catch (BizException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(e.getMessage());
        }

        return ResponseEntity.ok(stockHolding);
    }

    @PostMapping("/add-batch-stock")
    public ResponseEntity<?> addBatchStockHolding(@RequestBody List<StockHolding> stockHoldings) {
        List<StockHolding> batchStockHolding;
        try {
            batchStockHolding  = stockService.addBatchStockHolding(stockHoldings);
        } catch (BizException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(e.getMessage());
        }
        return ResponseEntity.ok(batchStockHolding);
    }

    @GetMapping("/asset-value")
    public ResponseEntity<?> getAssetValue(@RequestParam Long id) throws Exception {
        double totalValue = stockService.calculateAssetValue(id);
        return ResponseEntity.ok("Total asset value: $" + totalValue);
    }

}
