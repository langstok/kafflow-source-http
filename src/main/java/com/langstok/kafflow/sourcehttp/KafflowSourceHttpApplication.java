package com.langstok.kafflow.sourcehttp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class KafflowSourceHttpApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafflowSourceHttpApplication.class, args);
	}
}
