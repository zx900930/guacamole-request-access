package com.guac.request.portal.model;

import java.util.Date;

public class AccessRequest {

    private String requestId;
    private String username;
    private String targetEnvironment;
    private String reasonForRequest;
    private int estimatedTime; // Changed to int (hours)
    private Date requestDate;
    private String status;

    public AccessRequest() {
    }

    public AccessRequest(String requestId, String username, String targetEnvironment, String reasonForRequest, int estimatedTime, Date requestDate, String status) {
        this.requestId = requestId;
        this.username = username;
        this.targetEnvironment = targetEnvironment;
        this.reasonForRequest = reasonForRequest;
        this.estimatedTime = estimatedTime;
        this.requestDate = requestDate;
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTargetEnvironment() {
        return targetEnvironment;
    }

    public void setTargetEnvironment(String targetEnvironment) {
        this.targetEnvironment = targetEnvironment;
    }

    public String getReasonForRequest() {
        return reasonForRequest;
    }

    public void setReasonForRequest(String reasonForRequest) {
        this.reasonForRequest = reasonForRequest;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}