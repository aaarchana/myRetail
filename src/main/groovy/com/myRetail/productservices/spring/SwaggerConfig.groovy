package com.myRetail.productservices.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.paths.RelativePathProvider
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

import javax.servlet.ServletContext

@EnableSwagger2
@EnableWebMvc
class SwaggerConfig extends WebMvcConfigurerAdapter{


    @Autowired
    ServletContext servletContext

    @Value('${swagger.basePath}')
    String swaggerBasePath

    @Bean
    Docket api() {
        new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .ignoredParameterTypes(MetaClass, MetaMethod) //don't list these in the request and response models
                .useDefaultResponseMessages(false)
                .pathProvider(new RelativePathProvider(servletContext) {
                     @Override
                    public String getApplicationBasePath() {
                    return swaggerBasePath;
            }
        })
    }
    private static ApiInfo apiInfo() {
        new ApiInfo(
                'ProductPrice Services API',
                'ProductPrice Services',
                null,
                null,
                'Archana',
                null,
                null,

        )
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}