package com.rnd.controller;

import com.rnd.dto.request.CreateGuestScheduleRequest;
import com.rnd.dto.request.UpdateGuestScheduleRequest;
import com.rnd.entity.Guest;
import com.rnd.entity.GuestSchedule;
import com.rnd.service.GuestService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Path("/guests")
@Produces(MediaType.APPLICATION_JSON)
public class GuestController {

    @Inject
    GuestService service;

    @Authenticated
    @GET
    @Path("/lineup")
    public List<Guest> getAllLineup() {
        return service.fetchAllLineup();
    }

    @Authenticated
    @GET
    @Path("/lineup/{eventId}")
    public List<GuestSchedule> getLineup(@PathParam("eventId") Long eventId) {
        return service.fetchLineupByEventId(eventId);
    }

    @Authenticated
    @GET
    @Path("/lineup/{guestScheduleId}/detail")
    public Response getDetail(@PathParam("guestScheduleId") Long guestScheduleId) {
        GuestSchedule schedule = service.findDetailById(guestScheduleId);
        if (schedule == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "Guest schedule not found"))
                    .build();
        }
        return Response.ok(schedule).build();
    }

    @Authenticated
    @POST
    @Path("/lineup")
    public Response create(CreateGuestScheduleRequest dto) {
        GuestSchedule schedule = service.createSchedule(dto);
        return Response.status(Response.Status.CREATED).entity(schedule).build();
    }

    @Authenticated
    @PUT
    @Path("/lineup/{id}")
    @Transactional
    public GuestSchedule updateSchedule(@PathParam("id") Long id, UpdateGuestScheduleRequest dto) {
        return service.updateSchedule(id, dto);
    }

    @Authenticated
    @DELETE
    @Path("/lineup/{id}")
    @Transactional
    public Response deleteSchedule(@PathParam("id") Long id) {
        service.deleteSchedule(id);
        return Response.ok(Collections.singletonMap("message",
                "Guest schedule with ID " + id + " deleted successfully")).build();
    }

}
