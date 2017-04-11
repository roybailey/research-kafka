package me.roybailey.springboot.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import me.roybailey.springboot.domain.Event;
import me.roybailey.springboot.domain.Todo;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class JsonServiceTest {

    JsonService jsonService = new JsonService();


    @Test
    public void testJsonService_SimpleType() {

        Long expected = 1000000L;
        String encoded = jsonService.encode(expected);
        Long decoded = jsonService.decode(encoded, new TypeReference<Long>() {
        });

        assertThat(decoded).isEqualTo(expected);
    }


    @Test
    public void testJsonService_EventType() {

        Long expected = 1000000L;
        Event<Long> event = Event.<Long>builder()
                .header(ImmutableMap.of("string","test"))
                .payload(expected)
                .build();
        String encoded = jsonService.encode(event);
        Event<Long> decoded = jsonService.decode(encoded, new TypeReference<Event<Long>>() {
        });

        assertThat(decoded.getPayload()).isEqualTo(expected);
    }


    @Test
    public void testJsonService_ComplexEventType() {

        Todo ticket = Todo.builder()
                .key("TEST-KEY")
                .title("TEST-TITLE")
                .dueDate(LocalDate.now().plusDays(5))
                .build();
        Event<Todo> event = Event.<Todo>builder()
                .header(ImmutableMap.of(
                        "content-type", APPLICATION_JSON.getType(),
                        "content-class", Todo.class.getName()
                ))
                .payload(ticket)
                .build();
        String encoded = jsonService.encode(event);
        Event<Todo> decoded = jsonService.decode(encoded, new TypeReference<Event<Todo>>() {
        });

        assertThat(decoded.getPayload()).isEqualTo(ticket);
    }
}
