package com.myRetail.productservices.dao

import com.datastax.driver.core.Session
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import com.myRetail.productservices.domain.ProductPrice
import com.myRetail.productservices.spring.PerformanceLoggingServletFilter
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository

@Repository
@Slf4j
class ProductPricingDao {

    @Autowired
    Session session

    Mapper<ProductPrice> productMapper

    @Autowired(required = true)
    public ProductPricingDao(@Qualifier("session") Session session) {
        this.session = session
        productMapper =  new MappingManager(session).mapper(ProductPrice)

    }

    ProductPrice getProductByPrimaryKey(Integer id){
        log.info("Fetching Product price from DB for skuId: $id" )
        productMapper.get(id)?.toProduct()
    }


    void saveProductPrice(ProductPrice productPrice){
        productMapper.save(productPrice)
    }

}
