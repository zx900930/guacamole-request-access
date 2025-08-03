package org.apache.guacamole.request.access.service.mapper;

import org.apache.guacamole.request.access.model.AccessRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MyBatis mapper for access request operations.
 */
public interface AccessRequestMapper {

    /**
     * Create a new access request.
     * 
     * @param request The access request to create
     * @return The number of rows affected
     */
    int createRequest(AccessRequest request);

    /**
     * Retrieve an access request by ID.
     * 
     * @param requestId The request ID
     * @return The access request
     */
    AccessRequest getRequestById(@Param("requestId") Integer requestId);

    /**
     * Retrieve all access requests for a specific user.
     * 
     * @param userId The user ID
     * @return List of access requests
     */
    List<AccessRequest> getRequestsByUser(@Param("userId") String userId);

    /**
     * Retrieve all access requests with optional filtering.
     * 
     * @param status Optional status filter
     * @param connectionId Optional connection ID filter
     * @return List of access requests
     */
    List<AccessRequest> getAllRequests(@Param("status") String status, 
                                      @Param("connectionId") String connectionId);

    /**
     * Update an access request.
     * 
     * @param request The access request to update
     * @return The number of rows affected
     */
    int updateRequest(AccessRequest request);

    /**
     * Delete an access request.
     * 
     * @param requestId The request ID
     * @return The number of rows affected
     */
    int deleteRequest(@Param("requestId") Integer requestId);

    /**
     * Get pending requests that need approval.
     * 
     * @return List of pending requests
     */
    List<AccessRequest> getPendingRequests();

    /**
     * Get requests for a specific connection within a time range.
     * 
     * @param connectionId The connection ID
     * @param startTime Start of time range
     * @param endTime End of time range
     * @return List of requests
     */
    List<AccessRequest> getRequestsByConnectionAndTimeRange(@Param("connectionId") String connectionId,
                                                           @Param("startTime") java.util.Date startTime,
                                                           @Param("endTime") java.util.Date endTime);
}