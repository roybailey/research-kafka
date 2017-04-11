# research-kafka

#### Motivation 

Sandpit for kafka samples.  Stand up simple publish/subscribe tests to work with Spring and Kafka.


#### Running KAFKA 

Tests require running KAFKA, with `kafka.bootstrap.servers` defining the connection details in `application.properties`

Tested this using Docker for Mac which does not use docker-machine, hence 0.0.0.0 is used instead of `docker-machine active` 

Here's the docker command I used to run Kafka locally:

```
docker run -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=0.0.0.0 --env ADVERTISED_PORT=9092 spotify/kafka
```

Use these to test Kafka is running and working (type into producer console to send to consumer console)

Run then from kafka install folder (`kafka_2.11-0.10.1.1` in my testing)

```
export KAFKA=0.0.0.0:9092
./kafka-console-producer.sh --broker-list $KAFKA --topic test
```

```
export KAFKA=0.0.0.0:9092
./kafka-console-consumer.sh --bootstrap-server $KAFKA --topic test
```


#### Motivation 

* `KafkaSampleTest` used the `KafkaSamplePublisher/Consumer` setup to publish simple String messages over Kafka topic
* `KafkaEventTest` used the `KafkaEventPublisher/Consumer` setup to publish `Event<T>` objects over Kafka topic
  * `EventSerializer` is used to encode and decode `Event<T>` messages, using a fixed map of know types that can be transmitted as `Event` payloads

