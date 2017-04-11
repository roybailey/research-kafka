# research-kafka
Sandpit for kafka samples

Running KAFKA with Docker for Mac (doesn't use docker-machine, hence 0.0.0.0)

```
docker run -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=0.0.0.0 --env ADVERTISED_PORT=9092 spotify/kafka
```

Use these to test Kafka is running and working (type into producer console to send to consumer console)
```
export KAFKA=0.0.0.0:9092
./kafka-console-producer.sh --broker-list $KAFKA --topic test
```

```
export KAFKA=0.0.0.0:9092
./kafka-console-consumer.sh --bootstrap-server $KAFKA --topic test
```
