package org.apache.guacamole.requestaccess.service;

import com.google.inject.Inject;
import java.util.List;
import org.apache.guacamole.requestaccess.db.EnvironmentMapper;
import org.apache.guacamole.requestaccess.model.Environment;

public class EnvironmentManagementService {

    @Inject
    private EnvironmentMapper environmentMapper;

    public List<Environment> getEnvironments() {
        return environmentMapper.selectAll();
    }

    public void addEnvironment(Environment environment) {
        environmentMapper.insert(environment);
    }

    public void saveEnvironment(Environment environment) {
        environmentMapper.update(environment);
    }

    public void deleteEnvironment(int environmentId) {
        environmentMapper.delete(environmentId);
    }

}
