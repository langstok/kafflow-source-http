- Install Kafka & Zookeeper
- Install Spring Data Flow Local Server (for local usage)

Clone dependencies  

	git clone https://github.com/dbpedia-spotlight/dbpedia-spotlight
	git clone https://github.com/langstok/ixa-pipe-ned.git
    git clone https://github.com/ixa-ehu/ixa-pipe-wikify

wget http://spotlight.sztaki.hu/downloads/dbpedia-spotlight-latest.jar
wget http://spotlight.sztaki.hu/downloads/latest_models/en.tar.gz

Clone news-reader pipeline (LANGSTOK adapatation)


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

Local install applications (for correct users, not necessary after maven central upload)

	mvn install -f ./ixa-pipe-ned/pom.xml
	mvn install -f ./ixa-pipe-wikify/pom.xml

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

Stream

    http-naf | ixa-pipe-exec --directory=/cfn/opinion_miner_deluxePP/opinion_miner_deluxePP --command='python tag_file.py -d news' | naf-http



Bulk import applications in dashboard

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
