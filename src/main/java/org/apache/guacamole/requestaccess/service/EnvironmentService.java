package org.apache.guacamole.requestaccess.service;

import com.google.inject.Inject;
import java.util.List;
import org.apache.guacamole.requestaccess.model.Environment;
import org.apache.guacamole.requestaccess.db.EnvironmentMapper;

public class EnvironmentService {

    @Inject
    private EnvironmentMapper environmentMapper;

    public List<Environment> getEnvironments() {
        return environmentMapper.selectAll();
    }

}
