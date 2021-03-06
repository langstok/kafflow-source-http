package com.langstok.kafflow.sourcehttp.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ConfigurationProperties(prefix = "naf")
public class NafProperties {

	/**
	 * Default creation date format yyyy-MM-dd'T'HH:mm:ss
	 */
	private String creationDateFormat = "yyyy-MM-dd'T'HH:mm:ss";

	/**
	 * Supported Languages, default "en"
	 */
	private Set<String> supportedLanguages = new HashSet<>(Arrays.asList("en"));

	/**
	 * Field in ElasticSearch doc to store dateAnnotated, if null field not stored
	 */
	private String fieldNaf = "kaf";

	public String getFieldNaf() {
		return fieldNaf;
	}

	public void setFieldNaf(String fieldNaf) {
		this.fieldNaf = fieldNaf;
	}

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
