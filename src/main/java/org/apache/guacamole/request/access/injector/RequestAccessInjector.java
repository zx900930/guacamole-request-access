package org.apache.guacamole.request.access.injector;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.request.access.service.AccessRequestService;
import org.apache.guacamole.request.access.service.ReservationService;
import org.apache.guacamole.request.access.service.mapper.AccessRequestMapper;
import org.apache.guacamole.request.access.service.mapper.ApprovalHistoryMapper;
import org.apache.guacamole.request.access.service.mapper.ReservationMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Guice injector configuration for the Request Access extension.
 */
public class RequestAccessInjector {

    /**
     * Creates and configures the MyBatis SqlSessionFactory.
     */
    @Singleton
    public static class SqlSessionFactoryProvider implements Provider<SqlSessionFactory> {

        @Inject
        private DataSource dataSource;

        @Override
        public SqlSessionFactory get() {
            try {
                TransactionFactory transactionFactory = new JdbcTransactionFactory();
                Environment environment = new Environment("development", transactionFactory, dataSource);

                Configuration configuration = new Configuration(environment);
                
                // Add mappers
                configuration.addMapper(AccessRequestMapper.class);
                configuration.addMapper(ReservationMapper.class);
                configuration.addMapper(ApprovalHistoryMapper.class);

                // Add mapper XML locations
                configuration.addMappers("org.apache.guacamole.request.access.service.mapper");

                return new SqlSessionFactoryBuilder().build(configuration);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create SqlSessionFactory", e);
            }
        }
    }

    /**
     * Guice module for the Request Access extension.
     */
    public static class RequestAccessModule extends MyBatisModule {

        @Override
        protected void initialize() {
            
            // Bind data source
            install(JdbcHelper.MySQL);
            // install(JdbcHelper.PostgreSQL); // Uncomment for PostgreSQL support

            // Bind SqlSessionFactory
            bind(SqlSessionFactory.class).toProvider(SqlSessionFactoryProvider.class).in(Singleton.class);

            // Bind services
            bind(AccessRequestService.class);
            bind(ReservationService.class);

            // Add MyBatis mappers
            addMapperClass(AccessRequestMapper.class);
            addMapperClass(ReservationMapper.class);
            addMapperClass(ApprovalHistoryMapper.class);

            // Environment-specific configuration will be handled through Guacamole's configuration system
            // The extension will read configuration from guacamole.properties
        }
    }
}