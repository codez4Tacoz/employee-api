package com.example.rqchallenge.employees;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static java.util.concurrent.TimeUnit.SECONDS;

import feign.Retryer;

@Configuration
public class FeignConfiguration {
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(SECONDS.toMillis(20), SECONDS.toMillis(60), 3);
    }
}
