# KAFFLOW = KAF &amp; SCDF
##A NLP pipeline build on Spring Cloud Data Flow &amp; KAF Document format
 
Based on the [newsreader](http://www.newsreader-project.eu/) NLP pipeline


# WIP: currently moving to Kubernetes &amp; docker deployment

[news-reader](http://www.newsreader-project.eu/) modules adapted to a microservices-based distributed streaming pipeline. 

[Spring Cloud Data Flow](https://cloud.spring.io/spring-cloud-dataflow/) / [Stream Cloud Stream](http://docs.spring.io/spring-cloud-stream/docs/current-SNAPSHOT/reference/htmlsingle/) 

[Quickstart SCDF](https://cloud.spring.io/spring-cloud-stream-modules/)


- Install [Kafka](https://kafka.apache.org/) & [Zookeeper](https://zookeeper.apache.org/)
- Install (for local usage) [spring-cloud-dataflow-server-local](https://github.com/spring-cloud/spring-cloud-dataflow/tree/master/spring-cloud-dataflow-server-local)

Navigate to the Spring Data Flow Local dashboard [http://localhost:9393/dashboard](http://localhost:9393/dashboard) (default)


## Bulk Import Applications ##

In Spring Data Flow local server

   bulk import [bulkimportapp.properties](bulkimportapp.properties)

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
Disable swapping to make sure to use RAM. Ubuntu: sudo swapoff -a
Or limit swapping: 

Or set max default heapsize:
/etc/profile.d (create chmod 0755 <file>)
export _JAVA_OPTIONS="-Xmx1g"


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

To prevent JAVA using the SWAP the following [deployment properties](deployment.properties) for memory usage are recommended.

## Kafka settings ##
Provide the following argument to stream local server to set the max message size to 3Mb.
    
    --spring.cloud.dataflow.applicationProperties.stream.spring.cloud.stream.kafka.binder.configuration.max.request.size=3000000
    --spring.cloud.dataflow.applicationProperties.stream.spring.cloud.stream.bindings.input.consumer.maxAttempts=1


broker: message.max.bytes
consumer: max.partition.fetch.bytes

kafka/config/server.properties
message.max.bytes = 3000000
session.timeout.ms = 30000 #SRL doesnt make it
retries = 0

spring.cloud.stream.bindings.input.consumer.maxAttempts=1

## Logging ##
WIP

## Kubernetes docker deployement

### Windows 10 remarks

Kubernetes (MiniKube) on Windows 10 (use git bash as admin):

[@JockDaRock - minikube-on-windows-10-with-hyper-v](https://medium.com/@JockDaRock/minikube-on-windows-10-with-hyper-v-6ef0f4dc158c)

 To use hyperv as a driver
    
    minikube start --vm-driver=hyperv --hyperv-virtual-switch="Primary Virtual Switch" --cpus=4 --memory=4096


### [SCDF on kubernetes](https://docs.spring.io/spring-cloud-dataflow-server-kubernetes/docs/current/reference/htmlsingle/#kubernetes-getting-started)

Skipper is not used (so far)

Kafka edit of chapter 1.3

    $ kubectl create -f src/kubernetes/kafka/
    
    $ kubectl create -f src/kubernetes/mysql/
    $ kubectl create -f src/kubernetes/redis/
    
    $ kubectl create -f src/kubernetes/metrics/metrics-deployment-kafka.yaml
    $ kubectl create -f src/kubernetes/metrics/metrics-svc.yaml
    
    $ kubectl create -f src/kubernetes/server/server-roles.yaml
    
    $ kubectl create -f src/kubernetes/server/server-rolebinding.yaml
    
    $ kubectl create -f src/kubernetes/server/service-account.yaml
    $ kubectl create -f src/kubernetes/server/server-config-kafka.yaml
    $ kubectl create -f src/kubernetes/server/server-svc.yaml
    $ kubectl create -f src/kubernetes/server/server-deployment.yaml


Create proxy when running on external machine

    kubectl proxy --address 0.0.0.0 --accept-hosts '.*'


To get minikube ip

    minikube ip
    
To get SCDF service url in minikube

    minikube service --url scdf-server
    kubectl get services my-service
    

SCDF ssh client 

    server-unknown:>dataflow config server --username user --password password --uri http://192.168.99.100:30889 
    app import http://bit.ly/Celsius-BUILD-SNAPSHOT-stream-applications-kafka-10-docker


Get SCDF [ticktock](https://github.com/spring-cloud/spring-cloud-dataflow/tree/master/spring-cloud-dataflow-server-local) running

  
    kubectl logs ticktock-log-76d7bbfcd5-n22kv -f


Manage VirtualBox memory with [modifyvm](https://www.virtualbox.org/manual/ch08.html)
    
    VBoxManage modifyvm

Run Elasticsearch

    https://github.com/kubernetes/examples/tree/master/staging/elasticsearch

Retrieve ElasticSearch clusterinfo

    curl 192.168.99.100:31094/_cluster/health?pretty
    

Create KAFFLOW stream

    stream create kaf3 --definition "kafflow-source-http3 --elasticsearch.cluster_name=myesdb --elasticsearch.port=9300 --elasticsearch.host=elasticsearch | log"


Expose kafflow-source-http

    kubectl get services
    kubectl expose deployment/kaf4-kafflow-source-http4 --type="NodePort" --name kafexpose
    
Post KAF

    kubectl get services
    curl -H "Content-Type: application/json" -X POST -d '{"rawText":"This is a test","language":"en"}' http://192.168.99.100:31949/api/public/naf


