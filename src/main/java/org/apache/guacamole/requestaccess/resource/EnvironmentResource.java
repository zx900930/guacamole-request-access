package org.apache.guacamole.requestaccess.resource;

import com.google.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.apache.guacamole.requestaccess.model.Environment;
import org.apache.guacamole.requestaccess.service.EnvironmentService;

@Path("environments")
@Produces(MediaType.APPLICATION_JSON)
public class EnvironmentResource {

    @Inject
    private EnvironmentService environmentService;

    @GET
    public List<Environment> getEnvironments() {
        return environmentService.getEnvironments();
    }

}
