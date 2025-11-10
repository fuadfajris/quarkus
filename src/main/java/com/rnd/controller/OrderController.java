package com.rnd.controller;

import com.rnd.service.OrderService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;

@Path("/order-transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    OrderService service;

    @GET
    public List<Map<String, Object>> getOrdersByEvent(@QueryParam("event_id") Long eventId) {
        return service.getOrdersByEvent(eventId);
    }

    @Authenticated
    @GET
    @Path("/order")
    public Map<String, Object> getOrders(
            @QueryParam("event_id") Long eventId,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("limit") @DefaultValue("10") int limit,
            @QueryParam("search") String search
    ) {
        return service.getOrdersByEvent(eventId, page, limit, search);
    }
}
