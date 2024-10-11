package com.example.zozo.web.service;

import com.example.zozo.web.client.StockClient;
import com.example.zozo.web.model.StockPriceResponse;
import com.example.zozo.web.model.StockHolding;
import com.example.zozo.web.model.User;
import com.example.zozo.web.model.UserStockHolding;
import com.example.zozo.web.repository.StockHoldingRepository;
import com.example.zozo.web.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;


@Service
public class StockService {

    @Autowired
    private final StockClient stockClient;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_KEY = "AOW93E6NDJY6O8I8"; //API KEY

    private final UserRepository userRepository;
    private final StockHoldingRepository stockHoldingRepository;

    public StockService(UserRepository userRepository,
                        StockHoldingRepository stockHoldingRepository,
                        StockClient stockClient) {
        this.userRepository = userRepository;
        this.stockHoldingRepository = stockHoldingRepository;
        this.stockClient = stockClient;
    }

    public StockHolding addStockHolding(Long Id, String stockSymbol, int quantity, String timestamp) {
        Optional<User> userOpt = userRepository.findById(Id);
        if(!userOpt.isPresent()) {
            throw new RuntimeException("User not found with Id: " + Id);
        }
        User user = userOpt.get();

        StockHolding stockHolding = stockHoldingRepository.findByStockSymbol(user.getId(), stockSymbol)
                .map(sh -> {
                    if(timestamp.compareTo(sh.getTimestamp()) > 0) {
                       int updatedRow = stockHoldingRepository.updateStockHold(user.getId(), quantity, stockSymbol, sh.getTimestamp(), timestamp);
                    }
                    sh.setQuantity(quantity);
                    return sh;
                })
                .orElseGet(() -> {
                    StockHolding newStockHolding = new StockHolding();
                    newStockHolding.setId(Id);
                    newStockHolding.setStockSymbol(stockSymbol);
                    newStockHolding.setQuantity(quantity);
                    newStockHolding.setTimestamp(timestamp);
                    return stockHoldingRepository.save(newStockHolding);
                });

        return stockHolding;
    }

    public double calculateAssetValue(Long Id) {
        Optional<User> userOpt = userRepository.findById(Id);

        if(!userOpt.isPresent()) {
            throw new RuntimeException("User not found with Id: " + Id);
        }

        User user = userOpt.get();

        double totalValue = 0.0;

        List<StockHolding> holdings = stockHoldingRepository.findByStocksByUserId(user.getId()).get();
        if (holdings.isEmpty()) {
            throw new RuntimeException("No stock holdings found for Id: " + Id);
        }
        for (StockHolding holding : holdings) {
            StockPriceResponse priceResponse = stockClient.getStockPrice(
                    "TIME_SERIES_INTRADAY",
                    holding.getStockSymbol(),
                    "60", API_KEY).getBody();
            double currentPrice = priceResponse.getCurrentPrice();
            totalValue += holding.getQuantity() * currentPrice;
        }

        return totalValue;
    }

    public double getStockPrice(String symbol) {
        try {
            // Call the stock API via FeignClient
            StockPriceResponse response = stockClient.getStockPrice(
                    "TIME_SERIES_INTRADAY", symbol, "1min", API_KEY).getBody();

            // Parse the JSON to get the current price (pseudo-code, depending on your API)
            double currentPrice = parsePriceFromResponse(String.valueOf(response.getCurrentPrice()));
            return currentPrice;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching stock price for " + symbol, e);
        }
    }
    // This method needs to be implemented to parse the JSON response based on the API's format
    private double parsePriceFromResponse(String response) {
        // Parse the JSON response to extract the stock price
        return 0.0;
    }
}
