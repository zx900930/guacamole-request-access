package org.apache.guacamole.request.access;

import org.apache.guacamole.net.auth.AbstractAuthenticationProvider;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.net.auth.AuthenticatedUser;
import org.apache.guacamole.net.auth.UserContext;
import org.apache.guacamole.net.auth.simple.SimpleAuthenticationProvider;
import org.apache.guacamole.net.auth.simple.SimpleUserContext;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

import java.util.Collections;
import java.util.Map;

/**
 * Authentication provider for the Request Access extension.
 * This provider extends SimpleAuthenticationProvider to add request access functionality.
 */
public class RequestAccessAuthenticationProvider extends SimpleAuthenticationProvider {

    @Override
    public String getIdentifier() {
        return "guacamole-request-access";
    }

    @Override
    public Map<String, GuacamoleConfiguration> getAuthorizedConfigurations(Credentials credentials) throws GuacamoleException {
        // Return empty configurations - this extension doesn't provide direct connections
        // Users will request access through the extension's REST API
        return Collections.emptyMap();
    }

    @Override
    public UserContext getUserContext(AuthenticatedUser authenticatedUser) throws GuacamoleException {
        // Create a simple user context that includes our extension functionality
        // The context will be enhanced with our extension's REST endpoints
        // and frontend resources through the guac-manifest.json
        return new SimpleUserContext(this, authenticatedUser.getIdentifier(), 
                                   getAuthorizedConfigurations(authenticatedUser.getCredentials()), true);
    }
}
