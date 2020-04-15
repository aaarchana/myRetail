package com.myRetail.productservices.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.myRetail.productservices.domain.ProductCurrentPrice
import com.myRetail.productservices.domain.ProductDetailsResponse
import com.myRetail.productservices.service.ProductDetailsService
import com.myRetail.productservices.spring.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class ProductServiceResourceSpec extends Specification {


    ProductDetailsService productDetailsService = Mock()

    ProductServiceResource productServiceResource = new ProductServiceResource(objectMapper: new ObjectMapper(), productDetailsService: productDetailsService)

    def 'when we request for a sku and we get back the response with product name and price'(){

        when:
        ResponseEntity actual= productServiceResource.getProductDetails(123)

        then:
        1 * productDetailsService.getProductDetails(123) >> new ProductDetailsResponse(id: 123, name: "test", current_price: new ProductCurrentPrice(value: 99.99, currency_code: "USD"))
        actual.statusCode == HttpStatus.OK


    }

    def 'when we request for a sku and we get product not found'(){

        when:
        ResponseEntity actual= productServiceResource.getProductDetails(123)

        then:
        1 * productDetailsService.getProductDetails(123) >> { throw new ProductNotFoundException()}
        actual.statusCode == HttpStatus.NOT_FOUND
        productServiceResource.objectMapper.readValue(actual.body.toString(),ProductDetailsResponse) == new ProductDetailsResponse(id: 123, message: "Product not found in redsky")
    }


    def 'when we request for a sku and we get price not found'(){

        when:
        ResponseEntity actual= productServiceResource.getProductDetails(123)

        then:
        1 * productDetailsService.getProductDetails(123) >> new ProductDetailsResponse(id: 123, name: "test", current_price: new ProductCurrentPrice())
        actual.statusCode == HttpStatus.NOT_FOUND
        productServiceResource.objectMapper.readValue(actual.body.toString(),ProductDetailsResponse) == new ProductDetailsResponse(id: 123, message: "Product does not have a price")
    }


}
