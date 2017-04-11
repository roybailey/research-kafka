package me.roybailey.springboot.kafka;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.springboot.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


@Slf4j
public class KafkaEventPublisher {

    @Autowired
    @Qualifier("eventKafkaTemplate")
    private KafkaTemplate<Integer, Event> kafkaTemplate;

    public void sendMessage(String topic, Event event) {
        // the KafkaTemplate provides asynchronous send methods returning a Future
        ListenableFuture<SendResult<Integer, Event>> future = kafkaTemplate.send(topic, event);

        // you can register a callback with the listener to receive the result
        // of the send asynchronously
        future.addCallback(
                new ListenableFutureCallback<SendResult<Integer, Event>>() {

                    @Override
                    public void onSuccess(SendResult<Integer, Event> result) {
                        log.info("sent event='{}' with offset={}",
                                event,
                                result.getRecordMetadata().offset());
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        log.error("unable to send event='{}'", event, ex);
                    }
                });

        // alternatively, to block the sending thread, to await the result,
        // invoke the future’s get() method
    }
}
