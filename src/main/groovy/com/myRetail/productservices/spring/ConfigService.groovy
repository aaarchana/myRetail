package com.myRetail.productservices.spring

import groovy.util.logging.Slf4j
import org.springframework.core.env.PropertySource
import org.springframework.core.env.PropertySources

@Slf4j
class ConfigService implements PropertySources {

	List<PropertiesConfigSource> allConfigSources = []

	List<PropertySource> propertySources = [new ConfigServicePropertySource(this)]


	void setConfigSources(Collection<PropertiesConfigSource> configSources) {
		configSources.each { PropertiesConfigSource configSource ->
			log.info "Loading values from config source, source=${configSource}"
			configSource.load()
		}

		this.allConfigSources = configSources
	}

	protected String getValue(String name) {

		for (PropertiesConfigSource configSource : allConfigSources) {
			String value = configSource.getValue(name)
			if(value!=null){return value}
		}
		return null
	}



	@Override
	boolean contains(String name) {
		return true
	}

	@Override
	PropertySource get(String name) {
		propertySources[0]
	}

	@Override
	Iterator<PropertySource> iterator() {
		propertySources.iterator()
	}

	Properties getAllValues(){
		allConfigSources.reverse()
				.collect{ PropertiesConfigSource source -> source.values }
				.inject(new Properties()){Properties config, Map p->
			config.putAll(p)
			config
		}
	}

	Properties getMatchingProperties(java.util.regex.Pattern pattern){
		Properties all = getAllValues()
		all.findAll { Map.Entry e-> pattern.matcher(e.key).matches()}
	}


	static class ConfigServicePropertySource extends PropertySource {
		ConfigService configService

		ConfigServicePropertySource(ConfigService configService) {
			super(ConfigServicePropertySource.simpleName)
			this.configService = configService
		}

		@Override
		Object getProperty(String name) {
			return configService.getValue(name)
		}
	}
}
