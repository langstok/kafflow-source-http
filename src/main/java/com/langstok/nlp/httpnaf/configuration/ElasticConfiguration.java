package com.langstok.nlp.httpnaf.configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ModuleProperties.class)
public class ElasticConfiguration {

	private final static Logger LOGGER = Logger.getLogger(ElasticConfiguration.class);

	@Autowired
	ModuleProperties moduleProperties;

	@Bean
	public Client client() throws UnknownHostException{
		if(moduleProperties.isElasticSearchEnabled()){
			LOGGER.info("elasticSearchEnabled, setup client for "
					+ " elasticSearchHost:"+ moduleProperties.getElasticSearchHost() 
					+ " elasticSearchPort:"+ moduleProperties.getElasticSearchPort()  );

			return TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(
							InetAddress.getByName(
									moduleProperties.getElasticSearchHost()), 
							moduleProperties.getElasticSearchPort()));
		}
		else{
			LOGGER.info("elasticSearchEnabled: false");
			return null;
		}
	}
}
