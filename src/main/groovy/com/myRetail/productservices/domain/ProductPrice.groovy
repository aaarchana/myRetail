package com.myRetail.productservices.domain

import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import groovy.transform.ToString
import groovy.util.logging.Slf4j

@ToString(includeNames = true)
@Slf4j
@Table(name = "myretail_product")
class ProductPrice {

    @PartitionKey
    @Column(name = "ID")
    Integer id

    @Column(name="PRICE")
    BigDecimal price

    @Column(name="CURRENCY_CODE")
    String currency_code

    ProductPrice toProduct(){
        new ProductPrice(id: id, price: price, currency_code: currency_code)


    }
}
