package com.myRetail.productservices.spring

import groovy.util.logging.Slf4j
import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.request.RequestContextListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet

import javax.servlet.FilterRegistration
import javax.servlet.ServletContext
import javax.servlet.ServletRegistration

@Slf4j
class WebAppInit implements WebApplicationInitializer {

    @Override
    void onStartup(ServletContext servletContext) {

        log.info "on startup - product-services"

        // Create the 'root' Spring application context.
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext()
        rootContext.register(RootConfig)
        rootContext.registerShutdownHook()

        new ActivateProfile().initialize(rootContext)

        // Manage the lifecycle of the root application context.
        servletContext.addListener(new ContextLoaderListener(rootContext))

        // Register a default servlet to serve the api documentation
        ServletRegistration.Dynamic defaultServlet = servletContext.addServlet("default", "org.apache.catalina.servlets.DefaultServlet")
      //  defaultServlet.loadOnStartup = 1
        defaultServlet.addMapping("/product-services")

        defaultServlet.setInitParameter("listings", "false")

        // Register and map the dispatcher servlet.
        ServletRegistration.Dynamic springServlet = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext))
        springServlet.addMapping("/*")
        springServlet.loadOnStartup = 1
        servletContext.addListener(new RequestContextListener())

        FilterRegistration permMonFilterRegistration = servletContext.addFilter("perfMonitor", new PerformanceLoggingServletFilter())
        permMonFilterRegistration.addMappingForUrlPatterns(null, false, "/products/*")


    }


}
