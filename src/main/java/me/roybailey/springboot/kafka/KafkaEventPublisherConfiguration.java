package me.roybailey.springboot.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaEventPublisherConfiguration {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Bean
    public Map eventKafkaPublisherConfiguration() {
        Map props = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections
        // to the Kakfa cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EventSerializer.class);
        // value to block, after which it will throw a TimeoutException
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 15000);

        return props;
    }

    @Bean
    public ProducerFactory eventKafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(eventKafkaPublisherConfiguration());
    }

    @Bean("eventKafkaTemplate")
    public KafkaTemplate kafkaTemplate() {
        return new KafkaTemplate(eventKafkaProducerFactory());
    }

    @Bean
    public KafkaEventPublisher kafkaEventPublisher() {
        return new KafkaEventPublisher();
    }
}