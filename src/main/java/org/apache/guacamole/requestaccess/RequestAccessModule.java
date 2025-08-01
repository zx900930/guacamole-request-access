package org.apache.guacamole.requestaccess;

import com.google.inject.AbstractModule;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;
import org.apache.guacamole.requestaccess.db.EnvironmentMapper;
import org.apache.guacamole.requestaccess.db.ReasonMapper;
import org.apache.guacamole.requestaccess.db.RequestMapper;
import org.apache.guacamole.requestaccess.resource.EnvironmentManagementResource;
import org.apache.guacamole.requestaccess.resource.EnvironmentResource;
import org.apache.guacamole.requestaccess.resource.ExportResource;
import org.apache.guacamole.requestaccess.resource.ReasonResource;
import org.apache.guacamole.requestaccess.resource.RequestManagementResource;
import org.apache.guacamole.requestaccess.service.EnvironmentManagementService;
import org.apache.guacamole.requestaccess.service.EnvironmentService;
import org.apache.guacamole.requestaccess.service.ExportService;
import org.apache.guacamole.requestaccess.service.ReasonService;
import org.apache.guacamole.requestaccess.service.RequestManagementService;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.GuacamoleDataSourceProvider;

public class RequestAccessModule extends AbstractModule {

    private final Environment environment;

    public RequestAccessModule() throws GuacamoleException {
        this.environment = new LocalEnvironment();
    }

    @Override
    protected void configure() {
        // Configure MyBatis
        install(new MyBatisModule() {
            @Override
            protected void initialize() {
                bindDataSourceProviderType(GuacamoleDataSourceProvider.class);
                addMapperClass(EnvironmentMapper.class);
                addMapperClass(ReasonMapper.class);
                addMapperClass(RequestMapper.class);
            }
        });

        // Bind services
        bind(EnvironmentService.class);
        bind(ReasonService.class);
        bind(RequestManagementService.class);
        bind(ExportService.class);
        bind(EnvironmentManagementService.class);

        // Bind resources
        bind(EnvironmentResource.class);
        bind(ReasonResource.class);
        bind(RequestManagementResource.class);
        bind(ExportResource.class);
        bind(EnvironmentManagementResource.class);
    }

}
