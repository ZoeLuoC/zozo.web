package com.example.zozo.web.repository;

import com.example.zozo.web.model.StockHolding;
import com.example.zozo.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockHoldingRepository extends JpaRepository<StockHolding, Long> {
    @Query(value = "SELECT * FROM StockHolding WHERE userId = :userId", nativeQuery = true)
    Optional<List<StockHolding>> findByStocksByUserId(long userId);

    @Query(value = "SELECT * FROM StockHolding WHERE userId = :userId and stockSymbol = :symbol", nativeQuery = true)
    Optional<StockHolding> findByStockSymbol(long userId, String symbol);

    @Query(value = "UPDATE StockHolding SET quantity = :quantity, updateTime = :updateTime WHERE userId = :userId AND stockSymbol = :symbol AND updateTime = :orgUpdateTime", nativeQuery = true)
    int updateStockHold(long userId, int quantity, String symbol, String orgUpdateTime, String updateTime);
}
