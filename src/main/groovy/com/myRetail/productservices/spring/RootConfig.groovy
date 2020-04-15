package com.myRetail.productservices.spring

import groovy.util.logging.Slf4j
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.apache.http.impl.nio.reactor.IOReactorConfig
import org.apache.http.nio.client.HttpAsyncClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory
import org.springframework.web.client.AsyncRestTemplate

import javax.inject.Named

@Slf4j
@Configuration()
@Import([AppConfig, CommonConfig, CassandraConfig, SwaggerConfig, CacheConfig])
class RootConfig {

    @Bean(name = "productAsyncRestClient")
    HttpAsyncClient productAsyncRestClient() {
        HttpAsyncClientBuilder builder = HttpAsyncClientBuilder.create()
        builder.maxConnPerRoute = 500
        builder.maxConnTotal = 500
        IOReactorConfig config = new IOReactorConfig.Builder().setIoThreadCount(500).build();
        builder.defaultIOReactorConfig = config
        builder.build()
    }

    @Bean(name = "productAsyncRestTemplate")
    AsyncRestTemplate productAsyncRestTemplate(@Named("productAsyncRestClient") HttpAsyncClient productAsyncRestClient) {
        HttpComponentsAsyncClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsAsyncClientHttpRequestFactory(productAsyncRestClient)
        setTimeoutValueForAsyncClient(clientHttpRequestFactory, 50000,50000)
        new AsyncRestTemplate(clientHttpRequestFactory)
    }

    static void setTimeoutValueForAsyncClient(HttpComponentsAsyncClientHttpRequestFactory httpComponentsAsyncClientHttpRequestFactory, Integer timeoutValue,Integer connectionRequestTimeoutValue) {
        httpComponentsAsyncClientHttpRequestFactory.connectTimeout = timeoutValue
        httpComponentsAsyncClientHttpRequestFactory.readTimeout = timeoutValue
        httpComponentsAsyncClientHttpRequestFactory.connectionRequestTimeout = connectionRequestTimeoutValue
    }

    @Bean
    ProductServicesThreadExecutor executorService() {
        return new ProductServicesThreadExecutor(1000)
    }


}

