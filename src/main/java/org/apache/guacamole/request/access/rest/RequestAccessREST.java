package org.apache.guacamole.request.access.rest;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.AuthenticatedUser;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.request.access.model.AccessRequest;
import org.apache.guacamole.request.access.service.AccessRequestService;
import org.apache.guacamole.request.access.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * REST API endpoints for the Request Access extension.
 */
@Path("/request-access")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RequestAccessREST {

    private static final Logger logger = LoggerFactory.getLogger(RequestAccessREST.class);

    @Inject
    private AccessRequestService accessRequestService;

    @Inject
    private ReservationService reservationService;

    @Inject
    private AuthenticatedUser authenticatedUser;

    /**
     * Submit a new access request.
     */
    @POST
    @Path("/requests")
    public Response submitRequest(Map<String, Object> request_data) {
        try {
            String connectionId = (String) request_data.get("connectionId");
            String connectionName = (String) request_data.get("connectionName");
            String reason = (String) request_data.get("reason");
            Long startTime = (Long) request_data.get("startTime");
            Long endTime = (Long) request_data.get("endTime");

            if (connectionId == null || reason == null || startTime == null || endTime == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Missing required fields\"}")
                        .build();
            }

            AccessRequest request = new AccessRequest(
                    authenticatedUser.getIdentifier(),
                    authenticatedUser.getCredentials().getUsername(),
                    connectionId,
                    connectionName,
                    reason,
                    new Date(startTime),
                    new Date(endTime)
            );

            AccessRequest createdRequest = accessRequestService.createRequest(request);
            return Response.status(Response.Status.CREATED)
                    .entity(createdRequest)
                    .build();

        } catch (Exception e) {
            logger.error("Error submitting access request: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to submit request\"}")
                    .build();
        }
    }

    /**
     * Get all requests for the current user.
     */
    @GET
    @Path("/requests/my")
    public Response getMyRequests() {
        try {
            List<AccessRequest> requests = accessRequestService.getRequestsByUser(authenticatedUser.getIdentifier());
            return Response.ok(requests).build();
        } catch (Exception e) {
            logger.error("Error retrieving user requests: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to retrieve requests\"}")
                    .build();
        }
    }

    /**
     * Get all requests (admin only).
     */
    @GET
    @Path("/requests")
    public Response getAllRequests(@QueryParam("status") String status,
                                   @QueryParam("connectionId") String connectionId) {
        try {
            List<AccessRequest> requests = accessRequestService.getAllRequests(status, connectionId);
            return Response.ok(requests).build();
        } catch (Exception e) {
            logger.error("Error retrieving all requests: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to retrieve requests\"}")
                    .build();
        }
    }

    /**
     * Get pending requests (admin only).
     */
    @GET
    @Path("/requests/pending")
    public Response getPendingRequests() {
        try {
            List<AccessRequest> requests = accessRequestService.getPendingRequests();
            return Response.ok(requests).build();
        } catch (Exception e) {
            logger.error("Error retrieving pending requests: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to retrieve pending requests\"}")
                    .build();
        }
    }

    /**
     * Approve a request (admin only).
     */
    @POST
    @Path("/requests/{requestId}/approve")
    public Response approveRequest(@PathParam("requestId") Integer requestId) {
        try {
            boolean success = accessRequestService.approveRequest(requestId, authenticatedUser.getCredentials().getUsername());
            if (success) {
                return Response.ok("{\"message\": \"Request approved successfully\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Cannot approve request\"}")
                        .build();
            }
        } catch (Exception e) {
            logger.error("Error approving request: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to approve request\"}")
                    .build();
        }
    }

    /**
     * Reject a request (admin only).
     */
    @POST
    @Path("/requests/{requestId}/reject")
    public Response rejectRequest(@PathParam("requestId") Integer requestId,
                                  Map<String, String> rejectionData) {
        try {
            String reason = rejectionData.get("reason");
            if (reason == null || reason.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Rejection reason is required\"}")
                        .build();
            }

            boolean success = accessRequestService.rejectRequest(requestId, authenticatedUser.getCredentials().getUsername(), reason);
            if (success) {
                return Response.ok("{\"message\": \"Request rejected successfully\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Cannot reject request\"}")
                        .build();
            }
        } catch (Exception e) {
            logger.error("Error rejecting request: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to reject request\"}")
                    .build();
        }
    }

    /**
     * Cancel a request.
     */
    @POST
    @Path("/requests/{requestId}/cancel")
    public Response cancelRequest(@PathParam("requestId") Integer requestId) {
        try {
            boolean success = accessRequestService.cancelRequest(requestId, authenticatedUser.getCredentials().getUsername());
            if (success) {
                return Response.ok("{\"message\": \"Request cancelled successfully\"}").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Cannot cancel request\"}")
                        .build();
            }
        } catch (Exception e) {
            logger.error("Error cancelling request: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to cancel request\"}")
                    .build();
        }
    }

    /**
     * Get available connections for requesting access.
     */
    @GET
    @Path("/connections")
    public Response getAvailableConnections() {
        try {
            // This would typically query the database for available connections
            // For now, return a placeholder response
            return Response.ok("[{\"id\": \"conn1\", \"name\": \"Test Connection 1\"}]").build();
        } catch (Exception e) {
            logger.error("Error retrieving connections: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to retrieve connections\"}")
                    .build();
        }
    }
}
