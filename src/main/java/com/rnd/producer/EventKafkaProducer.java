package com.rnd.producer;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class EventKafkaProducer {

    @Channel("event-out")
    Emitter<String> emitter;

    public void sendEventMessage(String message) {
        emitter.send(message);
    }
}
