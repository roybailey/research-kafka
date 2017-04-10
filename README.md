# research-kafka
Sandpit for kafka samples


```
docker run -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=`docker-machine ip \`docker-machine active\`` --env ADVERTISED_PORT=9092 spotify/kafka
export KAFKA=`docker-machine ip \`docker-machine active\``:9092
export ZOOKEEPER=`docker-machine ip \`docker-machine active\``:2181
kafka-console-producer.sh --broker-list $KAFKA --topic test
kafka-console-consumer.sh --zookeeper $ZOOKEEPER --topic test
```
