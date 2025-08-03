package com.guac.request.portal;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.AuthenticationProvider;
import org.apache.guacamole.properties.GuacamoleProperty;
import org.apache.guacamole.properties.StringGuacamoleProperty;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.DefaultGuacamoleExtension;
import org.apache.guacamole.rest.GuacamoleTunnelingService;
import com.guac.request.portal.rest.RequestPortalRESTResource;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestPortalExtension extends DefaultGuacamoleExtension {

    /**
     * The name of the property which dictates the absolute path to the
     * directory containing the custom HTML files.
     */
    private static final GuacamoleProperty<String> CUSTOM_HTML_DIR = new StringGuacamoleProperty() {

        @Override
        public String getName() {
            return "guac-request-portal-html-dir";
        }

    };

    @Override
    public String getName() {
        return "guac-request-portal";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public List<GuacamoleProperty> getProperties() {
        return Collections.<GuacamoleProperty>singletonList(CUSTOM_HTML_DIR);
    }

    @Override
    public AuthenticationProvider getAuthenticationProvider() throws GuacamoleException {
        // This extension does not provide authentication, so return null.
        return null;
    }

    @Override
    public File getResourceDirectory() throws GuacamoleException {
        Environment environment = new LocalEnvironment();
        return environment.getDefaultExtensionResourceDirectory();
    }

    @Override
    public Set<Object> getRestResources() throws GuacamoleException {
        Set<Object> resources = new HashSet<>();
        resources.add(new RequestPortalRESTResource());
        return resources;
    }

}
