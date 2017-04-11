package me.roybailey.springboot.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.springboot.domain.Event;
import me.roybailey.springboot.domain.Todo;
import me.roybailey.springboot.service.JsonService;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class EventSerializer implements Serializer<Event>, Deserializer<Event> {

    private static final String PAYLOAD_CLASS_NAME = "PAYLOAD_CLASS_NAME";

    // simple mapping of all payload types for decoding event payloads
    Map<String,TypeReference> mapTypes = ImmutableMap.of(
            Todo.class.getName(), new TypeReference<Todo>() {},
            String.class.getName(), new TypeReference<String>() {}
    );
    JsonService jsonService = new JsonService();

    @Override
    public void configure(Map<String, ?> properties, boolean flag) {
        log.info("configuring event serializer properties={} flag={}", properties, flag);
    }

    @Override
    public byte[] serialize(String key, Event event) {
        // create new header to include payload class type
        HashMap<String, String> transissionHeader = new HashMap<>(event.getHeader());
        transissionHeader.put(PAYLOAD_CLASS_NAME, event.getPayload().getClass().getName());
        // create payload base64 encoded json (avoids json with json in string value)
        String payloadJson = jsonService.encode(event.getPayload());
        String payloadData = Base64.getEncoder().encodeToString(payloadJson.getBytes());
        // create new event with string only payload type for transmission
        Event<String> transmissionEvent = Event.<String>builder()
                .header(transissionHeader)
                .payload(payloadData)
                .build();
        // encode the transmission event with base64 payload and return bytes
        String transmission = jsonService.encode(transmissionEvent);
        log.info("key {} Encoded event as {}", key, transmission);
        return transmission.getBytes();
    }

    @Override
    public Event deserialize(String key, byte[] bytes) {
        Event event = null;
        // decode transmission event (payload is base64 string of json type data)
        Event<String> transimssionEvent = jsonService.decode(new String(bytes), new TypeReference<Event<String>>() {
        });
        // decode transmission payload from base64 json
        String transimssionData = transimssionEvent.getPayload();
        String transimssionPayload = new String(Base64.getDecoder().decode(transimssionData));
        try {
            // create decoded event, with new header (to exclude transmission keys)
            event = new Event();
            HashMap<String, String> eventHeader = new HashMap<>(transimssionEvent.getHeader());
            String payloadClassName = eventHeader.remove(PAYLOAD_CLASS_NAME);
            event.setHeader(eventHeader);
            // create decoded payload as original type
            Object payload = jsonService.decode(transimssionPayload, mapTypes.get(payloadClassName));
            event.setPayload(payload);
        } catch (Exception err) {
            log.error("Failed to decode payload type={}", transimssionEvent.getHeader().get(PAYLOAD_CLASS_NAME));
            err.printStackTrace();
        }
        log.info("key {} Decoded event as {}", key, event);
        return event;
    }

    @Override
    public void close() {
        log.info("closing event serializer");
    }
}
