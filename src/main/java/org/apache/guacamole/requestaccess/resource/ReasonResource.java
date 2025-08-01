package org.apache.guacamole.requestaccess.resource;

import com.google.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.apache.guacamole.requestaccess.service.ReasonService;

@Path("reasons")
@Produces(MediaType.APPLICATION_JSON)
public class ReasonResource {

    @Inject
    private ReasonService reasonService;

    @GET
    public List<String> getReasons(@QueryParam("contains") String contains) {
        return reasonService.getReasons(contains);
    }

}
