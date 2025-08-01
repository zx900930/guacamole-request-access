package org.apache.guacamole.requestaccess;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.AbstractAuthenticationProvider;
import org.apache.guacamole.net.auth.AuthenticatedUser;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.requestaccess.resource.RequestAccessResource;

public class RequestAccessAuthenticationProvider extends AbstractAuthenticationProvider {

    @Override
    public String getIdentifier() {
        return "request-access";
    }

    @Override
    public Object getResource() {
        return RequestAccessResource.class;
    }

    @Override
    public AuthenticatedUser authenticateUser(Credentials credentials) throws GuacamoleException {
        // This extension does not handle authentication, so we return null.
        return null;
    }

}
