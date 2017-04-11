package me.roybailey.springboot.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaSampleTest {

    @Autowired
    private KafkaSamplePublisher sender;

    @Autowired
    private KafkaSampleConsumer receiver;

    @Test
    public void testSampleKafkaSendReceive() throws Exception {
        String expected = "Hello Spring Kafka!";
        sender.sendMessage("helloworld.t", expected);

        String message = receiver.getMessage(60);
        assertThat(message).isEqualTo(expected);
    }
}
