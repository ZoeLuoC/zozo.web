package com.example.zozo.web.repository;

import com.example.zozo.web.model.StockHolding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// Table - stock_holding
//  user_id - Not Null
//  stock_symbol - Not Null
//  quantity - Nullable Default 0
//  update_time - Nullable Default now()

public interface StockHoldingRepository extends JpaRepository<StockHolding, Long> {
    @Query(value = "SELECT * FROM stock_holding WHERE user_id = :userId", nativeQuery = true)
    Optional<List<StockHolding>> findByStocksByUserId(long userId);//？？

    @Query(value = "SELECT * FROM stock_holding WHERE user_id = :userId and stock_symbol = :symbol", nativeQuery = true)
    Optional<StockHolding> findByStockSymbol(long userId, String symbol);

    @Modifying
    @Transactional
    @Query(value = "UPDATE stock_holding SET quantity = :quantity, update_time = :updateTime WHERE user_id = :userId AND stock_symbol = :symbol AND update_time = :orgUpdateTime", nativeQuery = true)
    int updateStockHold(long userId, int quantity, String symbol, String orgUpdateTime, String updateTime);
}
