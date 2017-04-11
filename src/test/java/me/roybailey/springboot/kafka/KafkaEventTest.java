package me.roybailey.springboot.kafka;

import com.google.common.collect.ImmutableMap;
import me.roybailey.springboot.domain.Event;
import me.roybailey.springboot.domain.Todo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaEventTest {

    @Autowired
    private KafkaEventPublisher sender;

    @Autowired
    private KafkaEventConsumer receiver;


    @Test
    public void testKafkaComplexEvent() throws Exception {
        Todo todo = Todo.builder()
                .key("todo1")
                .title("Wash Car")
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        sender.sendMessage("eventdriven.t", Event.builder()
                .header(ImmutableMap.of("type", "Todo"))
                .payload(todo)
                .build());

        Event<Todo> event = receiver.getEvent(60);
        assertThat(event).isNotNull();
        assertThat(event.getHeader())
                .hasSize(1)
                .containsEntry("type", "Todo");
        assertThat(event.getPayload())
                .isNotNull()
                .isEqualTo(todo);
    }


    @Test
    public void testKafkaStringEvent() throws Exception {
        String message = "Simple String";

        sender.sendMessage("eventdriven.t", Event.builder()
                .header(ImmutableMap.of("type", "String"))
                .payload(message)
                .build());

        Event<String> event = receiver.getEvent(60);
        assertThat(event).isNotNull();
        assertThat(event.getHeader())
                .hasSize(1)
                .containsEntry("type", "String");
        assertThat(event.getPayload())
                .isNotNull()
                .isEqualTo("Simple String");
    }
}
