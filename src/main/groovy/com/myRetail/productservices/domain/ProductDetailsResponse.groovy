package com.myRetail.productservices.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.ToString

@Canonical
@ToString(includeNames = true)
@CompileStatic
class ProductDetailsResponse {

    Integer id
    String name
    ProductCurrentPrice current_price
}

@Canonical
@ToString(includeNames = true)
@CompileStatic
class ProductCurrentPrice {

    BigDecimal value
    String currency_code

}