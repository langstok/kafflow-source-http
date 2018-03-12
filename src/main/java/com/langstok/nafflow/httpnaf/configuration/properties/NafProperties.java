package com.langstok.nafflow.httpnaf.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "naf")
public class NafProperties {

	/**
	 * format of naf creation date format yyyy-MM-dd'T'HH:mm:ss
	 */
	private String creationDateFormat = "yyyy-MM-dd'T'HH:mm:ss";

	public String getCreationDateFormat() {
		return creationDateFormat;
	}

	public void setCreationDateFormat(String creationDateFormat) {
		this.creationDateFormat = creationDateFormat;
	}
}
