#!/bin/sh
SERVICE_NAME=SpringFlowLocal
PATH_TO_JAR=/cfn/spring-cloud-dataflow-server-local/spring-cloud-dataflow-server-local.jar
PID_PATH_NAME=/tmp/SpringFlowLocal-pid
case $1 in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            nohup java -Xmx512m -jar $PATH_TO_JAR --spring.cloud.dataflow.applicationProperties.stream.spring.cloud.stream.kafka.binder.configuration.max.partition.fetch.bytes=3000000 --spring.cloud.dataflow.applicationProperties.stream.spring.cloud.stream.kafka.binder.configuration.message.max.bytes=3000000 --spring.cloud.dataflow.applicationProperties.stream.spring.cloud.stream.kafka.binder.configuration.max.request.size=3000000 /tmp 2>> /dev$
                        echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is already running ..."
        fi
    ;;
    stop)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stoping ..."
            kill $PID;
            echo "$SERVICE_NAME stopped ..."
            rm $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    restart)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stopping ...";
            kill $PID;
            echo "$SERVICE_NAME stopped ...";
            rm $PID_PATH_NAME
            echo "$SERVICE_NAME starting ..."
            nohup java -Xmx512m -jar $PATH_TO_JAR --spring.cloud.dataflow.applicationProperties.stream.spring.cloud.stream.kafka.binder.configuration.max.partition.fetch.bytes=3000000 --spring.cloud.dataflow.applicationProperties.stream.spring.cloud.stream.kafka.binder.configuration.message.max.bytes=3000000 --spring.cloud.dataflow.applicationProperties.stream.spring.cloud.stream.kafka.binder.configuration.max.request.size=3000000 /tmp 2>> /dev$dev$
                        echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
esac