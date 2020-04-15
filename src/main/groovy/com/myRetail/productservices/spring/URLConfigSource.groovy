package com.myRetail.productservices.spring

import groovy.util.logging.Slf4j

@Slf4j
class URLConfigSource extends PropertiesConfigSource {

	URL url

	void load() {
		if (url != null) {
			try {
				Properties properties = new Properties()
				url.withInputStream { InputStream inputStream ->
					properties.load(inputStream)
				}
				values.putAll(properties)
			} catch (FileNotFoundException e) {
                log.warn "config file not found,file=${url}"
			}
		}
	}

	String toString() {
		return "${this.class.simpleName}[name:${name}, url:${url}]"
	}

}
