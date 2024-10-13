package com.example.zozo.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockPriceResponse {
    @JsonProperty("Global Quote")
    private GlobalQuote globalQuote;

    // Getter and Setter
    public GlobalQuote getGlobalQuote() { return globalQuote; }
    public void setGlobalQuote(GlobalQuote globalQuote) { this.globalQuote = globalQuote; }
}
