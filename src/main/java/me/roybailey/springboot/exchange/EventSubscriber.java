package me.roybailey.springboot.exchange;


import me.roybailey.springboot.domain.event.Event;

@FunctionalInterface
public interface EventSubscriber {

    <T> void handle(Event<T> event);
}
