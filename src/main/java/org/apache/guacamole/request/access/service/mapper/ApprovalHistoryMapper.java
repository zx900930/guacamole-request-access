package org.apache.guacamole.request.access.service.mapper;

import org.apache.guacamole.request.access.model.ApprovalHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MyBatis mapper for approval history operations.
 */
public interface ApprovalHistoryMapper {

    /**
     * Create a new approval history entry.
     * 
     * @param history The approval history entry to create
     * @return The number of rows affected
     */
    int createHistoryEntry(ApprovalHistory history);

    /**
     * Retrieve approval history for a specific request.
     * 
     * @param requestId The request ID
     * @return List of approval history entries
     */
    List<ApprovalHistory> getHistoryByRequest(@Param("requestId") Integer requestId);

    /**
     * Retrieve all approval history entries.
     * 
     * @return List of all approval history entries
     */
    List<ApprovalHistory> getAllHistory();

    /**
     * Get approval history performed by a specific user.
     * 
     * @param performedBy The user who performed the actions
     * @return List of approval history entries
     */
    List<ApprovalHistory> getHistoryByPerformer(@Param("performedBy") String performedBy);
}