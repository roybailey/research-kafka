package me.roybailey.springboot.exchange;

import me.roybailey.springboot.domain.event.Event;


@FunctionalInterface
public interface EventPublisher {

    <T> void send(Event<T> event);
}
