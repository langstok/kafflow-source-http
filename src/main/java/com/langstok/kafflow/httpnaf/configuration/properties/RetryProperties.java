package com.langstok.kafflow.httpnaf.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "retry")
public class RetryProperties {

    private long setBackOffPeriod = 2000;

    private int maxAttempts = 15;

    public long getSetBackOffPeriod() {
        return setBackOffPeriod;
    }

    public void setSetBackOffPeriod(long setBackOffPeriod) {
        this.setBackOffPeriod = setBackOffPeriod;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
}
