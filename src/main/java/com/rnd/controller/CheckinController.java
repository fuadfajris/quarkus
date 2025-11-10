package com.rnd.controller;

import com.rnd.service.CheckinService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/checkins")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CheckinController {

    @Inject
    CheckinService service;

    @GET
    public List<Map<String, Object>> getCheckins(@QueryParam("event_id") Long eventId) {
        return service.getCheckinsByEvent(eventId);
    }

    @POST
    @Transactional
    public Map<String, Object> createCheckin(Map<String, Long> body) {
        Long ticketDetailId = body.get("ticket_detail_id");
        return service.createCheckin(ticketDetailId);
    }

    @GET
    @Path("all-checkins")
    public List<Map<String, Object>> getCheckinsV2(@QueryParam("event_id") Long eventId) {
        return service.getCheckinsByEventV2(eventId);
    }
}

