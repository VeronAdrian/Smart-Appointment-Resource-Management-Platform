package com.smartplatform.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Elasticsearch Configuration
 * Configures Spring Data Elasticsearch with the Elasticsearch server
 */
@Configuration
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.host:localhost}")
    private String host;

    @Value("${elasticsearch.port:9200}")
    private int port;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .withConnectTimeout(5000)
                .withSocketTimeout(60000)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}
