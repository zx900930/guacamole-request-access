package org.apache.guacamole.request.access.service;

import org.apache.guacamole.request.access.model.Reservation;
import org.apache.guacamole.request.access.service.mapper.ReservationMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Service for managing reservations.
 */
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Inject
    private SqlSessionFactory sqlSessionFactory;

    /**
     * Create a new reservation.
     * 
     * @param reservation The reservation to create
     * @return The created reservation with ID assigned
     * @throws Exception if there's an error creating the reservation
     */
    public Reservation createReservation(Reservation reservation) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            
            // Check for overlapping reservations
            List<Reservation> overlappingReservations = mapper.getOverlappingReservations(
                reservation.getConnectionId(), reservation.getStartTime(), reservation.getEndTime());
            
            if (!overlappingReservations.isEmpty()) {
                throw new Exception("Overlapping reservation already exists for this connection and time period");
            }
            
            mapper.createReservation(reservation);
            session.commit();
            
            logger.info("Created reservation {} for user {} on connection {}", 
                       reservation.getReservationId(), reservation.getUserId(), reservation.getConnectionId());
            
            return reservation;
        } catch (PersistenceException e) {
            logger.error("Error creating reservation: {}", e.getMessage(), e);
            throw new Exception("Failed to create reservation", e);
        }
    }

    /**
     * Get a reservation by ID.
     * 
     * @param reservationId The reservation ID
     * @return The reservation, or null if not found
     */
    public Reservation getReservationById(Integer reservationId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            return mapper.getReservationById(reservationId);
        } catch (PersistenceException e) {
            logger.error("Error retrieving reservation {}: {}", reservationId, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get all reservations for a specific user.
     * 
     * @param userId The user ID
     * @return List of reservations
     */
    public List<Reservation> getReservationsByUser(String userId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            return mapper.getReservationsByUser(userId);
        } catch (PersistenceException e) {
            logger.error("Error retrieving reservations for user {}: {}", userId, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get all reservations with optional filtering.
     * 
     * @param status Optional status filter
     * @param connectionId Optional connection ID filter
     * @return List of reservations
     */
    public List<Reservation> getAllReservations(String status, String connectionId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            return mapper.getAllReservations(status, connectionId);
        } catch (PersistenceException e) {
            logger.error("Error retrieving all reservations: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get active reservations for a specific connection.
     * 
     * @param connectionId The connection ID
     * @return List of active reservations
     */
    public List<Reservation> getActiveReservationsByConnection(String connectionId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            return mapper.getActiveReservationsByConnection(connectionId);
        } catch (PersistenceException e) {
            logger.error("Error retrieving active reservations for connection {}: {}", connectionId, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Cancel a reservation.
     * 
     * @param reservationId The reservation ID
     * @param cancelledBy The user who cancelled the reservation
     * @return true if successful, false otherwise
     */
    public boolean cancelReservation(Integer reservationId, String cancelledBy) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            
            Reservation reservation = mapper.getReservationById(reservationId);
            if (reservation == null || !"ACTIVE".equals(reservation.getStatus())) {
                return false;
            }
            
            reservation.setStatus("CANCELLED");
            
            mapper.updateReservation(reservation);
            session.commit();
            
            logger.info("Cancelled reservation {} by {}", reservationId, cancelledBy);
            return true;
        } catch (PersistenceException e) {
            logger.error("Error cancelling reservation {}: {}", reservationId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Expire reservations that have passed their end time.
     * 
     * @return The number of reservations expired
     */
    public int expireReservations() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            int count = mapper.expireReservations();
            session.commit();
            
            if (count > 0) {
                logger.info("Expired {} reservations", count);
            }
            
            return count;
        } catch (PersistenceException e) {
            logger.error("Error expiring reservations: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Delete a reservation.
     * 
     * @param reservationId The reservation ID
     * @return true if successful, false otherwise
     */
    public boolean deleteReservation(Integer reservationId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            int result = mapper.deleteReservation(reservationId);
            session.commit();
            
            if (result > 0) {
                logger.info("Deleted reservation {}", reservationId);
                return true;
            }
            return false;
        } catch (PersistenceException e) {
            logger.error("Error deleting reservation {}: {}", reservationId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Check if a connection is available for a given time range.
     * 
     * @param connectionId The connection ID
     * @param startTime Start of time range
     * @param endTime End of time range
     * @return true if available, false otherwise
     */
    public boolean isConnectionAvailable(String connectionId, Date startTime, Date endTime) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReservationMapper mapper = session.getMapper(ReservationMapper.class);
            List<Reservation> overlappingReservations = mapper.getOverlappingReservations(
                connectionId, startTime, endTime);
            return overlappingReservations.isEmpty();
        } catch (PersistenceException e) {
            logger.error("Error checking connection availability: {}", e.getMessage(), e);
            return false;
        }
    }
}