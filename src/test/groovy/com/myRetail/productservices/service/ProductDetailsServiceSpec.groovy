package com.myRetail.productservices.service

import com.myRetail.productservices.dao.ProductPricingDao
import com.myRetail.productservices.domain.*
import org.springframework.http.ResponseEntity
import org.springframework.util.concurrent.ListenableFuture
import spock.lang.Specification

class ProductDetailsServiceSpec extends Specification {


    ProductPricingDao productPricingDao = Mock()
    ProductClient productClient = Mock()


    ProductDetailsService productDetailsService = new ProductDetailsService(productPricingDao: productPricingDao, productClient: productClient)

    def "verify when requested for product details, database and product API is called"(){

        ListenableFuture<ResponseEntity<ProductDetails>> future = Mock()
        ResponseEntity<ProductDetails> responseEntity = Mock()
        ProductDetailsResponse expected = new ProductDetailsResponse(id: 1234, name: "test Product", current_price: new ProductCurrentPrice(value: 89.99, currency_code: "USD"))

        when:
        ProductDetailsResponse actual = productDetailsService.getProductDetails(1234)

        then:
        1 * productClient.getProductDetailsAsync(1234) >> future
        1 * future.get(_,_) >> responseEntity
        responseEntity.body >> new ProductDetails(product: new Product(item: new Item(product_description: new ProductDescription(title: "test Product"))))
        1 * productPricingDao.getProductByPrimaryKey(1234) >> new ProductPrice(id: 1234, price: 89.99, currency_code: "USD")

        actual == expected
    }

}
