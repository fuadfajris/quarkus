package com.rnd.controller;

import com.rnd.entity.Event;
import com.rnd.service.EventService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventController {

    @Inject
    EventService service;

    @Authenticated
    @GET
    @Path("/detail")
    public Object getEvent(@QueryParam("eventId") Long eventId,
                           @QueryParam("merchantId") Long merchantId) {
        Event event = service.findEvent(eventId, merchantId);
        if (event == null) {
            return Map.of("message", "Event not found");
        }
        return event;
    }

    @Authenticated
    @GET
    public Object getEventsByMerchant(@QueryParam("merchantId") Long merchantId,
                                      @QueryParam("page") @DefaultValue("1") int page,
                                      @QueryParam("perPage") @DefaultValue("10") int perPage,
                                      @QueryParam("search") String search) {
        if (merchantId == null) {
            return Map.of("message", "merchantId query parameter is required");
        }
        return service.findEventsByMerchant(merchantId, page, perPage, search);
    }

    @Authenticated
    @POST
    public Event createEvent(Event event) {
        return service.createEvent(event);
    }

    @Authenticated
    @PUT
    @Path("/{eventId}")
    public Event updateEvent(@PathParam("eventId") Long eventId, Event eventData) throws Exception {
        return service.updateEvent(eventId, eventData);
    }
}
