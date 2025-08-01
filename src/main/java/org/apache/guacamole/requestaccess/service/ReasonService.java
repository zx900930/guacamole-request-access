package org.apache.guacamole.requestaccess.service;

import com.google.inject.Inject;
import java.util.List;
import org.apache.guacamole.requestaccess.db.ReasonMapper;

public class ReasonService {

    @Inject
    private ReasonMapper reasonMapper;

    public List<String> getReasons(String contains) {
        return reasonMapper.select(contains);
    }

}
