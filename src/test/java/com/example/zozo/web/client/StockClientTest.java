package com.example.zozo.web.client;


import feign.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;


@SpringBootTest
public class StockClientTest {

    @FeignClient(name = "stockClient", url = "http://localhost:8080", configuration = FeignClientConfig.class)
    public interface StockClient {
        @GetMapping("/stock/{symbol}")
        double getStockPrice(@PathVariable  String symbol);
    }

    @Configuration
    public class FeignClientConfig {

        @Bean
        public Request.Options requestOptions() {
            return new Request.Options(1000, 3000); // Connection timeout and read timeout in milliseconds
        }
    }

    @Autowired
    private StockClient stockClient;

    private MockWebServer mockWebServer;

    @Test
    public void testFeignClientReadTimeout() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8080);
        try {
            mockWebServer.enqueue(new MockResponse().setBodyDelay(10, TimeUnit.SECONDS));
            stockClient.getStockPrice("AAPL");
        } finally {
            mockWebServer.shutdown();
        }

    }

    @Test
    public void testFeignClientConnectTimeout() throws Exception {

    }
}