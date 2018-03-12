package com.langstok.kafflow.httpnaf.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="elasticsearch")
public class ElasticProperties {

    /**
     * ElasticSearch host (default localhost)
     */
    private String host;

    /**
     * ElasticSearch port (default 9300)
     */
    private int port = 9300;

    /**
     * ElasticSearch enabled (default true)
     */
    private boolean enabled = true;


    /**
     * elasticSearchIndex default "annotated"
     */
    private String index = "annotated";

    /**
     * Use index language suffix (e.g. articles-en)
     */
    private boolean indexLanguageSuffix = true;

    /**
     * Document type in index
     */
    private String type = "kaf";


    /**
     * Field in ElasticSearch doc to store dateAnnotated, if null field not stored
     */
    private String fieldDateAnnotated = "dateAnnotated";

    /**
     * Field in ElasticSearch doc to store dateAnnotated, if null field not stored
     */
    private String fieldNaf = "naf";



    /**
     * ElasticSearch cluster_name (default elasticsearch)
     */
    private String clusterName;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public boolean isIndexLanguageSuffix() {
        return indexLanguageSuffix;
    }

    public void setIndexLanguageSuffix(boolean indexLanguageSuffix) {
        this.indexLanguageSuffix = indexLanguageSuffix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFieldDateAnnotated() {
        return fieldDateAnnotated;
    }

    public void setFieldDateAnnotated(String fieldDateAnnotated) {
        this.fieldDateAnnotated = fieldDateAnnotated;
    }

    public String getFieldNaf() {
        return fieldNaf;
    }

    public void setFieldNaf(String fieldNaf) {
        this.fieldNaf = fieldNaf;
    }

    @Override
    public String toString() {
        return "ElasticProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", enabled=" + enabled +
                ", clusterName='" + clusterName + '\'' +
                '}';
    }
}
