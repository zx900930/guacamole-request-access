package org.apache.guacamole.requestaccess.db;

import java.util.List;
import org.apache.guacamole.requestaccess.model.Environment;

public interface EnvironmentMapper {

    List<Environment> selectAll();

    void insert(Environment environment);

    void update(Environment environment);

    void delete(int environmentId);

}
