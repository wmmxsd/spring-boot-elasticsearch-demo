package com.wmm.elasticsearch.config;

import com.wmm.elasticsearch.config.impl.YamlPropertySourceFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 * es配置类
 * 使用{@link ConfigurationProperties}后不需要每个参数都是用{@link Value}来注释
 * @author wangmingming160328
 * @date @2020/6/22 11:56
 */
@Configuration
@PropertySource(value = "classpath:es.yml", factory = YamlPropertySourceFactory.class)

@ConfigurationProperties(prefix = "elasticsearch")
@Data
@NoArgsConstructor
public class EsConfig {
    @NonNull
    private String hostName;
    @NonNull
    private Integer port;

    /**
     * lowLevelRestConfig
     * @return RestClient
     */
    @Bean
    public RestClient restClient() {
        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(hostName, port));
        Header[] defaultHeaders = {new BasicHeader("content-type", "application/json")};
        restClientBuilder.setDefaultHeaders(defaultHeaders);
        return restClientBuilder.build();
    }

    /**
     * highLevelRestConfig
     * @return RestHighLevelClient
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(hostName, port)));
    }
}
