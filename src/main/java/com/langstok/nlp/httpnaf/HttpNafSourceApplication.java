package com.langstok.nlp.httpnaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class HttpNafSourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpNafSourceApplication.class, args);
	}
}
