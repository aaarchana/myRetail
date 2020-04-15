package com.myRetail.productservices.spring

class PropertiesConfigSource {

	String name

	Map values = [:]

	Collection<String> getKeys() {
		return values.keySet()
	}

	String getValue(String key) {
		return values.get(key)?.toString()?.trim()
	}

	void load() {
		// hook for subclasses
	}

	String toString() {
		return "${this.class.simpleName}[name:${name}]"
	}

    Map getAsMap() {
        return new HashMap(values)
    }
}
