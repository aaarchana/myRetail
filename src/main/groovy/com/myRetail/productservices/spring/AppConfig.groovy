package com.myRetail.productservices.spring

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import groovy.util.logging.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = [
        "com.myRetail.productservices"
])
@SuppressWarnings("GrMethodMayBeStatic")
@Slf4j
class AppConfig {

    @Bean
    XmlMapper xmlMapper() {
        new XmlMapper()
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper()
        //mapper.setSerializationInclusion(JsonInclude.Include.NULL)
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

        mapper
    }

   /* @Bean
    Jackson2JsonEncoder jackson2JsonEncoder(ObjectMapper objectMapper) {
        return new Jackson2JsonEncoder(objectMapper);
    }

    @Bean
    Jackson2JsonDecoder jackson2JsonDecoder(ObjectMapper objectMapper) {
        return new Jackson2JsonDecoder(objectMapper);
    }*/


}
