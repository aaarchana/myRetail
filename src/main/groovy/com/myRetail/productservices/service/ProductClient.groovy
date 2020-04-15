package com.myRetail.productservices.service

import com.myRetail.productservices.domain.ProductDetails
import com.myRetail.productservices.spring.PerformanceLoggingServletFilter
import com.myRetail.productservices.spring.ProductNotFoundException
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback
import org.springframework.web.client.AsyncRestTemplate

import javax.inject.Named

@Component
@Slf4j
class ProductClient {

    @Autowired
    @Named("productAsyncRestTemplate")
    AsyncRestTemplate productAsyncRestTemplate

    ListenableFuture<ResponseEntity<ProductDetails>> getProductDetailsAsync(Integer id) {

        String productEndpointUrl="https://redsky.target.com/v2/pdp/tcin/"+id+"?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics"

        HttpHeaders requestHeaders = new HttpHeaders()


        log.info("calling Product API for skuId: $id")

        PerformanceLoggingServletFilter.PerfTimer timer = PerformanceLoggingServletFilter.fetchTimer()
        ListenableFuture<ResponseEntity<ProductDetails>>  future = productAsyncRestTemplate.exchange(productEndpointUrl, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), ProductDetails)
       timer.startProductApiCall()
        future.addCallback(new ListenableFutureCallback<ResponseEntity<ProductDetails>>() {
            @Override
            void onFailure(Throwable ProductNotFoundException) {
                timer.stopProuctApiCall()

            }

            @Override
            void onSuccess(ResponseEntity<ProductDetails> result) {
                timer.stopProuctApiCall()
            }
        })
        return future
    }



}
