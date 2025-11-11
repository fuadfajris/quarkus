package com.rnd.consumer;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class TemplateKafkaConsumer {

    @Incoming("template-in")
    public void receive(String message) {
        System.out.println("Received from Kafka: " + message);
    }
}
