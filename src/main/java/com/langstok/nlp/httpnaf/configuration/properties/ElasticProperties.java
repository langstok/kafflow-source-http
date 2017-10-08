package com.langstok.nlp.httpnaf.configuration.properties;

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
