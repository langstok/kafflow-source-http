package com.langstok.nlp.httpnaf.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "document")
public class DocumentProperties {

    /**
     * elasticSearchIndex default "articles"
     */
    private String index;

    /**
     * Use index language suffix (e.g. articles-en)
     */
    private boolean indexLanguageSuffix = true;

    /**
     * Document type in index
     */
    private String type;


    /**
     * Field in ElasticSearch doc to store naf (binary)
     */
    private String fieldNaf = "kaf";

    /**
     * Field in ElasticSearch doc to store dateAnnotated, if null field not stored
     */
    private String fieldDateAnnotated = "dateAnnotated";



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

    public String getFieldNaf() {
        return fieldNaf;
    }

    public void setFieldNaf(String fieldNaf) {
        this.fieldNaf = fieldNaf;
    }

    public String getFieldDateAnnotated() {
        return fieldDateAnnotated;
    }

    public void setFieldDateAnnotated(String fieldDateAnnotated) {
        this.fieldDateAnnotated = fieldDateAnnotated;
    }
}
