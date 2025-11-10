package com.rnd.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldController {

    @GET
    public String hello() throws Exception {
        return "Hello World";
    }
}
