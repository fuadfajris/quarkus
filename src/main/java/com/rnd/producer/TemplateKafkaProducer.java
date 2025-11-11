package com.rnd.producer;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class TemplateKafkaProducer {

    @Channel("template-out") // channel Kafka di application.properties
    Emitter<String> emitter;

    public boolean sendTemplateEvent(String message) {
        try {
            emitter.send(message)
                    .toCompletableFuture()
                    .get(); // tunggu sampai selesai
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
