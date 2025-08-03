package com.guac.request.portal.rest;

import com.guac.request.portal.dao.RequestDAO;
import com.guac.request.portal.model.TargetEnvironment;
import org.apache.guacamole.GuacamoleException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

public class TargetEnvironmentResource {

    private final RequestDAO requestDAO;

    public TargetEnvironmentResource(RequestDAO requestDAO) {
        this.requestDAO = requestDAO;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTargetEnvironment(TargetEnvironment environment) throws GuacamoleException {
        environment.setEnvironmentId(UUID.randomUUID().toString());
        requestDAO.addTargetEnvironment(environment);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TargetEnvironment> getAllTargetEnvironments() throws GuacamoleException {
        return requestDAO.getAllTargetEnvironments();
    }

    @PUT
    @Path("/{environmentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTargetEnvironment(@PathParam("environmentId") String environmentId, TargetEnvironment environment) throws GuacamoleException {
        environment.setEnvironmentId(environmentId);
        requestDAO.updateTargetEnvironment(environment);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{environmentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTargetEnvironment(@PathParam("environmentId") String environmentId) throws GuacamoleException {
        requestDAO.deleteTargetEnvironment(environmentId);
        return Response.ok().build();
    }
}