package com.rnd.controller;

import com.rnd.service.TemplateService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/templates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TemplateController {

    @Inject
    TemplateService service;

    @Authenticated
    @GET
    public Map<String, Object> getAllTemplates() {
        List<TemplateService.TemplateResponse> templates = service.findAll();
        return Map.of("data", templates);
    }
}
