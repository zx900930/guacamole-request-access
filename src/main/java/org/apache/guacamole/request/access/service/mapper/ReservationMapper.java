package org.apache.guacamole.request.access.service.mapper;

import org.apache.guacamole.request.access.model.Reservation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MyBatis mapper for reservation operations.
 */
public interface ReservationMapper {

    /**
     * Create a new reservation.
     * 
     * @param reservation The reservation to create
     * @return The number of rows affected
     */
    int createReservation(Reservation reservation);

    /**
     * Retrieve a reservation by ID.
     * 
     * @param reservationId The reservation ID
     * @return The reservation
     */
    Reservation getReservationById(@Param("reservationId") Integer reservationId);

    /**
     * Retrieve all reservations for a specific user.
     * 
     * @param userId The user ID
     * @return List of reservations
     */
    List<Reservation> getReservationsByUser(@Param("userId") String userId);

    /**
     * Retrieve all reservations for a specific request.
     * 
     * @param requestId The request ID
     * @return List of reservations
     */
    List<Reservation> getReservationsByRequest(@Param("requestId") Integer requestId);

    /**
     * Retrieve all reservations with optional filtering.
     * 
     * @param status Optional status filter
     * @param connectionId Optional connection ID filter
     * @return List of reservations
     */
    List<Reservation> getAllReservations(@Param("status") String status,
                                        @Param("connectionId") String connectionId);

    /**
     * Update a reservation.
     * 
     * @param reservation The reservation to update
     * @return The number of rows affected
     */
    int updateReservation(Reservation reservation);

    /**
     * Delete a reservation.
     * 
     * @param reservationId The reservation ID
     * @return The number of rows affected
     */
    int deleteReservation(@Param("reservationId") Integer reservationId);

    /**
     * Get active reservations for a specific connection.
     * 
     * @param connectionId The connection ID
     * @return List of active reservations
     */
    List<Reservation> getActiveReservationsByConnection(@Param("connectionId") String connectionId);

    /**
     * Get reservations that overlap with a given time range for a connection.
     * 
     * @param connectionId The connection ID
     * @param startTime Start of time range
     * @param endTime End of time range
     * @return List of overlapping reservations
     */
    List<Reservation> getOverlappingReservations(@Param("connectionId") String connectionId,
                                                @Param("startTime") java.util.Date startTime,
                                                @Param("endTime") java.util.Date endTime);

    /**
     * Expire reservations that have ended.
     * 
     * @return The number of rows affected
     */
    int expireReservations();
}