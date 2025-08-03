package com.guac.request.portal.rest;

import com.google.inject.Inject;
import com.guac.request.portal.dao.RequestDAO;
import com.guac.request.portal.dao.RequestDAOMySQL;
import com.guac.request.portal.dao.RequestDAOPostgreSQL;
import com.guac.request.portal.model.AccessRequest;
import com.guac.request.portal.model.TargetEnvironment;
import com.guac.request.portal.util.GuacamoleProperties;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.UserContext;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

@Path("/request-portal")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RequestPortalRESTResource {

    @Inject
    private UserContext userContext;

    private final RequestDAO requestDAO;

    public RequestPortalRESTResource() throws GuacamoleException {
        Properties properties = GuacamoleProperties.getGuacamoleProperties();
        String databaseType = properties.getProperty("database-type"); // e.g., "mysql" or "postgresql"

        if ("mysql".equalsIgnoreCase(databaseType)) {
            this.requestDAO = new RequestDAOMySQL();
        } else if ("postgresql".equalsIgnoreCase(databaseType)) {
            this.requestDAO = new RequestDAOPostgreSQL();
        } else {
            throw new GuacamoleException("Unsupported database type specified in guacamole.properties: " + databaseType);
        }
    }

    @Path("/requests")
    public AccessRequestResource getAccessRequestResource() {
        return new AccessRequestResource(requestDAO, userContext);
    }

    @Path("/environments")
    public TargetEnvironmentResource getTargetEnvironmentResource() {
        return new TargetEnvironmentResource(requestDAO);
    }
}