package com.langstok.kafflow.httpnaf.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ConfigurationProperties(prefix = "naf")
public class NafProperties {

	/**
	 * Default creation date format yyyy-MM-dd'T'HH:mm:ss
	 */
	private String creationDateFormat = "yyyy-MM-dd'T'HH:mm:ss";

	private Set<String> supportedLanguages = new HashSet<>(Arrays.asList("en"));

	public String getCreationDateFormat() {
		return creationDateFormat;
	}

	public void setCreationDateFormat(String creationDateFormat) {
		this.creationDateFormat = creationDateFormat;
	}

	public Set<String> getSupportedLanguages() {
		return supportedLanguages;
	}

	public void setSupportedLanguages(Set<String> supportedLanguages) {
		this.supportedLanguages = supportedLanguages;
	}
}
