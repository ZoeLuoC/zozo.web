package com.example.zozo.web.client;

import com.example.zozo.web.model.StockPriceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "stockClient", url = "https://www.alphavantage.co")
public interface StockClient {

    @GetMapping("/query-stock-price")
    ResponseEntity<StockPriceResponse> getStockPrice(@RequestParam("function") String function,
                                        @RequestParam("symbol") String symbol,
                                        @RequestParam("interval") String interval,
                                        @RequestParam("apikey") String apikey);

    @GetMapping("/real-time-price")
    StockPriceResponse getCurrentPrice(@RequestParam("symbol") String symbol);
}
