package com.myRetail.productservices.spring

import groovy.util.logging.Slf4j
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.ConfigurableEnvironment

@Slf4j
class ActivateProfile implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public static final String SPRING_PROFILE_DEFAULT = "default"
    public static final String SPRING_PROFILE_LOCAL = "local"

    @Override
    void initialize(ConfigurableApplicationContext applicationContext) {
        String profile = EnvironmentUtil.getEnvironment()

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        //environment.setDefaultProfiles(SPRING_PROFILE_DEFAULT)

        if (profile == "local") {
            environment.setActiveProfiles(SPRING_PROFILE_LOCAL)
            log.info("Overriding default Spring profile, profile=${SPRING_PROFILE_LOCAL}");
        }

        log.info("active profiles,profiles=${environment.activeProfiles}")
    }

}


