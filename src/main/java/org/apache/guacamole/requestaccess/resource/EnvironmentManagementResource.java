package org.apache.guacamole.requestaccess.resource;

import com.google.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.apache.guacamole.requestaccess.model.Environment;
import org.apache.guacamole.requestaccess.service.EnvironmentManagementService;

@Path("/manage/environments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnvironmentManagementResource {

    @Inject
    private EnvironmentManagementService environmentManagementService;

    @GET
    public List<Environment> getEnvironments() {
        return environmentManagementService.getEnvironments();
    }

    @POST
    public void addEnvironment(Environment environment) {
        environmentManagementService.addEnvironment(environment);
    }

    @PUT
    @Path("/{id}")
    public void saveEnvironment(@PathParam("id") int id, Environment environment) {
        environment.setEnvironmentId(id);
        environmentManagementService.saveEnvironment(environment);
    }

    @DELETE
    @Path("/{id}")
    public void deleteEnvironment(@PathParam("id") int id) {
        environmentManagementService.deleteEnvironment(id);
    }

}
