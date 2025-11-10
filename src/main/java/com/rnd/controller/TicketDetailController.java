package com.rnd.controller;

import com.rnd.service.TicketDetailService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;

@Path("/ticket-details")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketDetailController {

    @Inject
    TicketDetailService service;

    @Authenticated
    @GET
    public List<Map<String, Object>> getTicketDetails(@QueryParam("order_id") Long orderId) {
        return service.getTicketDetailsByOrder(orderId);
    }
}