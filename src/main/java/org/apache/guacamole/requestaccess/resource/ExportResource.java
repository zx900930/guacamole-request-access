package org.apache.guacamole.requestaccess.resource;

import com.google.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.apache.guacamole.requestaccess.service.ExportService;

@Path("/export")
public class ExportResource {

    @Inject
    private ExportService exportService;

    @GET
    @Path("/{format}")
    @Produces("application/octet-stream")
    public Response exportRequests(@PathParam("format") String format) {
        if (format.equalsIgnoreCase("csv")) {
            return exportService.exportRequestsAsCsv();
        }
        else if (format.equalsIgnoreCase("xlsx")) {
            return exportService.exportRequestsAsXlsx();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}
