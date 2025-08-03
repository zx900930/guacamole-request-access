package com.guac.request.portal;

import com.google.inject.AbstractModule;
import com.guac.request.portal.rest.RequestPortalRESTResource;
public class RequestPortalExtensionModule extends AbstractModule {

    private final RequestPortalExtension extension;

    public RequestPortalExtensionModule(RequestPortalExtension extension) {
        this.extension = extension;
    }

    @Override
    protected void configure() {
        bind(RequestPortalRESTResource.class);
    }
}