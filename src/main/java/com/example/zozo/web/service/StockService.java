package com.example.zozo.web.service;

import com.example.zozo.web.model.StockHolding;
import com.example.zozo.web.model.User;
import com.example.zozo.web.repository.StockHoldingRepository;
import com.example.zozo.web.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class StockService {

    private final StockPriceService stockPriceService;
    private final UserRepository userRepository;
    private final StockHoldingRepository stockHoldingRepository;

    public StockService(UserRepository userRepository,
                        StockHoldingRepository stockHoldingRepository,
                        StockPriceService stockPriceService) {
        this.userRepository = userRepository;
        this.stockHoldingRepository = stockHoldingRepository;
        this.stockPriceService = stockPriceService;
    }

    public StockHolding addStockHolding(Long Id, String stockSymbol, int quantity, String timestamp) {
        Optional<User> userOpt = userRepository.findById(Id);
        if(userOpt.isEmpty()) {
            throw new RuntimeException("User not found with Id: " + Id);
        }

        User user = userOpt.get();
        StockHolding stockHolding = stockHoldingRepository.findByStockSymbol(user.getId(), stockSymbol)
                .map(sh -> {
                    if(timestamp.compareTo(sh.getUpdateTime()) > 0) {
                       int updatedRow = stockHoldingRepository.updateStockHold(user.getId(), quantity, stockSymbol, sh.getUpdateTime(), timestamp);
                    }
                    sh.setQuantity(quantity);
                    return sh;
                })
                .orElseGet(() -> {
                    StockHolding newStockHolding = new StockHolding();
                    newStockHolding.setUserId(user.getId());
                    newStockHolding.setStockSymbol(stockSymbol);
                    newStockHolding.setQuantity(quantity);
                    newStockHolding.setUpdateTime(timestamp);
                    return stockHoldingRepository.save(newStockHolding);
                });

        return stockHolding;
    }

    public double calculateAssetValue(Long Id) throws Exception {
        return stockPriceService.calculateAsset(Id);
    }

    public double getStockPrice(String symbol) throws Exception {
        return stockPriceService.getStockPrice(symbol);
    }
}
