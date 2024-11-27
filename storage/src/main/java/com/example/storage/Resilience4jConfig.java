package com.example.storage;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4jConfig {

    @Bean
    public Retry retryConfig() {
        return Retry.of(
                "retry",
                RetryConfig.custom()
                        .maxAttempts(5)
                        .waitDuration(Duration.ofMillis(500))
                        .build()
        );
    }
}
