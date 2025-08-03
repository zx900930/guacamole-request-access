package com.guac.request.portal.util;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.environment.Environment;
import org.apache.guacamole.environment.LocalEnvironment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GuacamoleProperties {

    private static final String GUACAMOLE_PROPERTIES_FILE = "guacamole.properties";

    public static Properties getGuacamoleProperties() throws GuacamoleException {
        Properties properties = new Properties();
        Environment environment = new LocalEnvironment();
        File propertiesFile = new File(environment.getGuacamoleHome(), GUACAMOLE_PROPERTIES_FILE);

        if (!propertiesFile.exists()) {
            throw new GuacamoleException("Guacamole properties file not found: " + propertiesFile.getAbsolutePath());
        }

        try (InputStream input = new FileInputStream(propertiesFile)) {
            properties.load(input);
        } catch (IOException e) {
            throw new GuacamoleException("Error reading Guacamole properties file", e);
        }
        return properties;
    }
}