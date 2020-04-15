package com.myRetail.productservices.service

import com.myRetail.productservices.dao.ProductPricingDao
import com.myRetail.productservices.domain.*
import com.myRetail.productservices.spring.PerformanceLoggingServletFilter
import com.myRetail.productservices.spring.ProductNotFoundException
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

@Component
@Slf4j
class ProductDetailsService {

    @Autowired
    ProductPricingDao productPricingDao

    @Autowired
    ProductClient productClient


    @Cacheable(value = "productData", key = "#id", unless = "#result == null")
    ProductDetailsResponse getProductDetails(Integer id){

        Future<ResponseEntity<ProductDetails>> productDetailsFuture

        productDetailsFuture = productClient.getProductDetailsAsync(id)
        PerformanceLoggingServletFilter.PerfTimer timer = PerformanceLoggingServletFilter.fetchTimer()
        timer.startCassandraCall()
        ProductPrice product= productPricingDao.getProductByPrimaryKey(id)
        timer.stopCassandraCall()
        ProductDetails productDetails

            productDetails = productDetailsFuture?.get(10000, TimeUnit.MILLISECONDS)?.body

        return new ProductDetailsResponse(id: id, name: productDetails?.product?.item?.product_description?.title, current_price: new ProductCurrentPrice(value: product?.price, currency_code: product?.currency_code))
    }


    void saveProductPrice(ProductPriceRequest productPriceRequest){
        ProductPrice productPrice = new ProductPrice(id: productPriceRequest.id, price: productPriceRequest.price, currency_code: productPriceRequest.currency_code)
        productPricingDao.saveProductPrice(productPrice)
    }

}
