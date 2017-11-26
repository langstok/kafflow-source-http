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

   bulk import [bulkimportapp.properties](http://sanderputs.com/stream/bulkimportapp.properties)

## Stream examples ##

Create Stream (deprecated) (IXA tok,pos,nerc,ned)

	http-naf --elastic-search-host=192.168.178.33 --elasticSearchEnabled=true --elasticSearchCluster_name='elasticsearch_sanderputs' --elastic-search-type=article --elastic-search-index=articles --vcap.services.eureka-service.credentials.uri='http://192.168.178.33:8761' --elastic-search-cluster-name=elasticsearch_sanderputs | ixa-pipe-tok | ixa-pipe-pos --models='/cfn/models/morph-models-1.5.0/en/en-pos-maxent-100-c5-baseline-autodict01-conll09.bin' --lemmatizermodels='/cfn/models/morph-models-1.5.0/en/en-lemma-perceptron-conll09.bin' --languages='en' | ixa-pipe-nerc --language=en --model='/cfn/models/nerc-models-1.5.4/en/combined/en-91-18-3-class-muc7-conll03-ontonotes-4.0.bin' --dict-path=/cfn/dict --dict-tag=post | ixa-pipe-ned | naf-http --elastic-search-host=192.168.178.33 --elastic-search-type=article --elastic-search-index=articles --elastic-search-cluster-name=elasticsearch_sanderputs

Create Stream (langstok English local Dbpedia)

    http-naf --vcap.services.eureka-service.credentials.uri='http://spmm:8761' --host=spmm --cluster-name=elasticsearch_cfncfn | langstok-stanford-corenlp | ixa-pipe-parse | ixa-pipe-ned | ixa-pipe-wikify | ixa-pipe-time | langstok-wsd-ims | ixa-pipe-srl | ixa-pipe-exec | ixa-pipe-topic | vua-eventcoreference --knowledge-store=http://spmm:3030/newsreader | naf-http --vcap.services.eureka-service.credentials.uri='http://spmm:8761' --host=spmm --cluster-name=elasticsearch_cfncfn

Create Stream (langstok English public Dbpedia)

    http-naf --vcap.services.eureka-service.credentials.uri='http://spmm:8761' --host=spmm --cluster-name=elasticsearch_cfncfn | langstok-stanford-corenlp | ixa-pipe-parse | ixa-pipe-ned --languages=en --endpoints=http://model.dbpedia-spotlight.org/en/disambiguate | ixa-pipe-time | langstok-wsd-ims | ixa-pipe-srl | ixa-pipe-exec | ixa-pipe-topic | vua-eventcoreference --knowledge-store=http://spmm:3030/newsreader | naf-http --vcap.services.eureka-service.credentials.uri='http://spmm:8761' --host=spmm --cluster-name=elasticsearch_cfncfn
    
## Models ##

[IXA-PIPE-POS](https://github.com/ixa-ehu/ixa-pipe-pos) 

[IXA-PIPE-NERC](https://github.com/ixa-ehu/ixa-pipe-nerc)

[DBPEDIA Spotlight](http://spotlight.sztaki.hu/downloads/latest_models)

[HEIDELTIME](https://github.com/HeidelTime/heideltime)

SRL

Opinion Miner

## Memory usage ##
For optimal performance, models are loaded into memory.
Without max memory settings, the modules use (on a 16GB + swap machine):

    corenlp 3818 (-Xmx4000m)
    srl 2167
    ims 2187
    parse 1200
    exec 530
    topic 373
    eventcoref 1900
    sink 628
    time 434
    ned 314
    source 609 

To prevent JAVA using the SWAP the following deployment properties for memory usage are recommended:

    deployer.langstok-stanford-corenlp.local.javaOpts=-Xmx4000m
    deployer.http-naf.local.javaOpts=-Xmx500m
    deployer.ixa-pipe-parse.local.javaOpts=-Xmx800m 
    deployer.ixa-pipe-ned.local.javaOpts=-Xmx400m
    deployer.ixa-pipe-time.local.javaOpts=-Xmx400m
    deployer.langstok-wsd-ims.local.javaOpts=-Xmx400m
    deployer.ixa-pipe-srl.local.javaOpts=-Xmx2000m
    deployer.ixa-pipe-exec.local.javaOpts=-Xmx400m 
    deployer.ixa-pipe-topic.local.javaOpts=-Xmx400m
    deployer.vua-eventcoreference.local.javaOpts=-Xmx1000m
    deployer.naf-http.local.javaOpts=-Xmx500m