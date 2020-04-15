package com.myRetail.productservices.domain

import com.fasterxml.jackson.annotation.JsonAnySetter
import groovy.transform.Canonical
import groovy.transform.ToString

@Canonical
@ToString(includeNames = true)
class ProductDetails {

    Product product

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {}
}


@Canonical
@ToString(includeNames = true)
class Product {

    Item item

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {}
}

@Canonical
@ToString(includeNames = true)
class Item {

    ProductDescription product_description

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {}
}

@Canonical
@ToString(includeNames = true)
class ProductDescription {

    String title

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {}
}
