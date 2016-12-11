package com.langstok.nlp.httpnaf.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class ModuleProperties {
	
	private String kafCreationDateFormat = "yyyy-MM-dd'T'HH:mm:ss";
	
	/**
	 * elasticSearchIndex default "articles"
	 */
	private String elasticSearchIndex;
	
	/**
	 * Enable of disable language index postfix (e.g. articles-en)
	 */
	private boolean elasticSearchIndexLangPostFix = true;

	/**
	 * Document type in index
	 */
	private String elasticSearchType;
	
	/**
	 * ElasticSearch host (default localhost)
	 */
	private String elasticSearchHost = "spmm";
	
	/**
	 * ElasticSearch port (default 9300)
	 */
	private int elasticSearchPort = 9300;
	
	/**
	 * ElasticSearch enabled (default true)
	 */
	private boolean elasticSearchEnabled = true;
	
	/**
	 * ElasticSearch cluster_name (default elasticsearch)
	 */
	private String elasticSearchCluster_name = "elasticsearch";
	
	private Map<String,String> elasticSearchNafMapping = new HashMap<>();

	public String getKafCreationDateFormat() {
		return kafCreationDateFormat;
	}

	public void setKafCreationDateFormat(String kafCreationDateFormat) {
		this.kafCreationDateFormat = kafCreationDateFormat;
	}

	public String getElasticSearchIndex() {
		return elasticSearchIndex;
	}

	public void setElasticSearchIndex(String elasticSearchIndex) {
		this.elasticSearchIndex = elasticSearchIndex;
	}

	public boolean isElasticSearchIndexLangPostFix() {
		return elasticSearchIndexLangPostFix;
	}

	public void setElasticSearchIndexLangPostFix(boolean elasticSearchIndexLangPostFix) {
		this.elasticSearchIndexLangPostFix = elasticSearchIndexLangPostFix;
	}

	public String getElasticSearchType() {
		return elasticSearchType;
	}

	public void setElasticSearchType(String elasticSearchType) {
		this.elasticSearchType = elasticSearchType;
	}

	public String getElasticSearchHost() {
		return elasticSearchHost;
	}

	public void setElasticSearchHost(String elasticSearchHost) {
		this.elasticSearchHost = elasticSearchHost;
	}

	public int getElasticSearchPort() {
		return elasticSearchPort;
	}

	public void setElasticSearchPort(int elasticSearchPort) {
		this.elasticSearchPort = elasticSearchPort;
	}

	public boolean isElasticSearchEnabled() {
		return elasticSearchEnabled;
	}

	public void setElasticSearchEnabled(boolean elasticSearchEnabled) {
		this.elasticSearchEnabled = elasticSearchEnabled;
	}

	public Map<String, String> getElasticSearchNafMapping() {
		return elasticSearchNafMapping;
	}

	public void setElasticSearchNafMapping(Map<String, String> elasticSearchNafMapping) {
		this.elasticSearchNafMapping = elasticSearchNafMapping;
	}

	public String getElasticSearchCluster_name() {
		return elasticSearchCluster_name;
	}

	public void setElasticSearchCluster_name(String elasticSearchCluster_name) {
		this.elasticSearchCluster_name = elasticSearchCluster_name;
	}
	
}
