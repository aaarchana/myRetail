package com.myRetail.productservices.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.ToString


@ToString(includeNames = true)
@CompileStatic
class ProductPriceRequest {

    @JsonIgnore
    Integer id
    BigDecimal price
    String currency_code
}
