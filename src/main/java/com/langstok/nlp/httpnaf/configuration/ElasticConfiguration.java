package com.langstok.nlp.httpnaf.configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("elasticSearchEnabled")
@EnableConfigurationProperties(ModuleProperties.class)
public class ElasticConfiguration {

	@Autowired
	ModuleProperties moduleProperties;

	@Bean
	public Client client() throws UnknownHostException{
		return TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(
						InetAddress.getByName(
								moduleProperties.getElasticSearchHost()), 
						moduleProperties.getElasticSearchPort()));
	}




}
