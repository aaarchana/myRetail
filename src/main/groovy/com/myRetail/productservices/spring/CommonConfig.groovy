package com.myRetail.productservices.spring

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.jmx.export.MBeanExporter
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

import javax.annotation.PostConstruct
import javax.servlet.ServletContext

@Slf4j
@Configuration
@ComponentScan(basePackages = [
        'com.myRetail.productservices'
])
@SuppressWarnings("GrMethodMayBeStatic")
class CommonConfig {

    @Value('${application.name}')
    String applicationName

    @Value('${application.version}')
    String applicationVersion

    @Bean
    static ConfigService configService(ServletContext servletContext) {
        List<PropertiesConfigSource> configSources = []

        /**
         * Any -D jvm arguments
         */
        configSources << new PropertiesConfigSource(name: 'System Properties', values: System.getProperties())

        /**
         * Environment configuration
         */
        String environment = EnvironmentUtil.getEnvironment()
        String environmentResource = "/config/environments/${environment}.properties"
        URL environmentUrl = CommonConfig.getResource(environmentResource)
        if (environmentUrl) {
            configSources << new URLConfigSource(name: 'Environment', url: environmentUrl)
        } else {
            log.warn "environment config resource not found, resource=${environmentResource}"
        }

        /**
         * Load application defaults
         */
        configSources << new URLConfigSource(name: 'Default', url: ConfigService.getResource('/config/default.properties'))

        //this allows us to use the context path from the servlet context as property value in our app.  And this helps us fix an issue with swagger behind a reverse proxy
        configSources << new PropertiesConfigSource(name:"servletContext", values: ["servletContext.contextPath":servletContext.getContextPath()])

        // jack in the configuration sources and Decryption property source
        ConfigService configService = new ConfigService(
                configSources: configSources)

        return configService
    }
    @Bean
    static PropertySourcesPlaceholderConfigurer properties(ServletContext servletContext) {
        PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        ppc.setPropertySources(configService(servletContext))
        return ppc;
    }

    @PostConstruct
    void emitStartupMessage() {
        //SLF4JBridgeHandler.removeHandlersForRootLogger();  // (since SLF4J 1.6.5)
        //SLF4JBridgeHandler.install();
        log.info "Configuration complete for ${applicationName} version ${applicationVersion}"
    }

    // This is required in order to get spring to recognize the @ManagedResource annotation for auto-exporting beans to JMX
    @Bean
    MBeanExporter getExporter() {
        MBeanExporter exporter = new org.springframework.jmx.export.MBeanExporter()
        AnnotationJmxAttributeSource attributeSource = new org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource()
        MetadataMBeanInfoAssembler assembler = new org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler(attributeSource)
        exporter.setAssembler(assembler)
        exporter.setNamingStrategy(new org.springframework.jmx.export.naming.MetadataNamingStrategy(attributeSource))
        exporter.setAutodetect(true)
        exporter
    }

    @Bean ThreadPoolTaskScheduler taskScheduler(){
        new ThreadPoolTaskScheduler(daemon:true, waitForTasksToCompleteOnShutdown: true, poolSize: 1 )
    }

}
