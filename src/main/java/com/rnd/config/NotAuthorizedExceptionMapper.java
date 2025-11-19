package com.rnd.config;

import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class NotAuthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException> {
    @Override
    public Response toResponse(NotAuthorizedException exception) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .header("WWW-Authenticate", "Bearer")
                .entity(Map.of(
                        "error", "unauthorized",
                        "message", "Unauthorized"
                ))
                .build();
    }
}