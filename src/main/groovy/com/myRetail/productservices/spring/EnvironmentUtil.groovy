package com.myRetail.productservices.spring

class EnvironmentUtil {
    public static final String DEFAULT_ENVIRONMENT = 'local'
    public static final String ENVIRONMENT_PROPERTY = 'environment'
    public static final String APP_NAME = 'app.name'
    public static final List<String> CLASS_PATH_ENVIRONMENTS = [DEFAULT_ENVIRONMENT, 'dev'].asImmutable()

    static final String getEnvironment(){
        System.getProperty(ENVIRONMENT_PROPERTY, DEFAULT_ENVIRONMENT)
    }

}
