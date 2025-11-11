package com.rnd.controller;

import com.rnd.producer.TemplateKafkaProducer;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/kafka")
public class KafkaTestController {

    @Inject
    TemplateKafkaProducer kafkaProducer;

    @GET
    @Path("/hello")
    public Response sendHelloWorld() {
        String message = "Hello World";
        boolean sent = kafkaProducer.sendTemplateEvent(message);

        if(sent) {
            return Response.ok("{\"status\":\"success\",\"message\":\"Hello World sent to Kafka\"}").build();
        } else {
            return Response.status(500)
                    .entity("{\"status\":\"error\",\"message\":\"Failed to send message\"}")
                    .build();
        }
    }
}
