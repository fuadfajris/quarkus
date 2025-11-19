package com.rnd.config;

import io.quarkus.security.AuthenticationFailedException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class AuthenticationFailedExceptionMapper implements ExceptionMapper<AuthenticationFailedException> {
    @Override
    public Response toResponse(AuthenticationFailedException exception) {
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
