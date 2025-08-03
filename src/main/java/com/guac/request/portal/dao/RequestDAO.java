package com.guac.request.portal.dao;

import com.guac.request.portal.model.AccessRequest;
import com.guac.request.portal.model.TargetEnvironment;
import org.apache.guacamole.GuacamoleException;

import java.util.List;

public interface RequestDAO {

    /**
     * Submits a new access request.
     *
     * @param request The AccessRequest object to submit.
     * @throws GuacamoleException If an error occurs during submission.
     */
    void submitAccessRequest(AccessRequest request) throws GuacamoleException;

    /**
     * Retrieves a list of all access requests.
     *
     * @return A list of AccessRequest objects.
     * @throws GuacamoleException If an error occurs during retrieval.
     */
    List<AccessRequest> getAllAccessRequests() throws GuacamoleException;

    /**
     * Approves an access request.
     *
     * @param requestId The ID of the request to approve.
     * @throws GuacamoleException If an error occurs during approval.
     */
    void approveAccessRequest(String requestId) throws GuacamoleException;

    /**
     * Denies an access request.
     *
     * @param requestId The ID of the request to deny.
     * @throws GuacamoleException If an error occurs during denial.
     */
    void denyAccessRequest(String requestId) throws GuacamoleException;

    /**
     * Adds a new target environment.
     *
     * @param environment The TargetEnvironment object to add.
     * @throws GuacamoleException If an error occurs during addition.
     */
    void addTargetEnvironment(TargetEnvironment environment) throws GuacamoleException;

    /**
     * Retrieves a list of all target environments.
     *
     * @return A list of TargetEnvironment objects.
     * @throws GuacamoleException If an error occurs during retrieval.
     */
    List<TargetEnvironment> getAllTargetEnvironments() throws GuacamoleException;

    /**
     * Updates an existing target environment.
     *
     * @param environment The TargetEnvironment object to update.
     * @throws GuacamoleException If an error occurs during update.
     */
    void updateTargetEnvironment(TargetEnvironment environment) throws GuacamoleException;

    /**
     * Deletes a target environment.
     *
     * @param environmentId The ID of the environment to delete.
     * @throws GuacamoleException If an error occurs during deletion.
     */
    void deleteTargetEnvironment(String environmentId) throws GuacamoleException;

}