package com.example.zozo.web.client;

import com.example.zozo.web.model.StockPriceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "stockClient", url = "https://www.alphavantage.co")
public interface StockClient {

    @GetMapping("/query")
    ResponseEntity<StockPriceResponse> getStockPrice(@RequestParam("function") String function,
                                        @RequestParam("symbol") String symbol,
                                        @RequestParam("apikey") String apikey);

}
