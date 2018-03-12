package com.langstok.nafflow.httpnaf.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RetryConfiguration {


    @Bean
    public RetryTemplate configuredRetry() {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(2000l);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);


//        Map<Class<? extends Throwable>, Boolean> exceptions = new HashMap<>();
//        exceptions.put(Exception.class, Boolean.FALSE);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(20);
        //retryPolicy.setMaxAttempts(20);

        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;

    }
}