package com.rnd.controller;

import com.rnd.entity.MerchantUser;
import com.rnd.service.MerchantUserService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

@Path("/merchant-users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MerchantUserController {

    @Inject
    MerchantUserService service;

    @POST
    @Path("/login")
    public Map<String, Object> login(Map<String, String> body) throws Exception {
        String email = body.get("email");
        String password = body.get("password");
        return service.login(email, password);
    }

    @Authenticated
    @GET
    public MerchantUser getProfileMerchant(@QueryParam("merchantUsersId") Long merchantUsersId) throws Exception {
        return service.findMerchant(merchantUsersId);
    }

    @Authenticated
    @PUT
    @Path("/{merchantId}")
    public MerchantUser updateProfile(
            @PathParam("merchantId") Long merchantId,
            Map<String, String> body
    ) throws Exception {
        String name = body.get("name");
        String email = body.get("email");
        String password = body.get("password");
        String logo = body.get("logo");
        return service.updateMerchantProfile(merchantId, name, email, password, logo);
    }
}
