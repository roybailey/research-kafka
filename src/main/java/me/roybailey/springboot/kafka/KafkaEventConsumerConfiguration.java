package me.roybailey.springboot.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.MessageConverter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaEventConsumerConfiguration {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Bean
    public Map eventKafkaConsumerConfiguration() {
        Map props = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections
        // to the Kakfa cluster
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, EventSerializer.class);
        // consumer groups allow a pool of processes to divide the work of
        // consuming and processing records
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "eventdriven");

        return props;
    }

    @Bean
    public ConsumerFactory eventKafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(eventKafkaConsumerConfiguration());
    }

    @Bean("eventKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory eventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setMessageConverter(new MessageConverter() {});
        factory.setConsumerFactory(eventKafkaConsumerFactory());

        return factory;
    }

    @Bean
    public KafkaEventConsumer eventKafkaConsumer() {
        return new KafkaEventConsumer();
    }
}
