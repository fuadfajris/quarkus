package com.rnd.consumer;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class EventKafkaConsumer {

    @Incoming("event-in")
    public void receive(String message) {
        System.out.println("Received Event message: " + message);
    }
}
