package org.apache.guacamole.request.access.service;

import org.apache.guacamole.request.access.model.AccessRequest;
import org.apache.guacamole.request.access.model.ApprovalHistory;
import org.apache.guacamole.request.access.service.mapper.AccessRequestMapper;
import org.apache.guacamole.request.access.service.mapper.ApprovalHistoryMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Service for managing access requests.
 */
public class AccessRequestService {

    private static final Logger logger = LoggerFactory.getLogger(AccessRequestService.class);

    @Inject
    private SqlSessionFactory sqlSessionFactory;

    @Inject
    private ApprovalHistoryMapper approvalHistoryMapper;

    /**
     * Create a new access request.
     * 
     * @param request The access request to create
     * @return The created access request with ID assigned
     * @throws Exception if there's an error creating the request
     */
    public AccessRequest createRequest(AccessRequest request) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccessRequestMapper mapper = session.getMapper(AccessRequestMapper.class);
            
            // Check for overlapping requests
            List<AccessRequest> overlappingRequests = mapper.getRequestsByConnectionAndTimeRange(
                request.getConnectionId(), request.getStartTime(), request.getEndTime());
            
            if (!overlappingRequests.isEmpty()) {
                throw new Exception("Overlapping request already exists for this connection and time period");
            }
            
            mapper.createRequest(request);
            session.commit();
            
            logger.info("Created access request {} for user {} on connection {}", 
                       request.getRequestId(), request.getUserId(), request.getConnectionId());
            
            return request;
        } catch (PersistenceException e) {
            logger.error("Error creating access request: {}", e.getMessage(), e);
            throw new Exception("Failed to create access request", e);
        }
    }

    /**
     * Get an access request by ID.
     * 
     * @param requestId The request ID
     * @return The access request, or null if not found
     */
    public AccessRequest getRequestById(Integer requestId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccessRequestMapper mapper = session.getMapper(AccessRequestMapper.class);
            return mapper.getRequestById(requestId);
        } catch (PersistenceException e) {
            logger.error("Error retrieving access request {}: {}", requestId, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get all access requests for a specific user.
     * 
     * @param userId The user ID
     * @return List of access requests
     */
    public List<AccessRequest> getRequestsByUser(String userId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccessRequestMapper mapper = session.getMapper(AccessRequestMapper.class);
            return mapper.getRequestsByUser(userId);
        } catch (PersistenceException e) {
            logger.error("Error retrieving requests for user {}: {}", userId, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get all access requests with optional filtering.
     * 
     * @param status Optional status filter
     * @param connectionId Optional connection ID filter
     * @return List of access requests
     */
    public List<AccessRequest> getAllRequests(String status, String connectionId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccessRequestMapper mapper = session.getMapper(AccessRequestMapper.class);
            return mapper.getAllRequests(status, connectionId);
        } catch (PersistenceException e) {
            logger.error("Error retrieving all requests: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get pending requests that need approval.
     * 
     * @return List of pending requests
     */
    public List<AccessRequest> getPendingRequests() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccessRequestMapper mapper = session.getMapper(AccessRequestMapper.class);
            return mapper.getPendingRequests();
        } catch (PersistenceException e) {
            logger.error("Error retrieving pending requests: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Approve an access request.
     * 
     * @param requestId The request ID
     * @param approvedBy The user who approved the request
     * @return true if successful, false otherwise
     */
    public boolean approveRequest(Integer requestId, String approvedBy) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccessRequestMapper mapper = session.getMapper(AccessRequestMapper.class);
            
            AccessRequest request = mapper.getRequestById(requestId);
            if (request == null || !"PENDING".equals(request.getStatus())) {
                return false;
            }
            
            request.setStatus("APPROVED");
            request.setApprovedBy(approvedBy);
            request.setApprovedAt(new Date());
            
            mapper.updateRequest(request);
            
            // Create approval history entry
            ApprovalHistory history = new ApprovalHistory(requestId, "APPROVED", approvedBy, "Request approved");
            approvalHistoryMapper.createHistoryEntry(history);
            
            session.commit();
            
            logger.info("Approved access request {} by {}", requestId, approvedBy);
            return true;
        } catch (PersistenceException e) {
            logger.error("Error approving request {}: {}", requestId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Reject an access request.
     * 
     * @param requestId The request ID
     * @param rejectedBy The user who rejected the request
     * @param reason The reason for rejection
     * @return true if successful, false otherwise
     */
    public boolean rejectRequest(Integer requestId, String rejectedBy, String reason) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccessRequestMapper mapper = session.getMapper(AccessRequestMapper.class);
            
            AccessRequest request = mapper.getRequestById(requestId);
            if (request == null || !"PENDING".equals(request.getStatus())) {
                return false;
            }
            
            request.setStatus("REJECTED");
            request.setApprovedBy(rejectedBy);
            request.setApprovedAt(new Date());
            request.setRejectionReason(reason);
            
            mapper.updateRequest(request);
            
            // Create approval history entry
            ApprovalHistory history = new ApprovalHistory(requestId, "REJECTED", rejectedBy, reason);
            approvalHistoryMapper.createHistoryEntry(history);
            
            session.commit();
            
            logger.info("Rejected access request {} by {} with reason: {}", requestId, rejectedBy, reason);
            return true;
        } catch (PersistenceException e) {
            logger.error("Error rejecting request {}: {}", requestId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Cancel an access request.
     * 
     * @param requestId The request ID
     * @param cancelledBy The user who cancelled the request
     * @return true if successful, false otherwise
     */
    public boolean cancelRequest(Integer requestId, String cancelledBy) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccessRequestMapper mapper = session.getMapper(AccessRequestMapper.class);
            
            AccessRequest request = mapper.getRequestById(requestId);
            if (request == null) {
                return false;
            }
            
            request.setStatus("CANCELLED");
            
            mapper.updateRequest(request);
            
            // Create approval history entry
            ApprovalHistory history = new ApprovalHistory(requestId, "CANCELLED", cancelledBy, "Request cancelled");
            approvalHistoryMapper.createHistoryEntry(history);
            
            session.commit();
            
            logger.info("Cancelled access request {} by {}", requestId, cancelledBy);
            return true;
        } catch (PersistenceException e) {
            logger.error("Error cancelling request {}: {}", requestId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Delete an access request.
     * 
     * @param requestId The request ID
     * @return true if successful, false otherwise
     */
    public boolean deleteRequest(Integer requestId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccessRequestMapper mapper = session.getMapper(AccessRequestMapper.class);
            int result = mapper.deleteRequest(requestId);
            session.commit();
            
            if (result > 0) {
                logger.info("Deleted access request {}", requestId);
                return true;
            }
            return false;
        } catch (PersistenceException e) {
            logger.error("Error deleting request {}: {}", requestId, e.getMessage(), e);
            return false;
        }
    }
}