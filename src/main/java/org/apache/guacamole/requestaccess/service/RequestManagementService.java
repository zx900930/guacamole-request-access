package org.apache.guacamole.requestaccess.service;

import com.google.inject.Inject;
import java.util.List;
import org.apache.guacamole.requestaccess.db.RequestMapper;
import org.apache.guacamole.requestaccess.model.Request;

public class RequestManagementService {

    @Inject
    private RequestMapper requestMapper;

    public List<Request> getRequests() {
        return requestMapper.selectAll();
    }

    public void approveRequest(int requestId) {
        requestMapper.updateStatus(requestId, "APPROVED");
    }

    public void denyRequest(int requestId) {
        requestMapper.updateStatus(requestId, "DENIED");
    }

}
