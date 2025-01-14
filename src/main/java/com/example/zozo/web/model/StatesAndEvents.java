package com.example.zozo.web.model;

public class StatesAndEvents {
    public enum GetStockPriceStates {
        INITIATED,
        PROCESSING,
        COMPLETED,
        FAILED
    }

    public enum GetStockPriceEvents {
        START_PROCESS,
        SUCCESS,
        FAILURE
    }
}
