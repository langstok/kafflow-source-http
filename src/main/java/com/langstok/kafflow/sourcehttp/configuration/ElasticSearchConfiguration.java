package com.langstok.kafflow.sourcehttp.configuration;

import com.langstok.kafflow.sourcehttp.configuration.properties.ElasticProperties;
import com.langstok.kafflow.sourcehttp.repository.DocumentRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableConfigurationProperties(ElasticProperties.class)
public class ElasticSearchConfiguration {

    private Log log = LogFactory.getLog(ElasticSearchConfiguration.class);

    private final ElasticProperties elasticProperties;

    public ElasticSearchConfiguration(ElasticProperties elasticProperties){
        this.elasticProperties = elasticProperties;
    }

    @Bean
    public Client client() throws UnknownHostException {
        if(elasticProperties.isEnabled()){
            log.info("Init elasticsearch: " + elasticProperties );
            Settings settings = Settings.builder()
                    .put("cluster.name", elasticProperties.getClusterName()).build();

            TransportClient client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(
                            elasticProperties.getHost()),
                            elasticProperties.getPort()));
            return client;
        }
        else{
            log.info("ElasticSearch disabled");
            return null;
        }
    }
}