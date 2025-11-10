package com.rnd.controller;

import com.rnd.entity.Activity;
import com.rnd.service.ActivityService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;

@Path("/activity")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActivityController {

    @Inject
    ActivityService service;

    @Authenticated
    @GET
    @Path("check-pending")
    public Map<String, Object> checkPending(
            @QueryParam("id") Long id,
            @QueryParam("merchantId") Long merchantId,
            @QueryParam("contentKey") String contentKey
    ) {
        if (id == null || merchantId == null || contentKey == null) {
            return Map.of("message", "Missing required query parameters");
        }
        boolean pending = service.checkPending(id, merchantId, contentKey);
        return Map.of("pending", pending);
    }

    @Authenticated
    @GET
    public Map<String, Object> getAll(
            @QueryParam("merchantId") Long merchantId,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("perPage") @DefaultValue("10") int perPage
    ) {
        return service.findAllByMerchant(merchantId, page, perPage);
    }

    @Authenticated
    @GET
    @Path("{id}")
    public Activity getOne(@PathParam("id") Long id) {
        return service.findOne(id);
    }

    @Authenticated
    @POST
    @Transactional
    public Activity create(Map<String, Object> body) {
        return service.create(body);
    }

    @Authenticated
    @PUT
    @Path("{id}")
    @Transactional
    public Activity update(
            @PathParam("id") Long id,
            Map<String, Object> body
    ) {
        Map<String, Object> approver = (Map<String, Object>) body.get("approver");
        String status = (String) body.get("status");
        return service.update(id, approver, status);
    }

    @Authenticated
    @DELETE
    @Path("{id}")
    @Transactional
    public Map<String, Object> remove(@PathParam("id") Long id) {
        return service.delete(id);
    }
}
