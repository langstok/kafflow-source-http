package com.langstok.nlp.httpnaf.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="httpsource")
public class ModuleProperties {
	
	private String kafCreationDateFormat = "yyyy-MM-dd'T'HH:mm:ss";

	public String getKafCreationDateFormat() {
		return kafCreationDateFormat;
	}

	public void setKafCreationDateFormat(String kafCreationDateFormat) {
		this.kafCreationDateFormat = kafCreationDateFormat;
	}

}
