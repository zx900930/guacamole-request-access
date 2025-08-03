package org.apache.guacamole.request.access.model;

import java.util.Date;

/**
 * Model class representing approval history.
 */
public class ApprovalHistory {

    private Integer historyId;
    private Integer requestId;
    private String action;
    private String performedBy;
    private Date performedAt;
    private String comments;

    // Constructors
    public ApprovalHistory() {}

    public ApprovalHistory(Integer requestId, String action, String performedBy, String comments) {
        this.requestId = requestId;
        this.action = action;
        this.performedBy = performedBy;
        this.performedAt = new Date();
        this.comments = comments;
    }

    // Getters and Setters
    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public Date getPerformedAt() {
        return performedAt;
    }

    public void setPerformedAt(Date performedAt) {
        this.performedAt = performedAt;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}