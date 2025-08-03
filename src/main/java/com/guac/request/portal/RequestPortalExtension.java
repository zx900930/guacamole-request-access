package com.guac.request.portal;

import com.google.inject.AbstractModule;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.api.GuacamoleExtension;
import org.apache.guacamole.net.auth.AuthenticationProvider;

public class RequestPortalExtension implements GuacamoleExtension {

    @Override
    public String getName() {
        return "guac-request-portal";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public AbstractModule getModule() throws GuacamoleException {
        return new RequestPortalExtensionModule(this);
    }

    @Override
    public AuthenticationProvider getAuthenticationProvider() throws GuacamoleException {
        return null;
    }
}