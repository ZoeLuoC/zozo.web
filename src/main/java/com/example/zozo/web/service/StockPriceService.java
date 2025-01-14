package com.example.zozo.web.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.example.zozo.web.client.StockClient;
import com.example.zozo.web.model.StatesAndEvents;
import com.example.zozo.web.model.StockHolding;
import com.example.zozo.web.model.StockPriceResponse;
import com.example.zozo.web.model.User;
import com.example.zozo.web.repository.StockHoldingRepository;
import com.example.zozo.web.repository.UserRepository;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class StockPriceService {

    private final UserRepository userRepository;
    private final StockHoldingRepository stockHoldingRepository;
    private final RedisCommands<String, String> redisCommands;
    private final StockClient stockClient;
    private final String API_KEY = "AOW93E6NDJY6O8I8";
    private final StateMachine<StatesAndEvents.GetStockPriceStates, StatesAndEvents.GetStockPriceEvents> tradeStateMachine;
    boolean isSuccessful = true; // Simulate success

    @Autowired
    public StockPriceService(RedisCommands<String, String> redisCommands,
                             StockClient stockClient,
                             UserRepository userRepository,
                             StockHoldingRepository stockHoldingRepository,
                             StateMachine<StatesAndEvents.GetStockPriceStates, StatesAndEvents.GetStockPriceEvents> tradeStateMachine) {
        this.redisCommands = redisCommands;
        this.stockClient = stockClient;
        this.userRepository = userRepository;
        this.stockHoldingRepository = stockHoldingRepository;
        this.tradeStateMachine = tradeStateMachine;
    }

    @SentinelResource(value = "getStockPrice", fallback = "stockPriceFallback", blockHandler = "blockHandler")
    public double getStockPrice(String stockSymbol) throws Exception {
        String redisKey = "stock:price:" + stockSymbol;

        //Check if the price is cached in Redis
        String cachedPrice = redisCommands.get(redisKey);
        if (cachedPrice != null) {
            return Double.parseDouble(cachedPrice);
        }

        StockPriceResponse response = stockClient.getStockPrice(
                "GLOBAL_QUOTE",
                stockSymbol,
                API_KEY).getBody();
        if (response == null || response.getGlobalQuote() == null || response.getGlobalQuote().getPrice() == null) {
            isSuccessful = false;
            throw new Exception("Unavailable stock price");
        }

        double stockPrice = Double.parseDouble(response.getGlobalQuote().getPrice());
        redisCommands.setex(redisKey,Duration.ofMinutes(15).getSeconds(), Double.toString(stockPrice));

        return stockPrice;
    }
    public double stockPriceFallback(String function, String symbol, String apiKey, Throwable throwable) {
        return 0d;
    }
    public double blockHandler(String function, String symbol, String apiKey, Throwable throwable) {
        return 0d;
    }

    public void processTrade() {
        tradeStateMachine.start(); // Start the state machine
        tradeStateMachine.sendEvent(StatesAndEvents.GetStockPriceEvents.START_PROCESS); // Trigger event to transition states

        if(isSuccessful) {
            tradeStateMachine.sendEvent(StatesAndEvents.GetStockPriceEvents.SUCCESS); // Move to COMPLETED state
        } else {
            tradeStateMachine.sendEvent(StatesAndEvents.GetStockPriceEvents.FAILURE); // Move to FAILED state
        }
    }
    public double calculateAsset(Long userId) throws Exception {
        Optional<User> userOpt = userRepository.findById(userId);

        if(userOpt.isEmpty()) {
            throw new RuntimeException("User not found with userId: " + userId);
        }

        User user = userOpt.get();
        double totalAsset = 0.0;

        List<StockHolding> stockHoldings = stockHoldingRepository.findByStocksByUserId(user.getId()).get();
        if(stockHoldings.isEmpty()) {
            throw new RuntimeException("No stock holdings found for Id: " + userId);
        }

        for(StockHolding stockHolding : stockHoldings) {
            double stockPrice = getStockPrice(stockHolding.getStockSymbol());
            totalAsset += stockPrice * stockHolding.getQuantity();
        }
        return totalAsset;
    }
}
