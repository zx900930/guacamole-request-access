package org.apache.guacamole.request.access.listener;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.event.listener.Listener;
import org.apache.guacamole.net.event.AuthenticationFailureEvent;
import org.apache.guacamole.net.event.AuthenticationSuccessEvent;
import org.apache.guacamole.net.event.TunnelCloseEvent;
import org.apache.guacamole.net.event.TunnelConnectEvent;
import org.apache.guacamole.request.access.service.AccessRequestService;
import org.apache.guacamole.request.access.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Event listener for the Request Access extension.
 */
public class RequestAccessListener implements Listener {

    private static final Logger logger = LoggerFactory.getLogger(RequestAccessListener.class);

    @Inject
    private AccessRequestService accessRequestService;

    @Inject
    private ReservationService reservationService;

    @Override
    public void handleEvent(Object event) throws GuacamoleException {
        
        if (event instanceof AuthenticationSuccessEvent) {
            handleAuthenticationSuccess((AuthenticationSuccessEvent) event);
        }
        else if (event instanceof AuthenticationFailureEvent) {
            handleAuthenticationFailure((AuthenticationFailureEvent) event);
        }
        else if (event instanceof TunnelConnectEvent) {
            handleTunnelConnect((TunnelConnectEvent) event);
        }
        else if (event instanceof TunnelCloseEvent) {
            handleTunnelClose((TunnelCloseEvent) event);
        }
    }

    /**
     * Handles successful authentication events.
     */
    private void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        try {
            String username = event.getAuthenticatedUser().getCredentials().getUsername();
            logger.info("User {} successfully authenticated", username);
            
            // Expire any outdated reservations
            int expiredCount = reservationService.expireReservations();
            if (expiredCount > 0) {
                logger.info("Expired {} outdated reservations", expiredCount);
            }
            
        } catch (Exception e) {
            logger.error("Error handling authentication success event: {}", e.getMessage(), e);
        }
    }

    /**
     * Handles failed authentication events.
     */
    private void handleAuthenticationFailure(AuthenticationFailureEvent event) {
        try {
            String username = event.getCredentials().getUsername();
            logger.warn("Authentication failed for user {}", username);
            
        } catch (Exception e) {
            logger.error("Error handling authentication failure event: {}", e.getMessage(), e);
        }
    }

    /**
     * Handles tunnel connect events.
     */
    private void handleTunnelConnect(TunnelConnectEvent event) {
        try {
            String username = event.getAuthenticatedUser().getCredentials().getUsername();
            String tunnelUuid = event.getTunnel().getUUID().toString();
            
            logger.info("User {} connected to tunnel {}", username, tunnelUuid);
            
            // Here you could add logic to verify that the user has a valid
            // reservation for this connection and time period
            
        } catch (Exception e) {
            logger.error("Error handling tunnel connect event: {}", e.getMessage(), e);
        }
    }

    /**
     * Handles tunnel close events.
     */
    private void handleTunnelClose(TunnelCloseEvent event) {
        try {
            String username = event.getAuthenticatedUser().getCredentials().getUsername();
            String tunnelUuid = event.getTunnel().getUUID().toString();
            
            logger.info("User {} disconnected from tunnel {}", username, tunnelUuid);
            
            // Here you could add logic to update reservation status
            // or collect usage statistics
            
        } catch (Exception e) {
            logger.error("Error handling tunnel close event: {}", e.getMessage(), e);
        }
    }
}