package org.apache.guacamole.requestaccess.resource;

import com.google.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.apache.guacamole.requestaccess.model.Request;
import org.apache.guacamole.requestaccess.service.RequestManagementService;

@Path("/manage/requests")
@Produces(MediaType.APPLICATION_JSON)
public class RequestManagementResource {

    @Inject
    private RequestManagementService requestManagementService;

    @Inject
    private ExportResource exportResource;

    @GET
    public List<Request> getRequests() {
        return requestManagementService.getRequests();
    }

    @PUT
    @Path("/{id}/approve")
    public void approveRequest(@PathParam("id") int id) {
        requestManagementService.approveRequest(id);
    }

    @PUT
    @Path("/{id}/deny")
    public void denyRequest(@PathParam("id") int id) {
        requestManagementService.denyRequest(id);
    }

    @Path("/export")
    public ExportResource getExportResource() {
        return exportResource;
    }

}
