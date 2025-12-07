package com.smartplatform.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;

/* Elasticsearch Configuration Configures Spring Data Elasticsearch with the Elasticsearch server
 */
@Configuration
public class ElasticsearchConfig extends org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration {

    @Value("${elasticsearch.host:localhost}")
    private String host;

    @Value("${elasticsearch.port:9200}")
    private int port;

    @Override
    @Bean
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .withConnectTimeout(5000)
                .withSocketTimeout(60000)
                .build();
    }
}
