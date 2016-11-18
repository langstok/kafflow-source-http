# LANGSTOK adapatation of the [newsreader](http://www.newsreader-project.eu/) NLP pipeline #

[news-reader](http://www.newsreader-project.eu/) modules adapted to a microservices-based distributed streaming pipeline. 

[Spring Cloud Data Flow](https://cloud.spring.io/spring-cloud-dataflow/) / [Stream Cloud Stream](http://docs.spring.io/spring-cloud-stream/docs/current-SNAPSHOT/reference/htmlsingle/) 


- Install [Kafka](https://kafka.apache.org/) & [Zookeeper](https://zookeeper.apache.org/)
- Install (for local usage) [spring-cloud-dataflow-server-local](https://github.com/spring-cloud/spring-cloud-dataflow/tree/master/spring-cloud-dataflow-server-local)

Navigate to the Spring Data Flow Local dashboard [http://localhost:9393/dashboard](http://localhost:9393/dashboard) (default)

## Clone and install dependencies  (not available in Maven Central) ##

DBPEDIA spotlight (only for NED/Wikify modules)

	https://github.com/dbpedia-spotlight/dbpedia-spotlight

	http://spotlight.sztaki.hu/downloads/dbpedia-spotlight-latest.jar

	(wikify module needs dbpedia as dependency)
	mvn install:install-file -Dfile=dbpedia-spotlight-latest.jar -DgroupId=ixa -DartifactId=dbpedia-spotlight -Dversion=0.7 -Dpackaging=jar -DgeneratePom=true


Clone news-reader dependencies  

    git clone https://github.com/ixa-ehu/ixa-pipe-wikify.git


Clone news-reader modules (LANGSTOK adapatation)

    git clone https://github.com/langstok/source-http-naf
    git clone https://github.com/langstok/processor-ixa-pipe-tok
    git clone https://github.com/langstok/processor-ixa-pipe-pos
    git clone https://github.com/langstok/processor-ixa-pipe-nerc
    git clone https://github.com/langstok/processor-ixa-pipe-ned
    git clone https://github.com/langstok/processor-ixa-pipe-time
    git clone https://github.com/langstok/processor-ixa-pipe-srl
    git clone https://github.com/langstok/processor-langstok-naf-exec
    git clone https://github.com/langstok/processor-ixa-pipe-parse
    git clone https://github.com/langstok/processor-ixa-pipe-wikify
    git clone https://github.com/langstok/processor-ixa-pipe-topic
    git clone https://github.com/langstok/sink-naf-http

Local install news-reader dependencies

	mvn install -f ./ixa-pipe-wikify/pom.xml


Local install applications (for correct users, not necessary after maven central upload)

    mvn install -f ./source-http-naf/pom.xml
    mvn install -f ./processor-ixa-pipe-tok/pom.xml
    mvn install -f ./processor-ixa-pipe-nerc/pom.xml
    mvn install -f ./processor-ixa-pipe-ned/pom.xml
    mvn install -f ./processor-ixa-pipe-time/pom.xml
    mvn install -f ./processor-ixa-pipe-srl/pom.xml
    mvn install -f ./processor-langstok-naf-exec/pom.xml
    mvn install -f ./processor-ixa-pipe-parse/pom.xml
    mvn install -f ./processor-ixa-pipe-wikify/pom.xml
    mvn install -f ./sink-naf-http/pom.xml
    mvn install -f ./processor-ixa-pipe-topic


## Bulk Import Applications ##

In Spring Data Flow local server

    source.http-naf=maven://com.langstok.nlp:source-http-naf:0.0.1-SNAPSHOT
    processor.ixa-pipe-tok=maven://com.langstok.nlp:processor-ixa-pipe-tok:0.0.1-SNAPSHOT
    processor.ixa-pipe-pos=maven://com.langstok.nlp:processor-ixa-pipe-pos:0.0.1-SNAPSHOT
    processor.ixa-pipe-nerc=maven://com.langstok.nlp:processor-ixa-pipe-nerc:0.0.1-SNAPSHOT
    processor.ixa-pipe-ned=maven://com.langstok.nlp:processor-ixa-pipe-ned:0.0.1-SNAPSHOT
    processor.ixa-pipe-time=maven://com.langstok.nlp:processor-ixa-pipe-time:0.0.1-SNAPSHOT
    processor.ixa-pipe-srl=maven://com.langstok.nlp:processor-ixa-pipe-srl:0.0.1-SNAPSHOT
    processor.ixa-pipe-exec=maven://com.langstok.nlp:processor-langstok-naf-exec:0.0.1-SNAPSHOT
    processor.ixa-pipe-parse=maven://com.langstok.nlp:processor-ixa-pipe-parse:0.0.1-SNAPSHOT
    processor.ixa-pipe-wikify=maven://com.langstok.nlp:processor-ixa-pipe-wikify:0.0.1-SNAPSHOT
    sink.naf-http=maven://com.langstok.nlp:sink-naf-http:0.0.1-SNAPSHOT

## Stream examples ##

Create Stream (basic named entity recognition)

	http-naf | ixa-pipe-tok | ixa-pipe-pos --languages='en' --models='/cfn/models/morph-models-1.5.0/en/en-pos-maxent-100-c5-baseline-autodict01-conll09.bin' --lemmatizermodels='/cfn/models/morph-models-1.5.0/en/en-lemma-perceptron-conll09.bin' | ixa-pipe-nercnaf-http 

Create Stream (elasticsearch example)

		http-naf --elastic-search-host=192.168.178.33 --elasticSearchEnabled=true --elastic-search-type=article --elastic-search-index=articles --vcap.services.eureka-service.credentials.uri='http://192.168.178.33:8761' | naf-http --elastic-search-host=192.168.178.33 --elastic-search-type=article --elastic-search-index=articles

Create Stream (Python call from Java, opinion miner example)

    http-naf | ixa-pipe-exec --directory=/cfn/opinion_miner_deluxePP/opinion_miner_deluxePP --command='python tag_file.py -d news' | naf-http

Create Stream full (under development)

	http-naf --elastic-search-host=192.168.178.33 --elasticSearchEnabled=true --elastic-search-type=article --elastic-search-index=articles --vcap.services.eureka-service.credentials.uri='http://192.168.178.33:8761' | ixa-pipe-tok | ixa-pipe-pos --models='/cfn/models/morph-models-1.5.0/en/en-pos-maxent-100-c5-baseline-autodict01-conll09.bin' --lemmatizermodels='/cfn/models/morph-models-1.5.0/en/en-lemma-perceptron-conll09.bin' --languages='en' | ixa-pipe-nerc --language=en --model='/cfn/models/nerc-models-1.5.4/en/combined/en-91-18-3-class-muc7-conll03-ontonotes-4.0.bin' | ixa-pipe-ned | naf-http --elastic-search-host=192.168.178.33 --elastic-search-type=article --elastic-search-index=articles


## Models ##

[IXA-PIPE-POS](https://github.com/ixa-ehu/ixa-pipe-pos) 

[IXA-PIPE-NERC](https://github.com/ixa-ehu/ixa-pipe-nerc)

[DBPEDIA](http://spotlight.sztaki.hu/downloads/latest_models)

[HEIDELTIME](https://github.com/HeidelTime/heideltime)

SRL

Opinion Miner