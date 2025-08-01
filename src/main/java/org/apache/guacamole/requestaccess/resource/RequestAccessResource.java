package org.apache.guacamole.requestaccess.resource;

import com.google.inject.Inject;
import jakarta.ws.rs.Path;

@Path("request-access")
public class RequestAccessResource {

    @Inject
    private EnvironmentResource environmentResource;

    @Inject
    private ReasonResource reasonResource;

    @Inject
    private RequestManagementResource requestManagementResource;

    @Inject
    private EnvironmentManagementResource environmentManagementResource;

    @Path("environments")
    public EnvironmentResource getEnvironmentResource() {
        return environmentResource;
    }

    @Path("reasons")
    public ReasonResource getReasonResource() {
        return reasonResource;
    }

    @Path("manage")
    public RequestManagementResource getRequestManagementResource() {
        return requestManagementResource;
    }

    @Path("manage/environments")
    public EnvironmentManagementResource getEnvironmentManagementResource() {
        return environmentManagementResource;
    }

}
