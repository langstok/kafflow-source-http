package com.langstok.kafflow.sourcehttp.configuration;

import com.langstok.kafflow.sourcehttp.configuration.properties.RetryProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableConfigurationProperties({RetryProperties.class})
public class RetryConfiguration {

    private RetryProperties retryProperties;

    public RetryConfiguration(RetryProperties retryProperties) {
        this.retryProperties = retryProperties;
    }

    @Bean
    public RetryTemplate configuredRetry() {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(retryProperties.getSetBackOffPeriod());
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(retryProperties.getMaxAttempts());
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;

    }
}