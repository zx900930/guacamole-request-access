package org.apache.guacamole.request.access.model;

import java.util.Date;

/**
 * Model class representing a reservation.
 */
public class Reservation {

    private Integer reservationId;
    private Integer requestId;
    private String userId;
    private String username;
    private String connectionId;
    private String connectionName;
    private Date startTime;
    private Date endTime;
    private String status;
    private Date reservedAt;
    private String reservedBy;

    // Constructors
    public Reservation() {}

    public Reservation(Integer requestId, String userId, String username, 
                      String connectionId, String connectionName, 
                      Date startTime, Date endTime, String reservedBy) {
        this.requestId = requestId;
        this.userId = userId;
        this.username = username;
        this.connectionId = connectionId;
        this.connectionName = connectionName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = "ACTIVE";
        this.reservedAt = new Date();
        this.reservedBy = reservedBy;
    }

    // Getters and Setters
    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

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

    public Date getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(Date reservedAt) {
        this.reservedAt = reservedAt;
    }

    public String getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(String reservedBy) {
        this.reservedBy = reservedBy;
    }
}