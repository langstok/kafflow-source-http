# LANGSTOK adaptation of the [newsreader](http://www.newsreader-project.eu/) NLP pipeline #

[news-reader](http://www.newsreader-project.eu/) modules adapted to a microservices-based distributed streaming pipeline. 

[Spring Cloud Data Flow](https://cloud.spring.io/spring-cloud-dataflow/) / [Stream Cloud Stream](http://docs.spring.io/spring-cloud-stream/docs/current-SNAPSHOT/reference/htmlsingle/) 


- Install [Kafka](https://kafka.apache.org/) & [Zookeeper](https://zookeeper.apache.org/)
- Install (for local usage) [spring-cloud-dataflow-server-local](https://github.com/spring-cloud/spring-cloud-dataflow/tree/master/spring-cloud-dataflow-server-local)

Navigate to the Spring Data Flow Local dashboard [http://localhost:9393/dashboard](http://localhost:9393/dashboard) (default)

## Clone and local install maven dependencies* ##

LANGSTOK dependencies

	git clone https://github.com/langstok/ims_maven.git
	git clone https://github.com/langstok/stanford-corenlp-naf-mapper.git
	git clone https://github.com/langstok/EventCoreference.git

install dependencies of ims_maven by following instructions

	mvn install -f ./ims_maven/pom.xml 
	mvn install -f ./stanford-corenlp-naf-mapper/pom.xml
	mvn install -f ./EventCoreference/pom.xml 


NEWSREADER LANGSTOK adapatations

    git clone https://bitbucket.org/langstok/source-http-naf
    git clone https://bitbucket.org/langstok/processor-langstok-stanford-corenlp.git
    git clone https://bitbucket.org/langstok/processor-ixa-pipe-parse.git
    git clone https://bitbucket.org/langstok/processor-ixa-pipe-ned.git
    git clone https://bitbucket.org/langstok/processor-ixa-pipe-wikify
    git clone https://bitbucket.org/langstok/processor-ixa-pipe-time
    git clone https://bitbucket.org/langstok/processor-ixa-pipe-srl
    git clone https://bitbucket.org/langstok/processor-langstok-wsd-ims
    git clone https://bitbucket.org/langstok/processor-langstok-naf-exec
    git clone https://bitbucket.org/langstok/processor-ixa-pipe-topic
	git clone https://bitbucket.org/langstok/processor-vua-eventcoreference.git
    git clone https://bitbucket.org/langstok/sink-naf-http

    mvn install -f ./source-http-naf/pom.xml
	mvn install -f ./processor-langstok-stanford-corenlp/pom.xml
    mvn install -f ./processor-ixa-pipe-parse/pom.xml
    mvn install -f ./processor-ixa-pipe-ned/pom.xml
    mvn install -f ./processor-ixa-pipe-wikify/pom.xml
    mvn install -f ./processor-ixa-pipe-time/pom.xml
    mvn install -f ./processor-ixa-pipe-srl/pom.xml
    mvn install -f ./processor-langstok-wsd-ims/pom.xml
    mvn install -f ./processor-langstok-naf-exec/pom.xml
	mvn install -f ./processor-ixa-pipe-topic/pom.xml
	mvn install -f ./processor-vua-eventcoreference/pom.xml
    mvn install -f ./sink-naf-http/pom.xml


## Bulk Import Applications ##

In Spring Data Flow local server

    source.http-naf=maven://com.langstok.nlp:source-http-naf:0.0.1-SNAPSHOT
    processor.langstok-stanford-corenlp=maven://com.langstok.nlp:processor-langstok-stanford-corenlp:0.0.1-SNAPSHOT
	processor.ixa-pipe-parse=maven://com.langstok.nlp:processor-ixa-pipe-parse:0.0.1-SNAPSHOT
    processor.ixa-pipe-ned=maven://com.langstok.nlp:processor-ixa-pipe-ned:0.0.1-SNAPSHOT
	processor.ixa-pipe-wikify=maven://com.langstok.nlp:processor-ixa-pipe-wikify:0.0.1-SNAPSHOT
    processor.ixa-pipe-time=maven://com.langstok.nlp:processor-ixa-pipe-time:0.0.1-SNAPSHOT
    processor.ixa-pipe-srl=maven://com.langstok.nlp:processor-ixa-pipe-srl:0.0.1-SNAPSHOT
    processor.langstok-wsd-ims=maven://com.langstok.nlp:processor-langstok-wsd-ims:0.0.1-SNAPSHOT
    processor.ixa-pipe-exec=maven://com.langstok.nlp:processor-langstok-naf-exec:0.0.1-SNAPSHOT
	processor.ixa-pipe-topic=maven://com.langstok.nlp:processor-ixa-pipe-topic:0.0.1-SNAPSHOT
    processor.vua-eventcoreference=maven://com.langstok.nlp:processor-vua-eventcoreference:0.0.1-SNAPSHOT
    sink.naf-http=maven://com.langstok.nlp:sink-naf-http:0.0.1-SNAPSHOT

## Stream examples ##

Create Stream (IXA tok,pos,nerc,ned)

	http-naf --elastic-search-host=192.168.178.33 --elasticSearchEnabled=true --elasticSearchCluster_name='elasticsearch_sanderputs' --elastic-search-type=article --elastic-search-index=articles --vcap.services.eureka-service.credentials.uri='http://192.168.178.33:8761' --elastic-search-cluster-name=elasticsearch_sanderputs | ixa-pipe-tok | ixa-pipe-pos --models='/cfn/models/morph-models-1.5.0/en/en-pos-maxent-100-c5-baseline-autodict01-conll09.bin' --lemmatizermodels='/cfn/models/morph-models-1.5.0/en/en-lemma-perceptron-conll09.bin' --languages='en' | ixa-pipe-nerc --language=en --model='/cfn/models/nerc-models-1.5.4/en/combined/en-91-18-3-class-muc7-conll03-ontonotes-4.0.bin' --dict-path=/cfn/dict --dict-tag=post | ixa-pipe-ned | naf-http --elastic-search-host=192.168.178.33 --elastic-search-type=article --elastic-search-index=articles --elastic-search-cluster-name=elasticsearch_sanderputs

Create Stream (langstok English)

    http-naf | langstok-stanford-corenlp | ixa-pipe-parse | ixa-pipe-ned | ixa-pipe-time | langstok-wsd-ims | ixa-pipe-srl | ixa-pipe-exec --logging.level.com.langstok=debug | ixa-pipe-topic | vua-eventcoreference --sem='false' | naf-http

## Models ##

[IXA-PIPE-POS](https://github.com/ixa-ehu/ixa-pipe-pos) 

[IXA-PIPE-NERC](https://github.com/ixa-ehu/ixa-pipe-nerc)

[DBPEDIA Spotlight](http://spotlight.sztaki.hu/downloads/latest_models)

[HEIDELTIME](https://github.com/HeidelTime/heideltime)

SRL

Opinion Miner