package org.apache.guacamole.request.access.model;

import java.util.Date;

/**
 * Model class representing an access request.
 */
public class AccessRequest {

    private Integer requestId;
    private String userId;
    private String username;
    private String connectionId;
    private String connectionName;
    private String reason;
    private Date startTime;
    private Date endTime;
    private String status;
    private Date submittedAt;
    private Date updatedAt;
    private String approvedBy;
    private Date approvedAt;
    private String rejectionReason;

    // Constructors
    public AccessRequest() {}

    public AccessRequest(String userId, String username, String connectionId, 
                        String connectionName, String reason, Date startTime, Date endTime) {
        this.userId = userId;
        this.username = username;
        this.connectionId = connectionId;
        this.connectionName = connectionName;
        this.reason = reason;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = "PENDING";
        this.submittedAt = new Date();
    }

    // Getters and Setters
    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Date approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}