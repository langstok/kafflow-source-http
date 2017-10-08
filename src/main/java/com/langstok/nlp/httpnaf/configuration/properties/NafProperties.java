package com.langstok.nlp.httpnaf.configuration.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "naf")
public class NafProperties {

	/**
	 * format of naf creation date format yyyy-MM-dd'T'HH:mm:ss
	 */
	private String creationDateFormat = "yyyy-MM-dd'T'HH:mm:ss";


	/**
	 * Mapping between elasticsearch document and KAFDocument, for KAFDocument creation
	 */
	private Map<String, String> mapping = new HashMap<>();


	public String getCreationDateFormat() {
		return creationDateFormat;
	}

	public void setCreationDateFormat(String creationDateFormat) {
		this.creationDateFormat = creationDateFormat;
	}

	public Map<String, String> getMapping() {
		return mapping;
	}

	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}

	@Override
	public String toString() {
		return "NafProperties{" +
				"creationDateFormat='" + creationDateFormat + '\'' +
				", mapping=" + mapping.toString() +
				'}';
	}
}
