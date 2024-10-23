package com.example.zozo.web.service;

import com.example.zozo.web.model.StockHolding;
import com.example.zozo.web.model.User;
import com.example.zozo.web.model.exception.BizException;
import com.example.zozo.web.repository.StockHoldingRepository;
import com.example.zozo.web.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


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

    public StockHolding addStockHolding(Long Id, String stockSymbol, int quantity, String timestamp) throws BizException {
        Optional<User> userOpt = userRepository.findById(Id);
        if(userOpt.isEmpty()) {
            throw new BizException("User not found with Id: " + Id);
        }

        User user = userOpt.get();
        Optional<StockHolding> stockHolding = stockHoldingRepository.findByStockSymbol(user.getId(), stockSymbol);
        if (stockHolding.isPresent()) {
            int updatedRow = 0;
            if(timestamp.compareTo(stockHolding.get().getUpdateTime()) > 0) {
                updatedRow = stockHoldingRepository.updateStockHold(user.getId(), quantity, stockSymbol, stockHolding.get().getUpdateTime(), timestamp);
            }

            if (updatedRow == 1) {
                stockHolding.get().setQuantity(quantity);
                stockHolding.get().setUpdateTime(timestamp);
                return stockHolding.get();
            }
            return null;
        } else {
            StockHolding newStockHolding = new StockHolding();
            newStockHolding.setUserId(user.getId());
            newStockHolding.setStockSymbol(stockSymbol);
            newStockHolding.setQuantity(quantity);
            newStockHolding.setUpdateTime(timestamp);
            return stockHoldingRepository.save(newStockHolding);
        }
    }

    @Transactional(rollbackFor = { BizException.class, RuntimeException.class })
    public List<StockHolding> addBatchStockHolding(List<StockHolding> stockHoldings) throws BizException {
        List<StockHolding> batchStockHolding = new ArrayList<>();
        for (StockHolding stockHolding : stockHoldings) {
            Long id = stockHolding.getUserId();
            String stockSymbol = stockHolding.getStockSymbol();
            int quantity = stockHolding.getQuantity();
            String timestamp = stockHolding.getUpdateTime();

            StockHolding sh = addStockHolding(id, stockSymbol, quantity, timestamp);
            if (sh == null) {
                throw new BizException("Add stock holding failed");
            }

            batchStockHolding.add(sh);
        }
        return batchStockHolding;
    }

    public double calculateAssetValue(Long Id) throws Exception {
        return stockPriceService.calculateAsset(Id);
    }

    public double getStockPrice(String symbol) throws Exception {
        return stockPriceService.getStockPrice(symbol);
    }
}
